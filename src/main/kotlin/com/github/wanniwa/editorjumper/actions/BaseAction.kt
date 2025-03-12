package com.github.wanniwa.editorjumper.actions

import com.github.wanniwa.editorjumper.editors.EditorHandler
import com.github.wanniwa.editorjumper.editors.EditorHandlerFactory
import com.github.wanniwa.editorjumper.settings.EditorJumperSettings
import com.github.wanniwa.editorjumper.settings.EditorJumperSettingsConfigurable
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VirtualFile
import java.io.File

/**
 * 基础动作类，提供通用方法
 */
abstract class BaseAction : AnAction() {
    
    override fun getActionUpdateThread() = ActionUpdateThread.BGT
    
    /**
     * 获取项目路径
     */
    protected open fun getProjectPath(project: Project): String? {
        return project.basePath
    }

    /**
     * 获取文件路径，子类可以覆盖此方法以提供不同的文件路径获取逻辑
     */
    protected open fun getFilePath(virtualFile: VirtualFile): String? {
        return if (virtualFile.isDirectory) null else virtualFile.path
    }
    
    /**
     * 获取编辑器处理器
     */
    protected open fun getEditorHandler(): EditorHandler {
        val settings = EditorJumperSettings.getInstance()
        val editorType = settings.selectedEditorType
        return EditorHandlerFactory.getHandler(editorType)
    }
    
    /**
     * 检查编辑器路径是否存在
     * @return 如果路径存在且可执行，则返回true；否则返回false
     */
    protected open fun checkEditorPathExists(project: Project, handler: EditorHandler): Boolean {
        val settings = EditorJumperSettings.getInstance()
        val editorType = settings.selectedEditorType
        val editorPath = handler.getPath()
        val editorFile = File(editorPath)
        
        if (!editorFile.exists() || !editorFile.canExecute()) {
            // 路径不存在或不可执行，提示用户配置
            val result = Messages.showYesNoDialog(
                project,
                "The path to $editorType does not exist or is not executable: $editorPath\nWould you like to configure it now?",
                "Editor Path Not Found",
                "Open Settings",
                "Cancel",
                Messages.getErrorIcon()
            )
            
            if (result == Messages.YES) {
                // 打开设置对话框
                ShowSettingsUtil.getInstance().showSettingsDialog(
                    project,
                    EditorJumperSettingsConfigurable::class.java
                )
            }
            return false
        }
        return true
    }
    
    /**
     * 更新动作的可见性
     */
    override fun update(e: AnActionEvent) {
        val project = e.project
        
        // 只要有项目就启用该操作，不需要选择文件
        e.presentation.isEnabledAndVisible = project != null
    }
} 