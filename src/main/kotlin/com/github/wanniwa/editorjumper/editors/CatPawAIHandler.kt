package com.github.wanniwa.editorjumper.editors

import com.intellij.openapi.project.Project
import com.intellij.openapi.util.SystemInfo

class CatPawAIHandler(customPath: String, project: Project?) : BaseEditorHandler(customPath) {
    override fun getName(): String = "CatPawAI"

    override fun getDefaultPath(): String {
        return when {
            SystemInfo.isMac -> "TODO" // Mac implementation to be done
            SystemInfo.isWindows -> "CatPawAI"
            else -> "CatPawAI"
        }
    }

    override fun getMacOpenName(): String? {
        return null // TODO: Update when Mac support is implemented
    }
}
