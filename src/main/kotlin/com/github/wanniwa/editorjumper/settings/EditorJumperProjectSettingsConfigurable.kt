package com.github.wanniwa.editorjumper.settings

import com.intellij.openapi.extensions.BaseExtensionPointName
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.options.Configurable.WithEpDependencies
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.TextBrowseFolderListener
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.ui.components.JBLabel
import com.intellij.util.ui.FormBuilder
import com.github.wanniwa.editorjumper.utils.I18nUtils
import javax.swing.JComponent
import javax.swing.JPanel

class EditorJumperProjectSettingsConfigurable(private val project: Project) : Configurable, WithEpDependencies {
    private var mySettingsComponent: EditorJumperProjectSettingsComponent? = null

    override fun getDisplayName(): String {
        return I18nUtils.message("settings.projectSettings.displayName")
    }

    override fun getPreferredFocusedComponent(): JComponent {
        return mySettingsComponent!!.getPreferredFocusedComponent()
    }

    override fun createComponent(): JComponent {
        mySettingsComponent = EditorJumperProjectSettingsComponent()
        return mySettingsComponent!!.getPanel()
    }

    override fun isModified(): Boolean {
        val settings = EditorJumperProjectSettings.getInstance(project)
        return mySettingsComponent!!.getVSCodeWorkspacePath() != settings.vsCodeWorkspacePath
    }

    override fun apply() {
        val settings = EditorJumperProjectSettings.getInstance(project)
        settings.vsCodeWorkspacePath = mySettingsComponent!!.getVSCodeWorkspacePath()
    }

    override fun reset() {
        val settings = EditorJumperProjectSettings.getInstance(project)
        mySettingsComponent!!.setVSCodeWorkspacePath(settings.vsCodeWorkspacePath)
    }

    override fun disposeUIResources() {
        mySettingsComponent = null
    }

    override fun getDependencies(): Collection<BaseExtensionPointName<*>> {
        return ArrayList()
    }
}

class EditorJumperProjectSettingsComponent {
    private val myMainPanel: JPanel
    private val vsCodeWorkspacePathField = TextFieldWithBrowseButton()

    init {
        val workspaceDescriptor = FileChooserDescriptor(true, false, false, false, false, false)
        workspaceDescriptor.title = "Select Workspace File"
        workspaceDescriptor.withFileFilter { file -> file.name.endsWith(".code-workspace") }
        
        vsCodeWorkspacePathField.addBrowseFolderListener(TextBrowseFolderListener(workspaceDescriptor))

        myMainPanel = FormBuilder.createFormBuilder()
                .addLabeledComponent(JBLabel(I18nUtils.message("settings.projectSettings.workspacePath")), vsCodeWorkspacePathField, 1, false)
                .addComponentFillVertically(JPanel(), 0)
                .panel
    }

    fun getPanel(): JPanel {
        return myMainPanel
    }

    fun getPreferredFocusedComponent(): JComponent {
        return vsCodeWorkspacePathField
    }

    fun getVSCodeWorkspacePath(): String {
        return vsCodeWorkspacePathField.text
    }

    fun setVSCodeWorkspacePath(path: String) {
        vsCodeWorkspacePathField.text = path
    }
} 