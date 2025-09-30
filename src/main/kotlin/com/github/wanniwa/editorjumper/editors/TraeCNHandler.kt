package com.github.wanniwa.editorjumper.editors

import com.intellij.openapi.project.Project
import com.intellij.openapi.util.SystemInfo

class TraeCNHandler(customPath: String, project: Project?) : BaseVscodeEditorHandler(customPath, project) {

    override fun getName(): String = "Trae CN"

    override fun getDefaultPath(): String {
        return when {
            SystemInfo.isMac -> "/Applications/Trae CN.app/Contents/MacOS/Electron"
            SystemInfo.isWindows -> "Trae"
            else -> "" // 其他平台不支持
        }
    }

    override fun getMacOpenName(): String {
        return "trae-cn"
    }

}
