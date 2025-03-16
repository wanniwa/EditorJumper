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
        val handler = getEditorHandler()
        
        // 检查编辑器路径是否存在
        if (!checkEditorPathExists(project, handler)) {
            return
        }

        val selectedFile = e.getData(CommonDataKeys.VIRTUAL_FILE)
        val editor = e.getData(CommonDataKeys.EDITOR)

        if (editor != null) {
            // 处理从编辑器中选择的文件
            if (selectedFile != null) {
                val file = selectedFile
                val line = editor.caretModel.logicalPosition.line.plus(1)

                // 获取当前行的文本
                val document = editor.document
                val lineStartOffset = document.getLineStartOffset(editor.caretModel.logicalPosition.line)
                val lineEndOffset = document.getLineEndOffset(editor.caretModel.logicalPosition.line)
                val lineText = document.getText(com.intellij.openapi.util.TextRange(lineStartOffset, lineEndOffset))

                // 获取光标在当前行的偏移量
                val caretOffsetInLine = editor.caretModel.offset - lineStartOffset

                // 计算光标前的制表符数量
                val tabsBeforeCaret = lineText.substring(0, caretOffsetInLine).count { it == '\t' }
                var offset = 0
                if (tabsBeforeCaret != 0) {
                    offset = (tabsBeforeCaret * 3)
                }
                // 调整列位置，每个制表符减去3（假设制表符宽度为4）
                val column = editor.caretModel.visualPosition.column.plus(1) - offset

                openInExternalEditor(project, handler, file, line, column)
            } else {
                // 没有选择文件，打开项目目录
                openInExternalEditor(project, handler, null)
            }
        } else if (selectedFile != null && !selectedFile.isDirectory) {
            // 处理从项目视图选择的文件（不是文件夹）
            openInExternalEditor(project, handler, selectedFile)
        } else {
            // 没有选择文件或选择的是文件夹，打开项目目录
            openInExternalEditor(project, handler, null)
        }
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
            processBuilder.directory(File(projectPath))
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
        } catch (ex: IOException) {
            Messages.showErrorDialog(
                project,
                "Failed to open editor. Error: ${ex.message}",
                "Editor Launch Failed"
            )
            ex.printStackTrace()
        } catch (ex: Exception) {
            Messages.showErrorDialog(
                project,
                "Failed to open editor. Error: ${ex.message}",
                "Editor Launch Failed"
            )
            ex.printStackTrace()
        }
    }
} 