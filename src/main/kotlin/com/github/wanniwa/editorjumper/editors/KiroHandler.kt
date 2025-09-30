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

    override fun getMacOpenName(): String? {
        return "kiro"
    }
}