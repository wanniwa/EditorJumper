package com.github.wanniwa.editorjumper.editors

import com.github.wanniwa.editorjumper.settings.EditorJumperSettings
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.SystemInfo

class EditorHandlerFactory {
    companion object {
        fun getHandler(editorType: String, customPath: String, project: Project?): EditorHandler {
            return when (editorType) {
                "Visual Studio Code" -> VSCodeHandler(customPath, project)
                "Cursor" -> CursorHandler(customPath, project)
                "Trae" -> {
                    val settings = EditorJumperSettings.getInstance()
                    // 只在 Mac 平台上检查 Trae CN 设置
                    if (SystemInfo.isMac && settings.traeCN) {
                        TraeCNHandler(customPath, project)
                    } else {
                        TraeHandler(customPath, project)
                    }
                }
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
                "Visual Studio Code" -> settings.vsCodePath
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