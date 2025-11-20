package com.github.wanniwa.editorjumper.editors

import com.intellij.openapi.project.Project
import com.intellij.openapi.util.SystemInfo

class AntigravityHandler(customPath: String?, private val project: Project?) : BaseEditorHandler(customPath) {
    override fun getName(): String = "Antigravity"

    override fun getDefaultPath(): String {
        return when {
            SystemInfo.isMac -> "TODO" // TODO: MAC 实现
            SystemInfo.isWindows -> "antigravity"
            else -> "antigravity"
        }
    }

    override fun getMacOpenName(): String? {
        return null // TODO: MAC实现
    }
}
