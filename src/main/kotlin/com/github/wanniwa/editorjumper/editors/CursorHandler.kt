package com.github.wanniwa.editorjumper.editors

import com.intellij.openapi.util.SystemInfo

class CursorHandler(customPath: String) : BaseEditorHandler(customPath) {
    override fun getName(): String = "Cursor"
    
    override fun getDefaultPath(): String {
        return when {
            SystemInfo.isMac -> "/Applications/Cursor.app/Contents/MacOS/Cursor"
            SystemInfo.isWindows -> "Cursor"
            else -> "Cursor" // 其他平台不支持
        }
    }

} 