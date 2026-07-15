package com.taoyuan.orderassistant;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

public class FloatingOverlayService extends Service {

    private WindowManager windowManager;
    private View overlayView;
    private String senderName = "";
    private String keyword = "";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            senderName = intent.getStringExtra("sender");
            keyword = intent.getStringExtra("keyword");
        }

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        showOverlay();

        return START_STICKY;
    }

    private void showOverlay() {
        overlayView = new LinearLayout(this);
        overlayView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));

        // 創建三個可點擊區域
        LinearLayout container = (LinearLayout) overlayView;
        container.setOrientation(LinearLayout.HORIZONTAL);

        // 左區域
        View leftArea = createClickableArea("10", () -> sendMessage("@" + senderName + keyword + "10"));
        container.addView(leftArea, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));

        // 中區域
        View middleArea = createClickableArea("準", () -> sendMessage("@" + senderName + keyword + "準"));
        container.addView(middleArea, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));

        // 右區域
        View rightArea = createClickableArea("15", () -> sendMessage("@" + senderName + keyword + "15"));
        container.addView(rightArea, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));

        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.type = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ?
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY :
                WindowManager.LayoutParams.TYPE_PHONE;
        params.format = PixelFormat.TRANSLUCENT;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = 100;
        params.gravity = Gravity.TOP;

        windowManager.addView(overlayView, params);

        // 3秒後自動隱藏
        overlayView.postDelayed(() -> {
            if (windowManager != null && overlayView != null) {
                windowManager.removeView(overlayView);
                stopSelf();
            }
        }, 3000);
    }

    private View createClickableArea(String label, Runnable onClickListener) {
        View area = new View(this);
        area.setBackgroundColor(0x33000000); // 半透明黑色
        area.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                onClickListener.run();
                if (windowManager != null && overlayView != null) {
                    windowManager.removeView(overlayView);
                }
                stopSelf();
            }
            return true;
        });
        return area;
    }

    private void sendMessage(String message) {
        Toast.makeText(this, "發送: " + message, Toast.LENGTH_SHORT).show();
        // 實際的訊息發送邏輯將在後續實現
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (windowManager != null && overlayView != null) {
            try {
                windowManager.removeView(overlayView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
