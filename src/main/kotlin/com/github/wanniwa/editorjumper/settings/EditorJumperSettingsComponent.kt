package com.github.wanniwa.editorjumper.settings

import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.ui.TextBrowseFolderListener
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.components.JBLabel
import com.intellij.util.ui.FormBuilder
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.DefaultComboBoxModel

class EditorJumperSettingsComponent {
    private val myMainPanel: JPanel
    private val editorTypeComboBox = ComboBox<String>()
    private val vsCodePathField = TextFieldWithBrowseButton()
    private val cursorPathField = TextFieldWithBrowseButton()
    private val traePathField = TextFieldWithBrowseButton()
    private val windsurfPathField = TextFieldWithBrowseButton()

    init {
        // 为每个编辑器创建单独的描述符
        val vsCodeDescriptor = FileChooserDescriptorFactory.createSingleFileDescriptor()
        vsCodeDescriptor.title = "Select VSCode Executable"
        
        val cursorDescriptor = FileChooserDescriptorFactory.createSingleFileDescriptor()
        cursorDescriptor.title = "Select Cursor Executable"
        
        val traeDescriptor = FileChooserDescriptorFactory.createSingleFileDescriptor()
        traeDescriptor.title = "Select Trae Executable"
        
        val windsurfDescriptor = FileChooserDescriptorFactory.createSingleFileDescriptor()
        windsurfDescriptor.title = "Select Windsurf Executable"
        
        vsCodePathField.addBrowseFolderListener(TextBrowseFolderListener(vsCodeDescriptor))
        cursorPathField.addBrowseFolderListener(TextBrowseFolderListener(cursorDescriptor))
        traePathField.addBrowseFolderListener(TextBrowseFolderListener(traeDescriptor))
        windsurfPathField.addBrowseFolderListener(TextBrowseFolderListener(windsurfDescriptor))

        // 添加编辑器类型选项
        val editorTypes = arrayOf("VSCode", "Cursor", "Trae", "Windsurf")
        editorTypeComboBox.model = DefaultComboBoxModel(editorTypes)

        val macHintLabel = JBLabel("<html><em>macOS: All paths are auto-detected, no manual configuration needed</em></html>")
        val windowsHintLabel = JBLabel("<html><em>Windows: Cursor is auto-detected, other editors need .exe file path</em></html>")
        val exampleLabel = JBLabel("<html><em>Example: C:\\Users\\username\\AppData\\Local\\Programs\\VSCode\\Code.exe</em></html>")
        val defaultEditorHintLabel = JBLabel("<html><em>Default Editor: The default target editor for newly opened projects</em></html>")

        myMainPanel = FormBuilder.createFormBuilder()
                .addComponent(macHintLabel)
                .addComponent(windowsHintLabel)
                .addComponent(exampleLabel)
                .addSeparator()
                .addComponent(defaultEditorHintLabel)
                .addLabeledComponent(JBLabel("Default Editor:"), editorTypeComboBox, 1, false)
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