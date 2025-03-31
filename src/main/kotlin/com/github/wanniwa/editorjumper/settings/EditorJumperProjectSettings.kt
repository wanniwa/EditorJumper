package com.github.wanniwa.editorjumper.settings

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.XmlSerializerUtil

@Service(Service.Level.PROJECT)
@State(
    name = "com.github.wanniwa.editorjumper.settings.EditorJumperProjectSettings",
    storages = [Storage("editorJumperProjectSettings.xml")]
)
class EditorJumperProjectSettings : PersistentStateComponent<EditorJumperProjectSettings> {
    var vsCodeWorkspacePath: String = ""

    companion object {
        fun getInstance(project: Project): EditorJumperProjectSettings {
            return project.getService(EditorJumperProjectSettings::class.java)
        }
    }

    override fun getState(): EditorJumperProjectSettings {
        return this
    }

    override fun loadState(state: EditorJumperProjectSettings) {
        XmlSerializerUtil.copyBean(state, this)
    }
} 