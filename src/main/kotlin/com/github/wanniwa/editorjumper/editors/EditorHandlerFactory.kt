package com.github.wanniwa.editorjumper.editors

import com.github.wanniwa.editorjumper.settings.EditorJumperSettings
import com.intellij.openapi.project.Project

class EditorHandlerFactory {
    companion object {
        fun getHandler(editorType: String, customPath: String, project: Project?): EditorHandler {
            val registration = EditorRegistry.find(editorType)
                ?: EditorRegistry.editors.firstOrNull()
                ?: error("editors.json is empty or missing")
            return registration.create(customPath, project)
        }

        fun getHandler(editorType: String, project: Project?): EditorHandler {
            val settings = EditorJumperSettings.getInstance()
            return getHandler(editorType, settings.getPath(editorType), project)
        }
    }
}
