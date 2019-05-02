package com.example;

import android.os.Build;
import android.view.View;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import javax.annotation.Nonnull;

import static android.view.View.SYSTEM_UI_FLAG_FULLSCREEN;
import static android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
import static android.view.View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
import static android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;

public class UIVisibilityModule extends ReactContextBaseJavaModule {

    public UIVisibilityModule(@Nonnull ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Nonnull
    @Override
    public String getName() {
        return "UIVisibility";
    }

    @ReactMethod
    public void changeStatusBarVisibility(boolean visible) {
        if (getCurrentActivity() == null) {
            return;
        }

        View decorView = getCurrentActivity().getWindow().getDecorView();
        decorView.post(() -> applyUIOption(decorView, SYSTEM_UI_FLAG_FULLSCREEN, visible));
    }

    @ReactMethod
    public void changeNavigationBarVisibility(boolean visible) {
        if (getCurrentActivity() == null) {
            return;
        }

        View decorView = getCurrentActivity().getWindow().getDecorView();
        decorView.post(() -> applyUIOption(decorView, SYSTEM_UI_FLAG_HIDE_NAVIGATION, visible));
    }

    private void applyUIOption(View decorView, int flag, boolean visible) {
        int uiOptions = baseUIOptions();

        if (!visible) {
            uiOptions |= flag;
        }

        decorView.setSystemUiVisibility(uiOptions);
    }

    private int baseUIOptions() {
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            uiOptions |= SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            uiOptions |= SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
        }

        return uiOptions;
    }
}
