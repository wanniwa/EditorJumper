package com.github.wanniwa.editorjumper.utils

import com.github.wanniwa.editorjumper.settings.EditorJumperProjectSettings
import com.github.wanniwa.editorjumper.settings.EditorJumperSettings
import com.intellij.openapi.project.Project

object EditorTargetUtils {
    /**
     * 获取目标编辑器类型，优先使用项目级设置，如果项目级设置为空则使用全局设置
     */
    fun getTargetEditor(project: Project?): String {
        val projectSettings = project?.let { EditorJumperProjectSettings.getInstance(it) }
        return if (projectSettings?.projectEditorType.isNullOrBlank()) {
            EditorJumperSettings.getInstance().selectedEditorType
        } else {
            projectSettings.projectEditorType
        }
    }
} 