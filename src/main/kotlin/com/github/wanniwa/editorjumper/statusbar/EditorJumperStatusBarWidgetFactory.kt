package com.github.wanniwa.editorjumper.statusbar

import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.wm.StatusBar
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.openapi.wm.StatusBarWidgetFactory

class EditorJumperStatusBarWidgetFactory : StatusBarWidgetFactory, Disposable {
    override fun getId(): String = "EditorJumperWidget"

    override fun getDisplayName(): String = "Editor Jumper"

    override fun isAvailable(project: Project): Boolean = true

    override fun createWidget(project: Project): StatusBarWidget {
        return EditorJumperStatusBarWidget(project)
    }

    override fun disposeWidget(widget: StatusBarWidget) {
        Disposer.dispose(widget)
    }

    override fun canBeEnabledOn(statusBar: StatusBar): Boolean = true

    override fun dispose() {
        // 清理工厂类资源
    }
} 