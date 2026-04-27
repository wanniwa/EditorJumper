package com.github.wanniwa.editorjumper.editors

import com.github.wanniwa.editorjumper.settings.EditorJumperProjectSettings
import com.github.wanniwa.editorjumper.utils.WslUtils
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.SystemInfo
import java.io.File

/**
 * Static metadata for one editor entry.
 * All fields come from editors.json; no handler instance is needed to read them.
 */
data class EditorConfig(
    val name: String,
    val macPath: String = "",
    val winPath: String = "",
    val linuxPath: String = "",
    val macOpenName: String? = null,
    /** If true, getOpenCommand respects the per-project VSCode workspace path setting. */
    val supportsWorkspace: Boolean = false,
    /** If true, project/file paths are quoted to avoid spaces being split (e.g. Cursor). */
    val quotePaths: Boolean = false,
    /** If true, the editor supports --folder-uri / --remote for WSL remote development. */
    val supportsWsl: Boolean = false,
)

/**
 * Thin wrapper that pairs a display name with a factory function.
 * EditorRegistry holds a list of these; everything else is driven from it.
 *
 * To add a new editor: add one entry to editors.json — no Kotlin code needed.
 */
data class EditorRegistration(
    val name: String,
    private val factory: (String, Project?) -> EditorHandler,
) {
    fun create(path: String, project: Project?): EditorHandler = factory(path, project)
}

interface EditorHandler {
    fun getName(): String
    fun getPath(): String
    fun getDefaultPath(): String

    /**
     * 获取打开命令
     * @param projectPath 项目路径
     * @param filePath 文件路径，可能为null
     * @param lineNumber 行号，可能为null
     * @param columnNumber 列号，可能为null
     * @return 命令数组
     */
    fun getOpenCommand(
        projectPath: String,
        filePath: String?,
        lineNumber: Int?,
        columnNumber: Int?
    ): Array<String>

    fun getFastOpenCommand(
        projectPath: String,
        filePath: String?,
        lineNumber: Int?,
        columnNumber: Int?
    ): Array<String>

    /**
     * 获取Mac URL方案名称（如 "cursor", "vscode" 等）
     * @return URL方案名称，如果不支持则返回null
     */
    fun getMacOpenName(): String? = null

    /**
     * 是否在命令行中对 projectPath/filePath 加引号。
     * 默认为 false，由具体实现（现在是配置）决定。
     */
    fun shouldQuotePaths(): Boolean = false
}

abstract class BaseEditorHandler(private val customPath: String?) : EditorHandler {
    override fun getPath(): String {
        return if (customPath.isNullOrEmpty()) getDefaultPath() else customPath
    }

    override fun getOpenCommand(
        projectPath: String,
        filePath: String?,
        lineNumber: Int?,
        columnNumber: Int?
    ): Array<String> {
        val macAppName = getName()
        val quote = shouldQuotePaths()

        // WSL remote development: only when distro is resolvable
        if (SystemInfo.isWindows && supportsWsl() && WslUtils.isWslPath(projectPath)) {
            val distro = WslUtils.extractDistro(projectPath)
            if (distro != null) {
                return buildWslCommand(distro, projectPath, filePath, lineNumber, columnNumber)
            }
        }

        return when {
            filePath != null -> {
                val actualLineNumber = lineNumber ?: 1
                val actualColumnNumber = columnNumber ?: 1
                // 项目路径和文件路径加引号，防止空格被分词
                val quotedProjectPath = if (quote) "\"$projectPath\"" else projectPath
                val quotedFilePath = if (quote) "\"$filePath\"" else filePath
                val fileWithPosition = "$quotedFilePath:$actualLineNumber:$actualColumnNumber"
                if (SystemInfo.isWindows && getPath() == getDefaultPath()) {
                    arrayOf("cmd", "/c", getPath(), quotedProjectPath, "--goto", fileWithPosition)
                } else {
                    arrayOf(getPath(), quotedProjectPath, "--goto", fileWithPosition)
                }
            }

            else -> {
                // 只打开项目
                if (SystemInfo.isWindows && getPath() == getDefaultPath()) {
                    arrayOf("cmd", "/c", getPath(), projectPath)
                } else if (SystemInfo.isMac) {
                    arrayOf("open", "-a", macAppName, projectPath)
                } else {
                    arrayOf(getPath(), projectPath)
                }
            }
        }
    }

    /**
     * Build command for opening a file/project in a WSL remote session.
     * Uses --folder-uri to open the project in WSL mode.
     *
     * @param distro the pre-validated WSL distribution name (never empty).
     */
    private fun buildWslCommand(
        distro: String,
        projectPath: String,
        filePath: String?,
        lineNumber: Int?,
        columnNumber: Int?
    ): Array<String> {
        val linuxProjectPath = WslUtils.toLinuxPath(projectPath)
        val folderUri = "vscode-remote://wsl+$distro$linuxProjectPath"
        val editorPath = getPath()
        val quote = shouldQuotePaths()
        val useCmd = SystemInfo.isWindows && editorPath == getDefaultPath()

        val quotedFolderUri = if (useCmd || quote) "\"$folderUri\"" else folderUri

        val base = if (useCmd) {
            arrayOf("cmd", "/c", editorPath)
        } else {
            arrayOf(editorPath)
        }

        return if (filePath != null) {
            val linuxFilePath = if (WslUtils.isWslPath(filePath)) {
                WslUtils.toLinuxPath(filePath)
            } else {
                filePath
            }
            val actualLineNumber = lineNumber ?: 1
            val actualColumnNumber = columnNumber ?: 1
            val gotoArg = "$linuxFilePath:$actualLineNumber:$actualColumnNumber"
            val finalGotoArg = if (quote) "\"$gotoArg\"" else gotoArg
            base + arrayOf("--folder-uri", quotedFolderUri, "--goto", finalGotoArg)
        } else {
            base + arrayOf("--folder-uri", quotedFolderUri)
        }
    }

    /**
     * Whether this handler supports WSL remote development.
     * Override in subclasses that have access to EditorConfig.
     */
    protected open fun supportsWsl(): Boolean = false

    override fun getFastOpenCommand(
        projectPath: String,
        filePath: String?,
        lineNumber: Int?,
        columnNumber: Int?
    ): Array<String> {
        val macOpenName = getMacOpenName()
        val macAppName = getName()
        if (SystemInfo.isWindows) {
            return getOpenCommand(projectPath, filePath, lineNumber, columnNumber)
        }
        return when {
            filePath.isNullOrBlank() -> {
                arrayOf("open", "-a", macAppName, projectPath)
            }

            else -> {
                arrayOf(
                    "open",
                    "-a",
                    macAppName,
                    "$macOpenName://file$filePath:$lineNumber:$columnNumber"
                )
            }
        }
    }
}

/**
 * A single generic handler driven entirely by [EditorConfig].
 * Replaces all the per-editor Handler subclasses that only differed in name / path / URL scheme.
 */
class ConfigBasedEditorHandler(
    private val cfg: EditorConfig,
    customPath: String?,
    private val project: Project?,
) : BaseEditorHandler(customPath) {

    override fun getName(): String = cfg.name

    override fun getDefaultPath(): String = when {
        SystemInfo.isMac -> cfg.macPath
        SystemInfo.isWindows -> cfg.winPath
        else -> cfg.linuxPath
    }

    override fun getMacOpenName(): String? = cfg.macOpenName

    override fun getOpenCommand(
        projectPath: String,
        filePath: String?,
        lineNumber: Int?,
        columnNumber: Int?,
    ): Array<String> = super.getOpenCommand(resolveProjectPath(projectPath), filePath, lineNumber, columnNumber)

    override fun getFastOpenCommand(
        projectPath: String,
        filePath: String?,
        lineNumber: Int?,
        columnNumber: Int?,
    ): Array<String> = super.getFastOpenCommand(resolveProjectPath(projectPath), filePath, lineNumber, columnNumber)

    private fun resolveProjectPath(projectPath: String): String {
        if (!cfg.supportsWorkspace) return projectPath
        val workspacePath = project
            ?.let { EditorJumperProjectSettings.getInstance(it) }
            ?.vsCodeWorkspacePath
        return if (!workspacePath.isNullOrBlank() && File(workspacePath).exists()) workspacePath
        else projectPath
    }

    override fun shouldQuotePaths(): Boolean = cfg.quotePaths

    override fun supportsWsl(): Boolean = cfg.supportsWsl
}
