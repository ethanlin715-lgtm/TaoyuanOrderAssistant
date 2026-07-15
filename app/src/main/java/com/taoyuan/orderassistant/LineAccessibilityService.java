package com.taoyuan.orderassistant;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LineAccessibilityService extends AccessibilityService {

    private SharedPreferences sharedPreferences;
    private Set<String> keywords;
    private String senderName = "";
    private String firstKeyword = "";

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences = getSharedPreferences("LineAssistant", MODE_PRIVATE);
        keywords = sharedPreferences.getStringSet("keywords", new HashSet<>());
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            // 檢測 Line 應用程式
            if (event.getPackageName() != null && event.getPackageName().toString().contains("line")) {
                processLineMessage(event);
            }
        }
    }

    private void processLineMessage(AccessibilityEvent event) {
        AccessibilityNodeInfo rootNode = getRootInActiveWindow();
        if (rootNode == null) return;

        // 遍歷所有文本節點
        findAndProcessMessages(rootNode);
    }

    private void findAndProcessMessages(AccessibilityNodeInfo node) {
        if (node == null) return;

        // 檢查當前節點是否包含文本
        CharSequence text = node.getText();
        if (text != null && text.length() > 0) {
            String messageText = text.toString();

            // 提取發送者名字和關鍵字
            extractSenderAndKeyword(messageText);

            // 如果找到關鍵字，顯示浮動覆蓋層
            if (!firstKeyword.isEmpty()) {
                showFloatingOverlay();
            }
        }

        // 遞歸檢查子節點
        for (int i = 0; i < node.getChildCount(); i++) {
            findAndProcessMessages(node.getChild(i));
        }
    }

    private void extractSenderAndKeyword(String messageText) {
        // 簡單的正則表達式來提取發送者名字
        Pattern senderPattern = Pattern.compile("^([\\w\\u4e00-\\u9fff]+)");
        Matcher senderMatcher = senderPattern.matcher(messageText);

        if (senderMatcher.find()) {
            senderName = senderMatcher.group(1);
        }

        // 檢查是否包含任何關鍵字
        for (String keyword : keywords) {
            if (messageText.contains(keyword)) {
                firstKeyword = keyword;
                break;
            }
        }
    }

    private void showFloatingOverlay() {
        Intent intent = new Intent(this, FloatingOverlayService.class);
        intent.putExtra("sender", senderName);
        intent.putExtra("keyword", firstKeyword);
        startService(intent);
    }

    @Override
    public void onInterrupt() {
        // 服務中斷時的處理
    }
}
