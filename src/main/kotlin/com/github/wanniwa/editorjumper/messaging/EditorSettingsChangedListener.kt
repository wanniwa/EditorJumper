package com.github.wanniwa.editorjumper.messaging

import com.intellij.util.messages.Topic

interface EditorSettingsChangedListener {
    companion object {
        val TOPIC = Topic.create("EditorJumper settings changed", EditorSettingsChangedListener::class.java)
    }

    fun editorTypeChanged(newEditorType: String)
} 