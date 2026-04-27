package com.github.wanniwa.editorjumper.utils

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class WslUtilsTest {

    @Nested
    inner class IsWslPath {

        @ParameterizedTest
        @ValueSource(strings = [
            "\\\\wsl\$\\Ubuntu\\home\\user\\project",
            "\\\\wsl.localhost\\Ubuntu\\home\\user\\project",
            "\\\\WSL\$\\Ubuntu\\home\\user",
            "\\\\WSL.LOCALHOST\\Debian\\home",
            "//wsl\$/Ubuntu/home/user/project",
            "//wsl.localhost/Ubuntu/home/user/project",
        ])
        fun `returns true for WSL UNC paths`(path: String) {
            assertTrue(WslUtils.isWslPath(path))
        }

        @ParameterizedTest
        @ValueSource(strings = [
            "C:\\Users\\me\\project",
            "/home/user/project",
            "\\\\server\\share",
            "",
            "wsl",
            "\\\\ws",
        ])
        fun `returns false for non-WSL paths`(path: String) {
            assertFalse(WslUtils.isWslPath(path))
        }
    }

    @Nested
    inner class ExtractDistro {

        @Test
        fun `extracts distro from wsl dollar path`() {
            assertEquals("Ubuntu", WslUtils.extractDistro("\\\\wsl\$\\Ubuntu\\home\\user"))
        }

        @Test
        fun `extracts distro from wsl localhost path`() {
            assertEquals("Debian", WslUtils.extractDistro("\\\\wsl.localhost\\Debian\\home\\user"))
        }

        @Test
        fun `extracts distro when path ends at distro name`() {
            assertEquals("Ubuntu", WslUtils.extractDistro("\\\\wsl\$\\Ubuntu"))
        }

        @Test
        fun `extracts distro with forward slashes`() {
            assertEquals("Ubuntu", WslUtils.extractDistro("//wsl\$/Ubuntu/home/user"))
        }

        @Test
        fun `returns null for non-WSL path`() {
            assertNull(WslUtils.extractDistro("C:\\Users\\me"))
        }

        @Test
        fun `returns null for empty string`() {
            assertNull(WslUtils.extractDistro(""))
        }

        @Test
        fun `returns null when prefix present but no distro`() {
            assertNull(WslUtils.extractDistro("\\\\wsl\$\\"))
        }

        @Test
        fun `is case-insensitive`() {
            assertEquals("Ubuntu", WslUtils.extractDistro("\\\\WSL.LOCALHOST\\Ubuntu\\home"))
        }
    }

    @Nested
    inner class ToLinuxPath {

        @Test
        fun `converts wsl dollar path to linux path`() {
            assertEquals(
                "/home/user/project",
                WslUtils.toLinuxPath("\\\\wsl\$\\Ubuntu\\home\\user\\project")
            )
        }

        @Test
        fun `converts wsl localhost path to linux path`() {
            assertEquals(
                "/home/user/project",
                WslUtils.toLinuxPath("\\\\wsl.localhost\\Debian\\home\\user\\project")
            )
        }

        @Test
        fun `returns root for distro-only path`() {
            assertEquals("/", WslUtils.toLinuxPath("\\\\wsl\$\\Ubuntu"))
        }

        @Test
        fun `converts forward-slash WSL path`() {
            assertEquals(
                "/home/user/project",
                WslUtils.toLinuxPath("//wsl\$/Ubuntu/home/user/project")
            )
        }

        @Test
        fun `returns original path for non-WSL input`() {
            val windowsPath = "C:\\Users\\me\\project"
            assertEquals(windowsPath, WslUtils.toLinuxPath(windowsPath))
        }

        @Test
        fun `returns original path for empty string`() {
            assertEquals("", WslUtils.toLinuxPath(""))
        }

        @Test
        fun `returns original path for linux-style path`() {
            val linuxPath = "/home/user/project"
            assertEquals(linuxPath, WslUtils.toLinuxPath(linuxPath))
        }
    }
}
