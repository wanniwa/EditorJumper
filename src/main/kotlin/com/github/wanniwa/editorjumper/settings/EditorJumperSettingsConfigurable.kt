package com.github.wanniwa.editorjumper.settings

import com.github.wanniwa.editorjumper.editors.EditorRegistry
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.options.Configurable.WithEpDependencies
import com.intellij.openapi.extensions.BaseExtensionPointName
import com.intellij.openapi.wm.IdeFocusManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.github.wanniwa.editorjumper.utils.I18nUtils
import javax.swing.JComponent
import java.util.ArrayList

class EditorJumperSettingsConfigurable : Configurable, WithEpDependencies {
    private var mySettingsComponent: EditorJumperSettingsComponent? = null

    override fun getDisplayName(): String = I18nUtils.message("settings.displayName")

    override fun getPreferredFocusedComponent(): JComponent =
        mySettingsComponent!!.getPreferredFocusedComponent()

    override fun createComponent(): JComponent {
        mySettingsComponent = EditorJumperSettingsComponent()
        return mySettingsComponent!!.getPanel()
    }

    override fun isModified(): Boolean {
        val settings = EditorJumperSettings.getInstance()
        val component = mySettingsComponent!!
        if (component.getDefaultEditorType() != settings.selectedEditorType) {
            return true
        }
        val currentEditorType = getCurrentEditorType(settings, getCurrentProject())
        if (component.getSelectedEditorType() != currentEditorType) {
            return true
        }

        // 路径是否修改
        if (EditorRegistry.editors.any { editor ->
                component.getPath(editor.name) != settings.getPath(editor.name)
            }) {
            return true
        }

        // 可见性是否修改（hiddenEditors 为空表示“全部可见”）
        val hiddenSet = settings.hiddenEditors
        if (EditorRegistry.editors.any { editor ->
                val name = editor.name
            val modelVisible = hiddenSet.isEmpty() || !hiddenSet.contains(name)
                val uiVisible = component.isEditorVisible(name)
                modelVisible != uiVisible
            }) {
            return true
        }

        return false
    }

    override fun apply() {
        val settings = EditorJumperSettings.getInstance()
        val component = mySettingsComponent!!

        EditorRegistry.editors.forEach { editor ->
            settings.setPath(editor.name, component.getPath(editor.name))
        }

        // 更新隐藏编辑器集合：全选时存储为空（表示全部可见），否则存储未选中的子集
        val allNames = EditorRegistry.editorNames
        val selectedVisible = allNames.filter { component.isEditorVisible(it) }.toMutableSet()
        val hidden = allNames.filterNot { selectedVisible.contains(it) }.toMutableSet()
        settings.hiddenEditors = LinkedHashSet(hidden)
        settings.selectedEditorType = component.getDefaultEditorType()
        val newEditorType = component.getSelectedEditorType()

        val currentProject = getCurrentProject()
        if (currentProject != null) {
            val projectSettings = EditorJumperProjectSettings.getInstance(currentProject)
            projectSettings.projectEditorType = newEditorType
            com.intellij.openapi.wm.WindowManager.getInstance()
                .getStatusBar(currentProject)?.updateWidget("EditorJumperWidget")
        }
    }

    override fun reset() {
        val settings = EditorJumperSettings.getInstance()
        val component = mySettingsComponent!!
        EditorRegistry.editors.forEach { editor ->
            component.setPath(editor.name, settings.getPath(editor.name))
        }

        // 当 hiddenEditors 为空时，表示全部可见
        val hiddenSet = settings.hiddenEditors
        EditorRegistry.editors.forEach { editor ->
            val name = editor.name
            val visible = hiddenSet.isEmpty() || !hiddenSet.contains(name)
            component.setEditorVisible(name, visible)
        }

        val currentEditorType = getCurrentEditorType(settings, getCurrentProject())
        component.setDefaultEditorType(settings.selectedEditorType)
        component.setSelectedEditorType(currentEditorType)
    }

    private fun getCurrentProject(): Project? =
        IdeFocusManager.getGlobalInstance().lastFocusedFrame?.project
            ?: ProjectManager.getInstance().openProjects.firstOrNull { !it.isDefault }

    private fun getCurrentEditorType(settings: EditorJumperSettings, project: Project?): String {
        if (project == null) return settings.selectedEditorType
        val projectSettings = EditorJumperProjectSettings.getInstance(project)
        return if (projectSettings.projectEditorType.isBlank()) {
            settings.selectedEditorType
        } else {
            projectSettings.projectEditorType
        }
    }

    override fun disposeUIResources() {
        mySettingsComponent = null
    }

    override fun getDependencies(): Collection<BaseExtensionPointName<*>> = ArrayList()
}
