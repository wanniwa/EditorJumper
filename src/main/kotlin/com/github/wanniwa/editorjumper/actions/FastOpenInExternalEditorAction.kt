package com.github.wanniwa.editorjumper.actions

import com.github.wanniwa.editorjumper.editors.EditorHandler
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.util.SystemInfo
import com.intellij.openapi.vfs.VirtualFile

/**
 * Action for opening files in external editor using Mac URL scheme
 * Triggered by Option+Shift+P shortcut on Mac systems
 */
class FastOpenInExternalEditorAction : BaseAction() {

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }

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
        var lineNumber = editor?.caretModel?.currentCaret?.logicalPosition?.line?.plus(1)
        var columnNumber = editor?.caretModel?.currentCaret?.logicalPosition?.column?.plus(1)
        if (lineNumber == null) {
            lineNumber = 1
        }
        if (columnNumber == null) {
            columnNumber = 1
        }
        // 在外部编辑器中打开
        if (SystemInfo.isMac) {
            openInExternalEditor(project, handler, selectedFile, lineNumber, columnNumber)
        } else {
            super.openInExternalEditor(project, handler, selectedFile, lineNumber, columnNumber)
        }
    }

    @Override
    override fun openInExternalEditor(
        project: Project,
        handler: EditorHandler,
        file: VirtualFile?,
        lineNumber: Int?,
        columnNumber: Int?
    ) {
        val projectPath = getProjectPath(project) ?: return
        val filePath = getFilePath(file)
        
        try {
            val command = handler.getFastOpenCommand(projectPath, filePath, lineNumber, columnNumber)

            ProcessBuilder(command.toList())
                .redirectOutput(ProcessBuilder.Redirect.DISCARD)
                .redirectError(ProcessBuilder.Redirect.DISCARD)
                .start()
                .outputStream.close()

        } catch (e: Exception) {
            Messages.showErrorDialog(
                project,
                "Failed to fast open editor. Error: ${e.message}",
                "Editor Launch Failed"
            )
        }
    }
}