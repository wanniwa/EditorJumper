package com.github.wanniwa.editorjumper.editors

import com.github.wanniwa.editorjumper.settings.EditorJumperProjectSettings
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.SystemInfo
import java.io.File

class QoderHandler(customPath: String, private val project: Project?) : BaseVscodeEditorHandler(customPath, project) {
    override fun getName(): String = "Qoder"

    override fun getDefaultPath(): String {
        return when {
            SystemInfo.isMac -> "/Applications/Qoder.app/Contents/Resources/app/bin/code"
            SystemInfo.isWindows -> "qoder"
            else -> "qoder" // Linux 和其他平台
        }
    }

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