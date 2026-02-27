package com.github.wanniwa.editorjumper.editors

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Loads the ordered list of supported editors from editors.json (bundled as a resource).
 *
 * To add a new editor: add one JSON object to editors.json — no Kotlin code needed.
 */
object EditorRegistry {

    val editors: List<EditorRegistration> by lazy { loadRegistrations() }

    val editorNames: List<String> get() = editors.map { it.name }

    fun find(name: String): EditorRegistration? = editors.find { it.name == name }

    private fun loadRegistrations(): List<EditorRegistration> {
        val json = EditorRegistry::class.java
            .classLoader
            .getResourceAsStream("editors.json")
            ?.bufferedReader()
            ?.readText()
            ?: return emptyList()

        val type = object : TypeToken<List<EditorConfig>>() {}.type
        val configs: List<EditorConfig> = Gson().fromJson(json, type)

        return configs.map { cfg ->
            EditorRegistration(cfg.name) { path, project ->
                ConfigBasedEditorHandler(cfg, path, project)
            }
        }
    }
}
