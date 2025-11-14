package com.github.wanniwa.editorjumper.settings

import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.ui.TextBrowseFolderListener
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBCheckBox
import com.intellij.util.ui.FormBuilder
import com.github.wanniwa.editorjumper.utils.I18nUtils
import com.intellij.openapi.util.SystemInfo
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.DefaultComboBoxModel

class EditorJumperSettingsComponent {
    private val myMainPanel: JPanel
    private val editorTypeComboBox = ComboBox<String>()
    private val vsCodePathField = TextFieldWithBrowseButton()
    private val cursorPathField = TextFieldWithBrowseButton()
    private val traePathField = TextFieldWithBrowseButton()
    private val traeCNCheckBox = JBCheckBox()
    private val voidPathField = TextFieldWithBrowseButton()
    private val windsurfPathField = TextFieldWithBrowseButton()
    private val kiroPathField = TextFieldWithBrowseButton()
    private val qoderPathField = TextFieldWithBrowseButton()
    private val catPawAIPathField = TextFieldWithBrowseButton()

    init {
        // 为每个编辑器创建单独的描述符
        val vsCodeDescriptor = FileChooserDescriptor(true, false, false, false, false, false)
        vsCodeDescriptor.title = I18nUtils.getFileChooserTitle("Visual Studio Code")

        val cursorDescriptor = FileChooserDescriptor(true, false, false, false, false, false)
        cursorDescriptor.title = I18nUtils.getFileChooserTitle("Cursor")

        val traeDescriptor = FileChooserDescriptor(true, false, false, false, false, false)
        traeDescriptor.title = I18nUtils.getFileChooserTitle("Trae")

        val windsurfDescriptor = FileChooserDescriptor(true, false, false, false, false, false)
        windsurfDescriptor.title = I18nUtils.getFileChooserTitle("Windsurf")

        val voidDescriptor = FileChooserDescriptor(true, false, false, false, false, false)
        voidDescriptor.title = I18nUtils.getFileChooserTitle("Void")

        val kiroDescriptor = FileChooserDescriptor(true, false, false, false, false, false)
        kiroDescriptor.title = I18nUtils.getFileChooserTitle("Kiro")

        val qoderDescriptor = FileChooserDescriptor(true, false, false, false, false, false)
        qoderDescriptor.title = I18nUtils.getFileChooserTitle("Qoder")

        val catPawAIDescriptor = FileChooserDescriptor(true, false, false, false, false, false)
        catPawAIDescriptor.title = I18nUtils.getFileChooserTitle("catPawAI")

        vsCodePathField.addBrowseFolderListener(TextBrowseFolderListener(vsCodeDescriptor))
        cursorPathField.addBrowseFolderListener(TextBrowseFolderListener(cursorDescriptor))
        traePathField.addBrowseFolderListener(TextBrowseFolderListener(traeDescriptor))
        windsurfPathField.addBrowseFolderListener(TextBrowseFolderListener(windsurfDescriptor))
        voidPathField.addBrowseFolderListener(TextBrowseFolderListener(voidDescriptor))
        kiroPathField.addBrowseFolderListener(TextBrowseFolderListener(kiroDescriptor))
        qoderPathField.addBrowseFolderListener(TextBrowseFolderListener(qoderDescriptor))
        catPawAIPathField.addBrowseFolderListener(TextBrowseFolderListener(catPawAIDescriptor))

        // 添加编辑器类型选项
        val editorTypes = arrayOf("Visual Studio Code", "Cursor", "Trae", "Windsurf", "Void", "Kiro", "Qoder", "catPawAI")
        editorTypeComboBox.model = DefaultComboBoxModel(editorTypes)

        val macHintLabel = JBLabel("<html><em>${I18nUtils.message("settings.hint.macOS")}</em></html>")
        val windowsHintLabel = JBLabel("<html><em>${I18nUtils.message("settings.hint.windows")}</em></html>")
        val exampleLabel = JBLabel("<html><em>${I18nUtils.message("settings.hint.example")}</em></html>")
        val defaultEditorHintLabel = JBLabel("<html><em>${I18nUtils.message("settings.hint.defaultEditor")}</em></html>")

        val formBuilder = FormBuilder.createFormBuilder()
                .addComponent(macHintLabel)
                .addComponent(windowsHintLabel)
                .addComponent(exampleLabel)
                .addSeparator()
                .addComponent(defaultEditorHintLabel)
                .addLabeledComponent(JBLabel(I18nUtils.message("settings.defaultEditor.label")), editorTypeComboBox, 1, false)
                .addSeparator()
                .addLabeledComponent(JBLabel(I18nUtils.getPathLabel("Visual Studio Code")), vsCodePathField, 1, false)
                .addLabeledComponent(JBLabel(I18nUtils.getPathLabel("Cursor")), cursorPathField, 1, false)
                .addLabeledComponent(JBLabel(I18nUtils.getPathLabel("Trae")), traePathField, 1, false)

        // 只在 Mac 平台上显示 Trae CN 选项
        if (SystemInfo.isMac) {
            formBuilder.addLabeledComponent(JBLabel(I18nUtils.message("settings.traeCN.label")), traeCNCheckBox, 1, false)
        }

        myMainPanel = formBuilder
                .addLabeledComponent(JBLabel(I18nUtils.getPathLabel("Windsurf")), windsurfPathField, 1, false)
                .addLabeledComponent(JBLabel(I18nUtils.getPathLabel("Void")), voidPathField, 1, false)
                .addLabeledComponent(JBLabel(I18nUtils.getPathLabel("Kiro")), kiroPathField, 1, false)
                .addLabeledComponent(JBLabel(I18nUtils.getPathLabel("Qoder")), qoderPathField, 1, false)
                .addLabeledComponent(JBLabel(I18nUtils.getPathLabel("catPawAI")), catPawAIPathField, 1, false)
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

      fun getVoidPath(): String {
        return voidPathField.text
    }

    fun setVoidPath(path: String) {
        voidPathField.text = path
    }

    fun getKiroPath(): String {
        return kiroPathField.text
    }

    fun setKiroPath(path: String) {
        kiroPathField.text = path
    }

    fun getQoderPath(): String {
        return qoderPathField.text
    }

    fun setQoderPath(path: String) {
        qoderPathField.text = path
    }

    fun getcatPawAIPath(): String {
        return catPawAIPathField.text
    }

    fun setcatPawAIPath(path: String) {
        catPawAIPathField.text = path
    }

    fun getSelectedEditorType(): String {
        return editorTypeComboBox.selectedItem as String
    }

    fun setSelectedEditorType(editorType: String) {
        editorTypeComboBox.selectedItem = editorType
    }

    fun getTraeCN(): Boolean {
        return if (SystemInfo.isMac) traeCNCheckBox.isSelected else false
    }

    fun setTraeCN(selected: Boolean) {
        if (SystemInfo.isMac) {
            traeCNCheckBox.isSelected = selected
        }
    }

}
