package com.github.wanniwa.editorjumper.editors

import com.intellij.openapi.project.Project
import com.intellij.openapi.util.SystemInfo

class VscodeHandler(customPath: String, project: Project?) : BaseVscodeEditorHandler(customPath, project) {
    override fun getName(): String = "Visual Studio Code"

    override fun getDefaultPath(): String {
        return when {
            SystemInfo.isMac -> "/Applications/Visual Studio Code.app/Contents/Resources/app/bin/code"
            SystemInfo.isWindows -> "Code"
            else -> "Code" // 其他平台不支持
        }
    }

    override fun getMacOpenName(): String {
        return "vscode"
    }
} 