package com.github.wanniwa.editorjumper.settings

import com.github.wanniwa.editorjumper.messaging.EditorSettingsChangedListener
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil

@State(
    name = "com.github.wanniwa.editorjumper.settings.EditorJumperSettings",
    storages = [Storage("EditorJumperSettings.xml")]
)
class EditorJumperSettings : PersistentStateComponent<EditorJumperSettings> {
    var vsCodePath: String = ""
    var cursorPath: String = ""
    var traePath: String = ""
    var windsurfPath: String = ""
    private var _selectedEditorType: String = "Cursor" // Default to Cursor
    
    var selectedEditorType: String
        get() = _selectedEditorType
        set(value) {
            if (_selectedEditorType != value) {
                _selectedEditorType = value
                // 通知所有监听器编辑器类型已更改
                ApplicationManager.getApplication().messageBus.syncPublisher(
                    EditorSettingsChangedListener.TOPIC
                ).editorTypeChanged(value)
            }
        }

    companion object {
        fun getInstance(): EditorJumperSettings {
            return ApplicationManager.getApplication().getService(EditorJumperSettings::class.java)
        }
    }

    override fun getState(): EditorJumperSettings {
        return this
    }

    override fun loadState(state: EditorJumperSettings) {
        XmlSerializerUtil.copyBean(state, this)
    }
} 