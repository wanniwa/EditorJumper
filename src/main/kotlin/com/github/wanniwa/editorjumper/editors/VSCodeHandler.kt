package com.github.wanniwa.editorjumper.editors

import com.intellij.openapi.util.SystemInfo

class VSCodeHandler(customPath: String) : BaseEditorHandler(customPath) {
    override fun getName(): String = "VSCode"
    
    override fun getDefaultPath(): String {
        return when {
            SystemInfo.isMac -> "/Applications/Visual Studio Code.app/Contents/Resources/app/bin/code"
            SystemInfo.isWindows -> "Code"
            else -> "Code" // 其他平台不支持
        }
    }
} 