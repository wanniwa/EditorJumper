package com.github.wanniwa.editorjumper.settings

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
    var voidPath: String = ""
    var kiroPath: String = ""
    var selectedEditorType: String = "Cursor"

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