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
        return when {
            filePath != null && lineNumber != null && columnNumber != null -> {
                // 如果有文件路径和光标位置，则打开项目并定位到文件的具体行列
                val fileWithPosition = "$filePath:$lineNumber:$columnNumber"
                if (SystemInfo.isWindows && customPath.isNullOrEmpty()) {
                    arrayOf("cmd", "/c", getPath(), projectPath, "--goto", fileWithPosition)
                } else {
                    arrayOf(getPath(), projectPath, "--goto", fileWithPosition)
                }
            }

            filePath != null -> {
                if (SystemInfo.isWindows && customPath.isNullOrEmpty()) {
                    arrayOf("cmd", "/c", getPath(), projectPath, "--goto", filePath)
                } else {
                    arrayOf(getPath(), projectPath, "--goto", filePath)
                }
            }

            else -> {

                if (SystemInfo.isWindows && customPath.isNullOrEmpty()) {
                    arrayOf("cmd", "/c", getPath(), projectPath)
                } else {
                    arrayOf(getPath(), projectPath)
                }
            }
        }
    }
}

abstract class BaseVscodeEditorHandler(customPath: String, private val project: Project?) :
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
}