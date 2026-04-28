package com.github.wanniwa.editorjumper.utils

/**
 * WSL (Windows Subsystem for Linux) 路径检测与转换工具。
 *
 * WSL 项目通过 UNC 路径访问，格式如：
 * - `\\wsl$\Ubuntu\home\user\project`
 * - `\\wsl.localhost\Ubuntu\home\user\project`
 */
object WslUtils {

    private const val WSL_DOLLAR_PREFIX = "\\\\wsl\$\\"
    private const val WSL_LOCALHOST_PREFIX = "\\\\wsl.localhost\\"

    private val WSL_PREFIXES = listOf(WSL_DOLLAR_PREFIX, WSL_LOCALHOST_PREFIX)

    /**
     * 判断 [path] 是否为指向 WSL 发行版的 Windows UNC 路径。
     */
    fun isWslPath(path: String): Boolean {
        if (path.length < 5) return false
        val normalized = path.replace('/', '\\')
        return WSL_PREFIXES.any { normalized.startsWith(it, ignoreCase = true) }
    }

    /**
     * 从 WSL UNC 路径中提取发行版名称。
     *
     * `\\wsl$\Ubuntu\home\user` → `Ubuntu`
     *
     * @return 发行版名称，如果 [wslPath] 不是有效的 WSL UNC 路径则返回 null。
     */
    fun extractDistro(wslPath: String): String? {
        val normalized = wslPath.replace('/', '\\')
        val prefix = WSL_PREFIXES.firstOrNull { normalized.startsWith(it, ignoreCase = true) }
            ?: return null
        val afterPrefix = normalized.substring(prefix.length)
        if (afterPrefix.isEmpty()) return null
        val nextSlash = afterPrefix.indexOf('\\')
        val distro = if (nextSlash >= 0) afterPrefix.substring(0, nextSlash) else afterPrefix
        return distro.ifEmpty { null }
    }

    /**
     * 将 Windows WSL UNC 路径转换为对应的 Linux 路径。
     * 如果 [wslPath] 不是 WSL UNC 路径，原样返回。
     *
     * `\\wsl$\Ubuntu\home\user\project` → `/home/user/project`
     */
    fun toLinuxPath(wslPath: String): String {
        if (!isWslPath(wslPath)) return wslPath
        val normalized = wslPath.replace('/', '\\')
        val prefix = WSL_PREFIXES.first { normalized.startsWith(it, ignoreCase = true) }
        val afterPrefix = normalized.substring(prefix.length)
        val firstSlash = afterPrefix.indexOf('\\')
        val linuxPart = if (firstSlash >= 0) afterPrefix.substring(firstSlash) else "/"
        return linuxPart.replace('\\', '/')
    }
}
