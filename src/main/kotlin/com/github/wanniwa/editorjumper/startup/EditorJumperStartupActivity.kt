package com.github.wanniwa.editorjumper.startup

import com.github.wanniwa.editorjumper.settings.EditorJumperProjectSettings
import com.github.wanniwa.editorjumper.settings.EditorJumperSettings
import com.github.wanniwa.editorjumper.utils.I18nUtils
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity

class EditorJumperStartupActivity : ProjectActivity {
    override suspend fun execute(project: Project) {
        // 获取全局设置和项目设置
        val globalSettings = EditorJumperSettings.getInstance()
        val projectSettings = EditorJumperProjectSettings.getInstance(project)

        // 如果项目设置为空，使用全局设置
        if (projectSettings.projectEditorType.isBlank()) {
            projectSettings.projectEditorType = globalSettings.selectedEditorType
        }

        if (!globalSettings.hasShownStatusBarGuide) {
            ApplicationManager.getApplication().invokeLater {
                if (project.isDisposed) return@invokeLater
                val title = I18nUtils.message("guide.statusbar.title")
                val message = I18nUtils.message("guide.statusbar.message")
                NotificationGroupManager.getInstance()
                    .getNotificationGroup("EditorJumper Notifications")
                    .createNotification(title, message, NotificationType.INFORMATION)
                    .notify(project)
                globalSettings.hasShownStatusBarGuide = true
            }
        }
    }
}