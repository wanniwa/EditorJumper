package com.github.wanniwa.editorjumper.editors

import com.intellij.openapi.project.Project
import com.intellij.openapi.util.SystemInfo

class WindsurfHandler(customPath: String, project: Project?) : BaseVscodeEditorHandler(customPath, project) {
    override fun getName(): String = "Windsurf"

    override fun getDefaultPath(): String {
        return when {
            SystemInfo.isMac -> "/Applications/Windsurf.app/Contents/Resources/app/bin/windsurf"
            SystemInfo.isWindows -> "Windsurf"
            else -> "Windsurf" // 其他平台不支持
        }
    }

    override fun getMacOpenName(): String? {
        return "windsurf"
    }
} 