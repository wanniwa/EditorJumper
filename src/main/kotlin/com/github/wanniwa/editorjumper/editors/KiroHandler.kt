package com.github.wanniwa.editorjumper.editors

import com.intellij.openapi.project.Project
import com.intellij.openapi.util.SystemInfo

class KiroHandler(customPath: String?, private val project: Project?) : BaseEditorHandler(customPath) {
    override fun getName(): String = "Kiro"

    override fun getDefaultPath(): String {
        return when {
            SystemInfo.isMac -> "/Applications/Kiro.app/Contents/Resources/app/bin/code"
            SystemInfo.isWindows -> "kiro"
            else -> "kiro" // Linux 等其他平台
        }
    }

    override fun getOpenCommand(
        projectPath: String,
        filePath: String?,
        lineNumber: Int?,
        columnNumber: Int?
    ): Array<String> {
        return when {
            filePath != null && lineNumber != null && columnNumber != null -> {
                // Kiro 支持 --goto 参数来定位到具体行列
                val fileWithPosition = "$filePath:$lineNumber:$columnNumber"
                if (SystemInfo.isWindows && getPath() == getDefaultPath()) {
                    arrayOf("cmd", "/c", getPath(), projectPath, "--goto", fileWithPosition)
                } else {
                    arrayOf(getPath(), projectPath, "--goto", fileWithPosition)
                }
            }
            filePath != null -> {
                // 只有文件路径，没有行列信息
                if (SystemInfo.isWindows && getPath() == getDefaultPath()) {
                    arrayOf("cmd", "/c", getPath(), projectPath, "--goto", filePath)
                } else {
                    arrayOf(getPath(), projectPath, "--goto", filePath)
                }
            }
            else -> {
                // 只打开项目
                if (SystemInfo.isWindows && getPath() == getDefaultPath()) {
                    arrayOf("cmd", "/c", getPath(), projectPath)
                } else {
                    arrayOf(getPath(), projectPath)
                }
            }
        }
    }
}