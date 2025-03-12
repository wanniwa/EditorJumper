package com.github.wanniwa.editorjumper.editors

import com.intellij.openapi.util.SystemInfo

class WindsurfHandler(customPath: String) : BaseEditorHandler(customPath) {
    override fun getName(): String = "Windsurf"
    
    override fun getDefaultPath(): String {
        return when {
            SystemInfo.isMac -> "/Applications/Windsurf.app/Contents/MacOS/Electron"
            SystemInfo.isWindows -> "Windsurf"
            else -> "Windsurf" // 其他平台不支持
        }
    }
} 