# Editor Jumper

<div align="center">
  <img src="src/main/resources/META-INF/pluginIcon.svg" alt="Editor Jumper Icon" width="128" height="128"/>
</div>

<div >
  <img src="https://img.shields.io/badge/JetBrains-Plugin-orange" alt="JetBrains Plugin"/>
  <img src="https://img.shields.io/badge/License-MIT-blue" alt="License"/>
  <a href="README_CN.md"><img src="https://img.shields.io/badge/文档-中文版-red.svg" alt="Chinese Doc"/></a>
</div>

## 🔍 Introduction

Editor Jumper is a JetBrains IDE plugin that allows you to seamlessly jump between JetBrains IDE and other popular code editors (such as VS Code, Cursor, Trae, and Windsurf). It maintains your cursor position and editing context, greatly improving development efficiency in multi-editor environments.

## 🌟 Features

- 🚀 **Seamless Editor Switching**
  - Quickly jump between JetBrains IDE and VS Code, Cursor, Trae, Windsurf
  - Automatically positions to the same cursor location (line and column)
  - Perfectly maintains editing context without interrupting workflow

- 🎯 **Smart Jump Behavior**
  - With file open: Opens the same project and file, preserving cursor position
  - Without file open: Opens the project directly in target editor

- ⚡ **Multiple Trigger Methods**
  - Right-click in editor - select "Open in [Editor Name]" (name updates based on selected editor)
  - Tools menu - select "Open in [Editor Name]" (name updates based on selected editor)
  - Keyboard shortcut - Alt+Shift+O (Windows) or Option+Shift+O (macOS)

- 🎚️ **Easy Target Editor Selection**
  - Status bar widget - click the editor icon to select which editor to jump to

## 💻 System Requirements

- Works with any JetBrains IDE (IntelliJ IDEA, WebStorm, PyCharm, etc.)
- Requires IDE version 2022.3 or newer

## 📥 Installation

1. Open Settings/Preferences in JetBrains IDE
2. Navigate to Plugins > Marketplace
3. Search for "Editor Jumper"
4. Click the Install button
5. Restart the IDE to activate the plugin

## ⚙️ Configuration

1. Open Settings/Preferences in the IDE
2. Navigate to Tools > Editor Jumper Settings
3. Configure the executable paths for each editor:
   - VS Code path
   - Cursor path
   - Trae path
   - Windsurf path
4. Select the default editor
5. Click Apply to save settings

> **Configuration Notes:**
> - macOS: No additional configuration needed for any editor
> - Windows:
>   - Cursor: No configuration needed (uses system PATH)
>   - Other editors: Configure the .exe file path in Settings
>
> **Configuration Interface:**
> - Default Editor: Select which editor to use when using keyboard shortcuts
> - Editor Paths:
>   - macOS: All paths are auto-detected, no manual configuration needed
>   - Windows:
>     - Cursor: Auto-detected through system PATH
>     - VS Code/Trae/Windsurf: Browse and select the .exe file location
>     - Example: `C:\Users\username\AppData\Local\Programs\VSCode\Code.exe`

## 🚀 Usage

### Via Keyboard Shortcut

- Use `Option+Shift+P` (macOS) or `Alt+Shift+P` (Windows) to open the current project in the default editor

### Via Context Menu

1. Right-click in the editor
2. Select "Open in External Editor"

### Via Tools Menu

1. Click on Tools in the top menu bar
2. Select "Open in External Editor"

### Via Status Bar

1. Click on the editor icon in the IDE's bottom status bar
2. Select the editor you want to jump to from the dropdown menu
   ![Select Target Editor](image/SelectTargetEditor.png)
3. Use any of the trigger methods above (keyboard shortcut, right-click, or Tools menu) to perform the jump

## 🔄 Complementary Use

> Recommended to use with [Switch2IDEA](https://github.com/qczone/switch2idea) to quickly return to JetBrains IDE from Cursor, VS Code, Trae, Windsurf

## 🤝 Contribution

Pull Requests and Issues are welcome to help improve this plugin!

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details
