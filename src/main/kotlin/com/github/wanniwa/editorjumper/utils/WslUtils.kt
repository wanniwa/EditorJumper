package com.github.wanniwa.editorjumper.utils

/**
 * Utility for detecting and converting Windows WSL (Windows Subsystem for Linux) paths.
 *
 * WSL projects are accessed via UNC paths like:
 * - `\\wsl$\Ubuntu\home\user\project`
 * - `\\wsl.localhost\Ubuntu\home\user\project`
 */
object WslUtils {

    private const val WSL_DOLLAR_PREFIX = "\\\\wsl\$\\"
    private const val WSL_LOCALHOST_PREFIX = "\\\\wsl.localhost\\"

    private val WSL_PREFIXES = listOf(WSL_DOLLAR_PREFIX, WSL_LOCALHOST_PREFIX)

    /**
     * Returns true when [path] is a Windows UNC path pointing into a WSL distribution.
     */
    fun isWslPath(path: String): Boolean {
        if (path.length < 5) return false
        val normalized = path.replace('/', '\\')
        return WSL_PREFIXES.any { normalized.startsWith(it, ignoreCase = true) }
    }

    /**
     * Extracts the distribution name from a WSL UNC path.
     *
     * `\\wsl$\Ubuntu\home\user` → `Ubuntu`
     *
     * @return the distro name, or `null` if [wslPath] is not a valid WSL UNC path.
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
     * Converts a Windows WSL UNC path to the corresponding Linux path.
     * If [wslPath] is not a WSL UNC path, returns it unchanged.
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
