package com.github.wanniwa.editorjumper.editors

import com.github.wanniwa.editorjumper.settings.EditorJumperProjectSettings
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.SystemInfo
import java.io.File

class CursorHandler(customPath: String, project: Project?) : BaseVscodeEditorHandler(customPath, project) {
    override fun getName(): String = "Cursor"

    override fun getDefaultPath(): String {
        return when {
            SystemInfo.isMac -> "/Applications/Cursor.app/Contents/Resources/app/bin/code"
            SystemInfo.isWindows -> "Cursor"
            else -> "Cursor" // 其他平台不支持
        }
    }

    override fun getMacOpenName(): String? {
        return "cursor"
    }

} 