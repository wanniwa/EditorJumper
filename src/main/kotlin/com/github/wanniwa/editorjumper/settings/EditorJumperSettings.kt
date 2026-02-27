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

    /** Map of editor name -> custom executable path. */
    var editorPaths: MutableMap<String, String> = HashMap()

    /**
     * Editors that are hidden from the status bar menu.
     * If empty, all known editors are shown (backwards compatible default).
     */
    var hiddenEditors: MutableSet<String> = LinkedHashSet()

    var selectedEditorType: String = "Cursor"
    var hasShownStatusBarGuide: Boolean = false

    // ---------------------------------------------------------------------------
    // Legacy fields kept solely for one-time migration of existing user settings.
    // ---------------------------------------------------------------------------
    var vscodePath: String = ""
    var cursorPath: String = ""
    var traePath: String = ""
    var windsurfPath: String = ""
    var voidPath: String = ""
    var kiroPath: String = ""
    var qoderPath: String = ""
    var catPawAIPath: String = ""
    var antigravityPath: String = ""
    var traeCN: Boolean = false

    fun getPath(editorName: String): String = editorPaths[editorName] ?: ""

    fun setPath(editorName: String, path: String) {
        editorPaths[editorName] = path
    }

    companion object {
        fun getInstance(): EditorJumperSettings =
            ApplicationManager.getApplication().getService(EditorJumperSettings::class.java)
    }

    override fun getState(): EditorJumperSettings = this

    override fun loadState(state: EditorJumperSettings) {
        XmlSerializerUtil.copyBean(state, this)
        migrateLegacyFields()
    }

    private fun migrateLegacyFields() {
        if (editorPaths.isEmpty()) {
            mapOf(
                "Visual Studio Code" to vscodePath,
                "Cursor" to cursorPath,
                (if (traeCN) "Trae CN" else "Trae") to traePath,
                "Windsurf" to windsurfPath,
                "Void" to voidPath,
                "Kiro" to kiroPath,
                "Qoder" to qoderPath,
                "CatPawAI" to catPawAIPath,
                "Antigravity" to antigravityPath,
            ).filter { it.value.isNotEmpty() }
                .forEach { (k, v) -> editorPaths[k] = v }
        }

        // 兼容历史数据：之前用 Trae + traeCN，现在显式区分为两种编辑器
        if (traeCN && selectedEditorType == "Trae") {
            selectedEditorType = "Trae CN"
        }

    }
}
