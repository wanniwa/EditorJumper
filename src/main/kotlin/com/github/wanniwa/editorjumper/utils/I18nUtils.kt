package com.github.wanniwa.editorjumper.utils

import com.intellij.openapi.diagnostic.Logger
import java.text.MessageFormat
import java.util.*

/**
 * 国际化工具类
 * 根据 IDEA 的语言设置自动切换中英文
 */
object I18nUtils {
    private val logger = Logger.getInstance(I18nUtils::class.java)
    
    private val bundle: ResourceBundle by lazy {
        ResourceBundle.getBundle("bundle")
    }

    /**
     * 获取国际化文本
     * @param key 资源键
     * @return 对应语言的文本，如果找不到则返回键值本身
     */
    fun message(key: String): String {
        return try {
            bundle.getString(key)
        } catch (e: MissingResourceException) {
            logger.warn("Missing translation key: $key", e)
            // 返回友好的fallback文本而不是原始key
            "[Missing: $key]"
        }
    }

    /**
     * 获取带参数的国际化文本
     * @param key 资源键
     * @param args 参数
     * @return 格式化后的文本
     */
    fun message(key: String, vararg args: Any): String {
        return try {
            val pattern = bundle.getString(key)
            MessageFormat.format(pattern, *args)
        } catch (e: MissingResourceException) {
            logger.warn("Missing translation key: $key", e)
            "[Missing: $key]"
        } catch (e: IllegalArgumentException) {
            logger.warn("Invalid message format for key: $key, args: ${args.contentToString()}", e)
            // 尝试返回原始模板
            try {
                bundle.getString(key)
            } catch (ex: MissingResourceException) {
                "[Format Error: $key]"
            }
        }
    }


    /**
     * 获取设置路径标签文本
     */
    fun getPathLabel(editorName: String): String {
        return message("settings.path.label", editorName)
    }

    /**
     * 获取文件选择器标题
     */
    fun getFileChooserTitle(editorType: String): String {
        return message("fileChooser.title", editorType)
    }
}