package com.github.wanniwa.editorjumper.settings

import com.intellij.openapi.options.Configurable
import com.intellij.openapi.options.Configurable.WithEpDependencies
import com.intellij.openapi.extensions.BaseExtensionPointName
import javax.swing.JComponent
import java.util.ArrayList

class EditorJumperSettingsConfigurable : Configurable, WithEpDependencies {
    private var mySettingsComponent: EditorJumperSettingsComponent? = null

    override fun getDisplayName(): String {
        return "EditorJumper Settings"
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
               mySettingsComponent!!.getSelectedEditorType() != settings.selectedEditorType
    }

    override fun apply() {
        val settings = EditorJumperSettings.getInstance()
        settings.vsCodePath = mySettingsComponent!!.getVSCodePath()
        settings.cursorPath = mySettingsComponent!!.getCursorPath()
        settings.traePath = mySettingsComponent!!.getTraePath()
        settings.windsurfPath = mySettingsComponent!!.getWindsurfPath()
        settings.voidPath = mySettingsComponent!!.getVoidPath()
        settings.kiroPath = mySettingsComponent!!.getKiroPath()
        settings.selectedEditorType = mySettingsComponent!!.getSelectedEditorType()
    }

    override fun reset() {
        val settings = EditorJumperSettings.getInstance()
        mySettingsComponent!!.setVSCodePath(settings.vsCodePath)
        mySettingsComponent!!.setCursorPath(settings.cursorPath)
        mySettingsComponent!!.setTraePath(settings.traePath)
        mySettingsComponent!!.setWindsurfPath(settings.windsurfPath)
        mySettingsComponent!!.setVoidPath(settings.voidPath)
        mySettingsComponent!!.setKiroPath(settings.kiroPath)
        mySettingsComponent!!.setSelectedEditorType(settings.selectedEditorType)
    }

    override fun disposeUIResources() {
        mySettingsComponent = null
    }

    override fun getDependencies(): Collection<BaseExtensionPointName<*>> {
        return ArrayList()
    }
} 