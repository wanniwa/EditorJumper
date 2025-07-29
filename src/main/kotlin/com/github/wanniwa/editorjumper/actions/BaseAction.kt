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
import com.intellij.openapi.util.SystemInfo
import com.github.wanniwa.editorjumper.utils.EditorTargetUtils

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
    protected open fun getEditorHandler(project: Project?): EditorHandler {
        val editorType = EditorTargetUtils.getTargetEditor(project)
        return EditorHandlerFactory.getHandler(editorType, project)
    }
    
    /**
     * 检查编辑器路径是否存在
     * @return 如果路径有效，则返回true；否则返回false
     */
    protected fun checkEditorPathExists(project: Project, handler: EditorHandler): Boolean {
        val settings = EditorJumperSettings.getInstance()
        // 获取目标编辑器类型，如果为空则使用默认设置s
        val targetEditor = EditorTargetUtils.getTargetEditor(project)
        val editorType = if (targetEditor.isNullOrBlank()) settings.selectedEditorType else targetEditor
        val customPath = when (editorType) {
            "VSCode" -> settings.vsCodePath
            "Cursor" -> settings.cursorPath
            "Trae" -> settings.traePath
            "Windsurf" -> settings.windsurfPath
            "Void" -> settings.voidPath
            "Kiro" -> settings.kiroPath
            else -> ""
        }
        
        // macOS: 不需要检查路径，所有编辑器都自动检测
        if (SystemInfo.isMac) {
            return true
        }
        
        // Windows: 只检查非 Cursor 编辑器的路径
        if (SystemInfo.isWindows && editorType == "Cursor") {
            return true
        }
        
        // 其他情况: 只检查用户自定义的路径是否为空
        if (customPath.isBlank()) {
            // 路径为空，提示用户配置
            val result = Messages.showYesNoDialog(
                project,
                "The path to $editorType is not configured. Would you like to configure it now?",
                "Editor Path Not Configured",
                "Open Settings",
                "Cancel",
                Messages.getWarningIcon()
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