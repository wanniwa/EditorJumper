# Editor Jumper

<div >
  <img src="https://img.shields.io/badge/JetBrains-Plugin-orange" alt="JetBrains Plugin"/>
  <img src="https://img.shields.io/badge/License-MIT-blue" alt="License"/>
  <a href="README.md"><img src="https://img.shields.io/badge/Doc-English-blue.svg" alt="English Doc"/></a>
</div>

## 🔍 简介

Editor Jumper 是一个 JetBrains IDE 插件，允许您在 JetBrains IDE 和其他流行的代码编辑器（如 VS Code、Cursor、Trae 和 Windsurf）之间无缝跳转。它能够保持光标位置和编辑上下文，大大提高了多编辑器环境中的开发效率。

## 🌟 功能特点

- 🚀 **无缝编辑器切换**
  - 快速在 JetBrains IDE 和 VS Code、Cursor、Trae、Windsurf 之间跳转
  - 自动定位到相同的光标位置（行和列）
  - 完美保持编辑上下文，不中断工作流

- 🎯 **智能跳转行为**
  - 有文件打开时：打开相同的项目和文件，保持光标位置
  - 无文件打开时：直接在目标编辑器中打开项目

- ⚡ **多种触发方式**
  - 在编辑器中右击 - 选择"在 [编辑器名称] 中打开"（名称会根据选择的编辑器更新）
  - 工具菜单 - 选择"在 [编辑器名称] 中打开"（名称会根据选择的编辑器更新）
  - 快捷键 - Alt+Shift+O (Windows) 或 Option+Shift+O (macOS)

- 🎚️ **便捷的目标编辑器选择**
  - 状态栏小部件 - 点击编辑器图标选择要跳转到哪个编辑器

## 💻 系统要求

- 适用于任何 JetBrains IDE（IntelliJ IDEA、WebStorm、PyCharm 等）
- 需要 IDE 版本 2022.3 或更新版本

## 📥 安装

1. 在 JetBrains IDE 中打开设置/首选项
2. 导航到插件 > 市场
3. 搜索 "Editor Jumper"
4. 点击安装按钮
5. 重启 IDE 以激活插件

## ⚙️ 配置

1. 在 IDE 中打开设置/首选项
2. 导航到工具 > Editor Jumper 设置
3. 配置每个编辑器的可执行文件路径：
   - VS Code 路径
   - Cursor 路径
   - Trae 路径
   - Windsurf 路径
4. 选择默认编辑器
5. 点击应用保存设置

> **配置说明：**
> - macOS：所有编辑器无需额外配置
> - Windows：
>   - Cursor：无需配置（使用系统 PATH）
>   - 其他编辑器：需在设置中配置 .exe 文件路径
>
> **配置界面：**
> - 默认编辑器：选择使用快捷键时要使用的编辑器
> - 编辑器路径：
>   - macOS：所有路径都自动检测，无需手动配置
>   - Windows：
>     - Cursor：通过系统 PATH 自动检测
>     - VS Code/Trae/Windsurf：浏览并选择 .exe 文件位置
>     - 示例：`C:\Users\username\AppData\Local\Programs\VSCode\Code.exe`

## 🚀 使用方法

### 通过快捷键

- 使用 `Option+Shift+O`（macOS）或 `Alt+Shift+O`（Windows）在默认编辑器中打开当前项目

### 通过上下文菜单

1. 在编辑器中右击
2. 选择"在外部编辑器中打开"

### 通过工具菜单

1. 点击顶部菜单栏中的工具
2. 选择"在外部编辑器中打开"

### 通过状态栏

1. 点击 IDE 底部状态栏中的编辑器图标
2. 从下拉菜单中选择要跳转到的编辑器
   ![选择目标编辑器](image/SelectTargetEditor.png)
3. 使用上述任一触发方式（快捷键、右击或工具菜单）执行跳转

## 🔄 配套使用

> 推荐与 [Switch2IDEA](https://github.com/qczone/switch2idea) 配合使用，以便从 Cursor、VS Code、Trae、Windsurf 快速返回 JetBrains IDE

## 🤝 贡献

欢迎提交 Pull Requests 和 Issues 来帮助改进这个插件！

## 📄 许可证

本项目采用 MIT 许可证 - 详情请参阅 [LICENSE](LICENSE) 文件 