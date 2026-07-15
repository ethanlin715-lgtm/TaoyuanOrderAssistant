# 使用 GitHub Actions 雲端構建 APK

由於本地 Gradle 配置問題，我們使用 **GitHub Actions** 進行雲端構建。這是完全免費的，不需要任何本地開發工具。

## 步驟 1：準備 GitHub 帳戶

1. 如果您還沒有 GitHub 帳戶，請訪問 [github.com](https://github.com) 並註冊一個免費帳戶
2. 記住您的用戶名和密碼

## 步驟 2：上傳項目到 GitHub

1. 訪問 [github.com/new](https://github.com/new) 創建新倉庫
2. 倉庫名稱：`TaoyuanOrderAssistant`
3. 選擇 **Public**（公開）
4. 點擊 **Create repository**

## 步驟 3：上傳項目文件

### 方法 A：使用 Git 命令行（推薦）

在您的電腦上打開命令提示符或 PowerShell，執行以下命令：

```bash
cd C:\path\to\TaoyuanOrderAssistant
git init
git add .
git commit -m "Initial commit"
git branch -M main
git remote add origin https://github.com/YOUR_USERNAME/TaoyuanOrderAssistant.git
git push -u origin main
```

將 `YOUR_USERNAME` 替換為您的 GitHub 用戶名。

### 方法 B：使用 GitHub Web 界面（如果沒有 Git）

1. 進入您剛創建的倉庫
2. 點擊 **Add file** → **Upload files**
3. 將整個 `TaoyuanOrderAssistant` 文件夾的內容拖放到上傳區域
4. 點擊 **Commit changes**

## 步驟 4：觸發自動構建

1. 進入您的 GitHub 倉庫
2. 點擊 **Actions** 標籤
3. 點擊 **Build Android APK** 工作流
4. 點擊 **Run workflow** → **Run workflow**

GitHub Actions 將自動開始構建。這通常需要 **5-10 分鐘**。

## 步驟 5：下載 APK

1. 等待構建完成（您會看到綠色的 ✓ 標記）
2. 點擊最新的構建任務
3. 在 **Artifacts** 部分，點擊 **app-debug** 下載 APK 檔案
4. 或者，如果看到 **Releases** 部分，直接下載 `app-debug.apk`

## 步驟 6：安裝到手機

1. 將 `app-debug.apk` 檔案複製到您的 Android 手機
2. 在手機上打開文件管理器，找到 APK 檔案
3. 點擊 APK 檔案進行安裝
4. 如果出現安全警告，選擇 **允許安裝未知來源的應用**

## 步驟 7：啟用必要的權限

安裝後，打開應用並啟用以下權限：

1. **Accessibility Service（無障礙服務）**
   - 進入手機設置 → 無障礙 → 無障礙服務
   - 找到「Taoyuan Order Assistant」
   - 打開開關

2. **Display over other apps（在其他應用上顯示）**
   - 進入手機設置 → 應用 → 應用權限 → 在其他應用上顯示
   - 找到「Taoyuan Order Assistant」
   - 打開開關

## 常見問題

### Q: 構建失敗怎麼辦？
A: 檢查 GitHub Actions 的構建日誌。通常是因為 Gradle 依賴問題。請聯繫技術支持。

### Q: 如何更新應用？
A: 修改代碼後，提交到 GitHub（`git push`），GitHub Actions 會自動重新構建。

### Q: APK 可以安裝到其他手機嗎？
A: 可以。APK 檔案是通用的，可以安裝到任何 Android 手機（API 24+）。

### Q: 如何卸載應用？
A: 進入手機設置 → 應用 → 找到「Taoyuan Order Assistant」→ 卸載。

## 需要幫助？

如果您在任何步驟遇到問題，請提供以下信息：
- 您的 GitHub 用戶名
- 構建失敗的錯誤信息（如果有）
- 您的手機型號和 Android 版本
