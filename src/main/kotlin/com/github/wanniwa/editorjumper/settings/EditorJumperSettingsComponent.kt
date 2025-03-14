package com.github.wanniwa.editorjumper.settings

import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.ui.components.JBLabel
import com.intellij.util.ui.FormBuilder
import javax.swing.JComponent
import javax.swing.JPanel

class EditorJumperSettingsComponent {
    private val myMainPanel: JPanel
    private val vsCodePathField = TextFieldWithBrowseButton()
    private val cursorPathField = TextFieldWithBrowseButton()
    private val traePathField = TextFieldWithBrowseButton()
    private val windsurfPathField = TextFieldWithBrowseButton()
    private val editorTypeComboBox = ComboBox(arrayOf("VSCode", "Cursor", "Trae", "Windsurf"))

    init {
        val descriptor = FileChooserDescriptor(true, false, false, false, false, false)
        
        vsCodePathField.addBrowseFolderListener("Select VSCode Executable", null, null, descriptor)
        cursorPathField.addBrowseFolderListener("Select Cursor Executable", null, null, descriptor)
        traePathField.addBrowseFolderListener("Select Trae Executable", null, null, descriptor)
        windsurfPathField.addBrowseFolderListener("Select Windsurf Executable", null, null, descriptor)

        val macHintLabel = JBLabel("<html><em>macOS: All paths are auto-detected, no manual configuration needed</em></html>")
        val windowsHintLabel = JBLabel("<html><em>Windows: Cursor is auto-detected, other editors need .exe file path</em></html>")
        val exampleLabel = JBLabel("<html><em>Example: C:\\Users\\username\\AppData\\Local\\Programs\\VSCode\\Code.exe</em></html>")

        myMainPanel = FormBuilder.createFormBuilder()
                .addLabeledComponent(JBLabel("Default editor:"), editorTypeComboBox, 1, false)
                .addSeparator()
                .addComponent(macHintLabel)
                .addComponent(windowsHintLabel)
                .addComponent(exampleLabel)
                .addSeparator()
                .addLabeledComponent(JBLabel("VSCode path:"), vsCodePathField, 1, false)
                .addLabeledComponent(JBLabel("Cursor path:"), cursorPathField, 1, false)
                .addLabeledComponent(JBLabel("Trae path:"), traePathField, 1, false)
                .addLabeledComponent(JBLabel("Windsurf path:"), windsurfPathField, 1, false)
                .addComponentFillVertically(JPanel(), 0)
                .panel
    }

    fun getPanel(): JPanel {
        return myMainPanel
    }

    fun getPreferredFocusedComponent(): JComponent {
        return editorTypeComboBox
    }

    fun getVSCodePath(): String {
        return vsCodePathField.text
    }

    fun setVSCodePath(path: String) {
        vsCodePathField.text = path
    }

    fun getCursorPath(): String {
        return cursorPathField.text
    }

    fun setCursorPath(path: String) {
        cursorPathField.text = path
    }

    fun getTraePath(): String {
        return traePathField.text
    }

    fun setTraePath(path: String) {
        traePathField.text = path
    }
    
    fun getWindsurfPath(): String {
        return windsurfPathField.text
    }

    fun setWindsurfPath(path: String) {
        windsurfPathField.text = path
    }
    
    fun getSelectedEditorType(): String {
        return editorTypeComboBox.selectedItem as String
    }
    
    fun setSelectedEditorType(editorType: String) {
        editorTypeComboBox.selectedItem = editorType
    }
} 