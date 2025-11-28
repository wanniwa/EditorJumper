package com.github.wanniwa.editorjumper.editors

import com.intellij.openapi.project.Project
import com.intellij.openapi.util.SystemInfo

class QoderHandler(customPath: String, project: Project?) : BaseVscodeEditorHandler(customPath, project) {
    override fun getName(): String = "Qoder"

    override fun getDefaultPath(): String {
        return when {
            SystemInfo.isMac -> "/Applications/Qoder.app/Contents/Resources/app/bin/code"
            SystemInfo.isWindows -> "qoder"
            else -> "qoder" // Linux 和其他平台
        }
    }

    override fun getMacOpenName(): String? {
        return "qoder"
    }
}