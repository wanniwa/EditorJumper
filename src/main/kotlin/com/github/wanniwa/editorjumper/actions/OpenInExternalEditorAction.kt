package com.github.wanniwa.editorjumper.actions

import com.github.wanniwa.editorjumper.editors.EditorHandler
import com.github.wanniwa.editorjumper.settings.EditorJumperSettings
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VirtualFile
import java.io.File
import java.io.IOException

/**
 * 在外部编辑器中打开文件或文件夹的动作
 */
class OpenInExternalEditorAction : BaseAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return

        // 获取编辑器处理器
        val handler = getEditorHandler(project)
        
        // 检查编辑器路径是否存在
        if (!checkEditorPathExists(project, handler)) {
            return
        }

        val selectedFile = e.getData(CommonDataKeys.VIRTUAL_FILE)
        val editor = e.getData(CommonDataKeys.EDITOR)

        // 获取光标位置
        val lineNumber = editor?.caretModel?.currentCaret?.logicalPosition?.line?.plus(1)
        val columnNumber = editor?.caretModel?.currentCaret?.logicalPosition?.column?.plus(1)

        // 在外部编辑器中打开
        openInExternalEditor(project, handler, selectedFile, lineNumber, columnNumber)
    }

    /**
     * 更新动作的可见性和文本
     */
    override fun update(e: AnActionEvent) {
        super.update(e)

        // 更新菜单项文本
        val settings = EditorJumperSettings.getInstance()
        val editorType = settings.selectedEditorType
        e.presentation.text = "Open in $editorType"
    }

    /**
     * 在外部编辑器中打开文件或文件夹
     */
    private fun openInExternalEditor(
        project: Project,
        handler: EditorHandler,
        file: VirtualFile?,
        lineNumber: Int? = null,
        columnNumber: Int? = null
    ) {
        val projectPath = getProjectPath(project) ?: return
        val filePath = file?.let { 
            if (it.isDirectory) null else getFilePath(it) ?: it.path 
        }

        // 使用 getOpenCommand 方法
        val command = handler.getOpenCommand(
            projectPath,
            filePath,
            lineNumber,
            columnNumber
        )

        try {
            // 使用列表构造函数
            val processBuilder = ProcessBuilder(command.toList())
            
            val process = processBuilder.start()
            // 检查进程是否成功启动
//            val exitCode = process.waitFor()
//            if (exitCode != 0) {
//                val errorStream = process.errorStream.bufferedReader().readText()
//                Messages.showErrorDialog(
//                    project,
//                    "Failed to open editor. Error: $errorStream",
//                    "Editor Launch Failed"
//                )
//            }
        } catch (e: IOException) {
            Messages.showErrorDialog(
                project,
                "Failed to open editor. Error: ${e.message}",
                "Editor Launch Failed"
            )
        }
    }
} 