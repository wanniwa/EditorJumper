package com.github.wanniwa.editorjumper.settings

import com.intellij.openapi.options.Configurable
import com.intellij.openapi.options.Configurable.WithEpDependencies
import com.intellij.openapi.extensions.BaseExtensionPointName
import com.intellij.openapi.project.ProjectManager
import com.github.wanniwa.editorjumper.utils.I18nUtils
import javax.swing.JComponent
import java.util.ArrayList

class EditorJumperSettingsConfigurable : Configurable, WithEpDependencies {
    private var mySettingsComponent: EditorJumperSettingsComponent? = null

    override fun getDisplayName(): String {
        return I18nUtils.message("settings.displayName")
    }

    override fun getPreferredFocusedComponent(): JComponent {
        return mySettingsComponent!!.getPreferredFocusedComponent()
    }

    override fun createComponent(): JComponent {
        mySettingsComponent = EditorJumperSettingsComponent()
        return mySettingsComponent!!.getPanel()
    }

    override fun isModified(): Boolean {
        val settings = EditorJumperSettings.getInstance()
        return mySettingsComponent!!.getVSCodePath() != settings.vsCodePath ||
               mySettingsComponent!!.getCursorPath() != settings.cursorPath ||
               mySettingsComponent!!.getTraePath() != settings.traePath ||
               mySettingsComponent!!.getWindsurfPath() != settings.windsurfPath ||
               mySettingsComponent!!.getVoidPath() != settings.voidPath ||
               mySettingsComponent!!.getKiroPath() != settings.kiroPath ||
               mySettingsComponent!!.getQoderPath() != settings.qoderPath ||
               mySettingsComponent!!.getSelectedEditorType() != settings.selectedEditorType
    }

    override fun apply() {
        val settings = EditorJumperSettings.getInstance()
        val oldEditorType = settings.selectedEditorType
        val newEditorType = mySettingsComponent!!.getSelectedEditorType()
        
        settings.vsCodePath = mySettingsComponent!!.getVSCodePath()
        settings.cursorPath = mySettingsComponent!!.getCursorPath()
        settings.traePath = mySettingsComponent!!.getTraePath()
        settings.windsurfPath = mySettingsComponent!!.getWindsurfPath()
        settings.voidPath = mySettingsComponent!!.getVoidPath()
        settings.kiroPath = mySettingsComponent!!.getKiroPath()
        settings.qoderPath = mySettingsComponent!!.getQoderPath()
        settings.selectedEditorType = newEditorType
        
        // 如果默认编辑器类型改变了，询问是否同时更新当前项目的编辑器类型设置
        if (oldEditorType != newEditorType) {
            askToUpdateCurrentProject(newEditorType)
        }
    }
    
        /**
     * 询问是否更新当前项目的编辑器类型设置
     */
    private fun askToUpdateCurrentProject(newEditorType: String) {
        // 获取当前打开的项目（非默认项目）
        val openProjects = com.intellij.openapi.project.ProjectManager.getInstance().openProjects
        val currentProject = openProjects.firstOrNull { !it.isDefault }
        
        currentProject?.let { project ->
            val result = com.intellij.openapi.ui.Messages.showYesNoDialog(
                project,
                I18nUtils.message("dialog.updateCurrentProject.message", newEditorType, newEditorType),
                I18nUtils.message("dialog.updateCurrentProject.title"),
                I18nUtils.message("dialog.updateCurrentProject.yes"),
                I18nUtils.message("dialog.updateCurrentProject.no"),
                com.intellij.openapi.ui.Messages.getQuestionIcon()
            )
            
            if (result == com.intellij.openapi.ui.Messages.YES) {
                val projectSettings = EditorJumperProjectSettings.getInstance(project)
                projectSettings.projectEditorType = newEditorType
                
                // 更新状态栏显示
                com.intellij.openapi.wm.WindowManager.getInstance().getStatusBar(project)?.updateWidget("EditorJumperWidget")
            }
        }
    }

    override fun reset() {
        val settings = EditorJumperSettings.getInstance()
        mySettingsComponent!!.setVSCodePath(settings.vsCodePath)
        mySettingsComponent!!.setCursorPath(settings.cursorPath)
        mySettingsComponent!!.setTraePath(settings.traePath)
        mySettingsComponent!!.setWindsurfPath(settings.windsurfPath)
        mySettingsComponent!!.setVoidPath(settings.voidPath)
        mySettingsComponent!!.setKiroPath(settings.kiroPath)
        mySettingsComponent!!.setQoderPath(settings.qoderPath)
        mySettingsComponent!!.setSelectedEditorType(settings.selectedEditorType)
    }

    override fun disposeUIResources() {
        mySettingsComponent = null
    }

    override fun getDependencies(): Collection<BaseExtensionPointName<*>> {
        return ArrayList()
    }
} 