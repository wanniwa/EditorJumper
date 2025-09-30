package com.github.wanniwa.editorjumper.editors

import com.intellij.openapi.project.Project
import com.intellij.openapi.util.SystemInfo

class VoidHandler(customPath: String, project: Project?) : BaseVscodeEditorHandler(customPath,project) {
    override fun getName(): String = "Void"

    override fun getDefaultPath(): String {
        return when {
            SystemInfo.isMac -> "/Applications/Void.app/Contents/Resources/app/bin/void"
            SystemInfo.isWindows -> "void"
            else -> "void" // 其他平台不支持
        }
    }

    override fun getMacOpenName(): String? {
        return "void"
    }
} 