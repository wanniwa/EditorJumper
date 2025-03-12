package com.github.wanniwa.editorjumper.editors

import com.intellij.openapi.util.SystemInfo

class TraeHandler(customPath: String) : BaseEditorHandler(customPath) {
    override fun getName(): String = "Trae"
    
    override fun getDefaultPath(): String {
        return when {
            SystemInfo.isMac -> "/Applications/Trae.app/Contents/MacOS/Electron"
            SystemInfo.isWindows -> "Trae"
            else -> "" // 其他平台不支持
        }
    }
} 