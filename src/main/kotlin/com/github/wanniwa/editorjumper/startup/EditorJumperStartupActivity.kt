package com.github.wanniwa.editorjumper.startup

import com.github.wanniwa.editorjumper.settings.EditorJumperProjectSettings
import com.github.wanniwa.editorjumper.settings.EditorJumperSettings
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import com.intellij.openapi.wm.WindowManager
import com.github.wanniwa.editorjumper.statusbar.EditorJumperStatusBarWidget

class EditorJumperStartupActivity : ProjectActivity {
    override suspend fun execute(project: Project) {
        // 获取全局设置和项目设置
        val globalSettings = EditorJumperSettings.getInstance()
        val projectSettings = EditorJumperProjectSettings.getInstance(project)

        // 如果项目设置为空，使用全局设置
        if (projectSettings.projectEditorType.isBlank()) {
            projectSettings.projectEditorType = globalSettings.selectedEditorType
        }

        // 更新状态栏显示
        val statusBar = WindowManager.getInstance().getStatusBar(project)
        statusBar?.updateWidget(EditorJumperStatusBarWidget.ID)
    }
} 