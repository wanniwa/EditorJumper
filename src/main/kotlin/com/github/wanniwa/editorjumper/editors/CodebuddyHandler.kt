package com.github.wanniwa.editorjumper.editors

import com.intellij.openapi.project.Project
import com.intellij.openapi.util.SystemInfo

class CodebuddyHandler(customPath: String, project: Project?) : BaseVscodeEditorHandler(customPath, project) {

    override fun getName(): String = "CodeBuddy"

    override fun getDefaultPath(): String {
        return when {
            SystemInfo.isMac -> "/Applications/CodeBuddy.app/Contents/Resources/app/bin/codebuddy"
            else -> ""
        }
    }

    override fun getMacOpenName(): String = "codebuddy"

}
