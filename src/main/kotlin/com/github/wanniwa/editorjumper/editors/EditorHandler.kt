package com.github.wanniwa.editorjumper.editors

import com.github.wanniwa.editorjumper.settings.EditorJumperProjectSettings
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.SystemInfo
import java.io.File

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
        return when {
            filePath != null -> {
                val actualLineNumber = lineNumber ?: 1
                val actualColumnNumber = columnNumber ?: 1
                // 如果有文件路径和光标位置，则打开项目并定位到文件的具体行列
                val fileWithPosition = "$filePath:$actualLineNumber:$actualColumnNumber"
                if (SystemInfo.isWindows && getPath() == getDefaultPath()) {
                    arrayOf("cmd", "/c", getPath(), projectPath, "--goto", fileWithPosition)
                } else {
                    arrayOf(getPath(), projectPath, "--goto", fileWithPosition)
                }
            }

            else -> {
                // 只打开项目
                if (SystemInfo.isWindows && getPath() == getDefaultPath()) {
                    arrayOf("cmd", "/c", getPath(), projectPath)
                } else if (SystemInfo.isMac) {
                    arrayOf("open", "-a", "$macAppName", projectPath)
                } else {
                    arrayOf(getPath(), projectPath)
                }
            }
        }
    }

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
                arrayOf("open", "-a", "$macAppName", projectPath)
            }
            else -> {
                arrayOf("open", "-a", "$macAppName", "$macOpenName://file$filePath:$lineNumber:$columnNumber")
            }
        }
    }
}

abstract class BaseVscodeEditorHandler(customPath: String?, val project: Project?) :
    BaseEditorHandler(customPath) {
    override fun getOpenCommand(
        projectPath: String,
        filePath: String?,
        lineNumber: Int?,
        columnNumber: Int?
    ): Array<String> {

        // 如果配置了工作空间文件，优先使用工作空间文件
        val projectSettings = project?.let { EditorJumperProjectSettings.getInstance(it) }
        val workspacePath = projectSettings?.vsCodeWorkspacePath

        // 如果配置了工作空间文件且文件存在，使用工作空间文件
        if (!workspacePath.isNullOrBlank() && File(workspacePath).exists()) {
            return super.getOpenCommand(workspacePath, filePath, lineNumber, columnNumber)
        } else {
            return super.getOpenCommand(projectPath, filePath, lineNumber, columnNumber)
        }
    }

    override fun getFastOpenCommand(
        projectPath: String,
        filePath: String?,
        lineNumber: Int?,
        columnNumber: Int?
    ): Array<String> {
        // 如果配置了工作空间文件，优先使用工作空间文件
        val projectSettings = project?.let { EditorJumperProjectSettings.getInstance(it) }
        val workspacePath = projectSettings?.vsCodeWorkspacePath

        // 如果配置了工作空间文件且文件存在，使用工作空间文件
        if (!workspacePath.isNullOrBlank() && File(workspacePath).exists()) {
            return super.getFastOpenCommand(workspacePath, filePath, lineNumber, columnNumber)
        } else {
            return super.getFastOpenCommand(projectPath, filePath, lineNumber, columnNumber)
        }
    }
}