package com.github.wanniwa.editorjumper.editors

import com.github.wanniwa.editorjumper.settings.EditorJumperSettings
import com.intellij.openapi.project.Project

class EditorHandlerFactory {
    companion object {
        fun getHandler(editorType: String, customPath: String, project: Project?): EditorHandler {
            return when (editorType) {
                "VSCode" -> VSCodeHandler(customPath, project)
                "Cursor" -> CursorHandler(customPath, project)
                "Trae" -> TraeHandler(customPath, project)
                "Windsurf" -> WindsurfHandler(customPath, project)
                "Void" -> VoidHandler(customPath, project)
                "Kiro" -> KiroHandler(customPath, project)
                "Qoder" -> QoderHandler(customPath, project)
                else -> CursorHandler(customPath, project)
            }
        }

        /**
         * 从设置中获取自定义路径并创建对应的编辑器处理器
         */
        fun getHandler(editorType: String, project: Project?): EditorHandler {
            val settings = EditorJumperSettings.getInstance()
            val customPath = when (editorType) {
                "VSCode" -> settings.vsCodePath
                "Cursor" -> settings.cursorPath
                "Trae" -> settings.traePath
                "Windsurf" -> settings.windsurfPath
                "Void" -> settings.voidPath
                "Kiro" -> settings.kiroPath
                "Qoder" -> settings.qoderPath
                else -> ""
            }
            return getHandler(editorType, customPath, project)
        }
    }
} 