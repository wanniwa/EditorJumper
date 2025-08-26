package com.github.wanniwa.editorjumper.statusbar

import com.github.wanniwa.editorjumper.settings.EditorJumperProjectSettings
import com.github.wanniwa.editorjumper.settings.EditorJumperSettings
import com.github.wanniwa.editorjumper.settings.EditorJumperSettingsConfigurable
import com.intellij.openapi.Disposable
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.openapi.ui.popup.ListPopup
import com.intellij.openapi.ui.popup.PopupStep
import com.intellij.openapi.ui.popup.util.BaseListPopupStep
import com.intellij.openapi.wm.StatusBar
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.ui.awt.RelativePoint
import com.intellij.util.Consumer
import java.awt.Point
import java.awt.event.MouseEvent

class EditorJumperStatusBarWidget(private val project: Project) : StatusBarWidget, 
                                                                  StatusBarWidget.MultipleTextValuesPresentation,
                                                                  Disposable {
    companion object {
        const val ID = "EditorJumperWidget"
    }

    private var statusBar: StatusBar? = null
    private val supportedEditors = arrayOf("VSCode", "Cursor", "Trae", "Windsurf", "Void", "Kiro", "Qoder", "Settings")

    override fun ID(): String = ID

    override fun getTooltipText(): String = "Click to switch target editor"

    override fun getSelectedValue(): String {
        val projectSettings = EditorJumperProjectSettings.getInstance(project)
        val editorType = if (projectSettings.projectEditorType.isBlank()) {
            EditorJumperSettings.getInstance().selectedEditorType
        } else {
            projectSettings.projectEditorType
        }
        return "Jump to: $editorType"
    }

    override fun getPresentation(): StatusBarWidget.WidgetPresentation {
        return this
    }

    override fun getPopup(): ListPopup {
        val factory = JBPopupFactory.getInstance()
        var selectedValue: String? = null
        
        val step = object : BaseListPopupStep<String>("Select Target Editor", supportedEditors.toList()) {
            override fun onChosen(value: String, finalChoice: Boolean): PopupStep<*>? {
                selectedValue = value
                if (value == "Settings") {
                    return PopupStep.FINAL_CHOICE
                } else {
                    // 只更新项目级设置
                    val projectSettings = EditorJumperProjectSettings.getInstance(project)
                    projectSettings.projectEditorType = value
                    // 更新状态栏显示
                    statusBar?.updateWidget(ID())
                }
                return PopupStep.FINAL_CHOICE
            }

            override fun getFinalRunnable(): Runnable? {
                return if (selectedValue == "Settings") {
                    Runnable {
                        ShowSettingsUtil.getInstance().showSettingsDialog(
                            project,
                            EditorJumperSettingsConfigurable::class.java
                        )
                    }
                } else null
            }
        }
        
        return factory.createListPopup(step)
    }

    override fun getClickConsumer(): Consumer<MouseEvent> = Consumer { event ->
        val popup = getPopup()
        val component = event.component
        
        // 将弹出菜单显示在组件的正上方，并与组件左对齐
        // 使用固定的偏移量，确保菜单贴合状态栏
        val point = Point(0, 0)
        
        popup.show(RelativePoint(component, point))
    }

    override fun install(statusBar: StatusBar) {
        this.statusBar = statusBar
    }

    override fun dispose() {
        statusBar = null
    }
} 