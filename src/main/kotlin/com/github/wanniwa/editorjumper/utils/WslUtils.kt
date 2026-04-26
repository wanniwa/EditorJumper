package com.github.wanniwa.editorjumper.utils

/**
 * Utility for detecting and converting Windows WSL (Windows Subsystem for Linux) paths.
 *
 * WSL projects are accessed via UNC paths like:
 * - `\\wsl$\Ubuntu\home\user\project`
 * - `\\wsl.localhost\Ubuntu\home\user\project`
 */
object WslUtils {

    /** Pattern prefixes that indicate a WSL UNC path. */
    private val WSL_PREFIXES = listOf("\\\\wsl$\\", "\\\\wsl.localhost\\")

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
     */
    fun extractDistro(wslPath: String): String {
        val normalized = wslPath.replace('/', '\\')
        val prefix = WSL_PREFIXES.firstOrNull { normalized.startsWith(it, ignoreCase = true) } ?: return ""
        val afterPrefix = normalized.substring(prefix.length)
        val nextSlash = afterPrefix.indexOf('\\')
        return if (nextSlash >= 0) afterPrefix.substring(0, nextSlash) else afterPrefix
    }

    /**
     * Converts a Windows WSL UNC path to the corresponding Linux path.
     *
     * `\\wsl$\Ubuntu\home\user\project` → `/home/user/project`
     */
    fun toLinuxPath(wslPath: String): String {
        val normalized = wslPath.replace('/', '\\')
        val prefix = WSL_PREFIXES.firstOrNull { normalized.startsWith(it, ignoreCase = true) } ?: return wslPath
        val afterPrefix = normalized.substring(prefix.length)
        val firstSlash = afterPrefix.indexOf('\\')
        val linuxPart = if (firstSlash >= 0) afterPrefix.substring(firstSlash) else "/"
        return linuxPart.replace('\\', '/')
    }
}
