package com.github.wanniwa.editorjumper.actions

import com.github.wanniwa.editorjumper.settings.EditorJumperSettingsConfigurable
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.options.ShowSettingsUtil

class EditorJumperSettingsAction : AnAction() {
    override fun getActionUpdateThread() = ActionUpdateThread.BGT


    override fun actionPerformed(e: AnActionEvent) {
        ShowSettingsUtil.getInstance().showSettingsDialog(
                e.project,
                EditorJumperSettingsConfigurable::class.java
        )
    }
} 