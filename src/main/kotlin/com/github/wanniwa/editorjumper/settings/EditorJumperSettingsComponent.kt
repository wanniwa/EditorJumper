package com.github.wanniwa.editorjumper.settings

import com.github.wanniwa.editorjumper.editors.EditorRegistry
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.ui.TextBrowseFolderListener
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBRadioButton
import com.intellij.util.ui.FormBuilder
import com.github.wanniwa.editorjumper.utils.I18nUtils
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.DefaultComboBoxModel
import javax.swing.ButtonGroup
import java.awt.GridBagLayout
import java.awt.GridBagConstraints
import java.awt.Insets
import java.awt.FlowLayout

class EditorJumperSettingsComponent {
    private val myMainPanel: JPanel
    private val editorTypeComboBox = ComboBox<String>()
    private val editorNameLabels: MutableMap<String, JBLabel> = mutableMapOf()

    private val pathFields: Map<String, TextFieldWithBrowseButton> =
        EditorRegistry.editors.associate { editor ->
            val field = TextFieldWithBrowseButton()
            val descriptor = FileChooserDescriptor(true, false, false, false, false, false)
            descriptor.title = I18nUtils.getFileChooserTitle(editor.name)
            field.addBrowseFolderListener(TextBrowseFolderListener(descriptor))
            editor.name to field
        }

    private val hiddenCheckBoxes: Map<String, JBCheckBox> =
        EditorRegistry.editors.associate { editor ->
            editor.name to JBCheckBox()
        }

    private val selectedButtons: Map<String, JBRadioButton> =
        EditorRegistry.editors.associate { editor ->
            editor.name to JBRadioButton()
        }

    private val buttonGroup = ButtonGroup()

    init {
        editorTypeComboBox.model = DefaultComboBoxModel(EditorRegistry.editorNames.toTypedArray())

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

        // Editors table: Editor | Path | Hidden
        val editorsPanel = JPanel(GridBagLayout())
        val gbc = GridBagConstraints().apply {
            fill = GridBagConstraints.HORIZONTAL
            insets = Insets(2, 2, 2, 2)
        }

        // Header row
        gbc.gridy = 0
        gbc.weighty = 0.0

        gbc.gridx = 0
        gbc.weightx = 0.0
        editorsPanel.add(JBLabel("Editor"), gbc)

        gbc.gridx = 1
        gbc.weightx = 1.0
        editorsPanel.add(JBLabel("Path"), gbc)

        gbc.gridx = 2
        gbc.weightx = 0.0
        editorsPanel.add(JBLabel("Hide"), gbc)

        // Data rows
        var row = 1
        EditorRegistry.editors.forEach { editor ->
            val radio = selectedButtons[editor.name]!!
            radio.text = ""
            buttonGroup.add(radio)

            val pathField = pathFields[editor.name]!!
            val hiddenCheckBox = hiddenCheckBoxes[editor.name]!!
            hiddenCheckBox.text = ""
            hiddenCheckBox.addActionListener {
                updateEditorNameStyle(editor.name)
            }

            gbc.gridy = row

            val editorLabel = JBLabel(editor.name)
            editorNameLabels[editor.name] = editorLabel
            val editorCell = JPanel(FlowLayout(FlowLayout.LEFT, 4, 0)).apply {
                isOpaque = false
                add(radio)
                add(editorLabel)
            }
            gbc.gridx = 0
            gbc.weightx = 0.0
            editorsPanel.add(editorCell, gbc)

            gbc.gridx = 1
            gbc.weightx = 1.0
            editorsPanel.add(pathField, gbc)

            gbc.gridx = 2
            gbc.weightx = 0.0
            editorsPanel.add(hiddenCheckBox, gbc)

            row++
        }

        formBuilder.addComponent(editorsPanel)

        myMainPanel = formBuilder
            .addComponentFillVertically(JPanel(), 0)
            .panel
    }

    fun getPanel(): JPanel = myMainPanel

    fun getPreferredFocusedComponent(): JComponent = editorTypeComboBox

    fun getPath(editorName: String): String = pathFields[editorName]?.text ?: ""

    fun setPath(editorName: String, path: String) {
        pathFields[editorName]?.text = path
    }

    fun isEditorVisible(editorName: String): Boolean =
        !(hiddenCheckBoxes[editorName]?.isSelected ?: false)

    fun setEditorVisible(editorName: String, visible: Boolean) {
        hiddenCheckBoxes[editorName]?.isSelected = !visible
        updateEditorNameStyle(editorName)
    }

    fun setHideToggleEnabled(editorName: String, enabled: Boolean) {
        hiddenCheckBoxes[editorName]?.isEnabled = enabled
    }

    fun getDefaultEditorType(): String = editorTypeComboBox.selectedItem as String

    fun setDefaultEditorType(editorType: String) {
        editorTypeComboBox.selectedItem = editorType
    }

    fun getSelectedEditorType(): String {
        return selectedButtons.entries.firstOrNull { it.value.isSelected }?.key
            ?: EditorRegistry.editorNames.first()
    }

    fun setSelectedEditorType(editorType: String) {
        val target = if (selectedButtons.containsKey(editorType)) editorType else EditorRegistry.editorNames.first()
        selectedButtons.forEach { (name, button) ->
            button.isEnabled = true
            button.isSelected = name == target
        }
    }

    private fun updateEditorNameStyle(editorName: String) {
        val label = editorNameLabels[editorName] ?: return
        val hidden = hiddenCheckBoxes[editorName]?.isSelected ?: false
        label.text = if (hidden) "<html><s>$editorName</s></html>" else editorName
    }
}
