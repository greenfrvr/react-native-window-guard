package com.github.greenfrvr;

import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.StringDef;
import android.support.v4.view.DisplayCutoutCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;

import java.lang.annotation.Retention;

import javax.annotation.Nonnull;

import static android.view.View.SYSTEM_UI_FLAG_FULLSCREEN;
import static android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
import static android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN;
import static android.view.WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
import static android.view.WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
import static java.lang.annotation.RetentionPolicy.SOURCE;

public class RNWindowGuardModule extends ReactContextBaseJavaModule {

    private static final String TAG = RNWindowGuardModule.class.getSimpleName();

    public static final String MODULE_NAME = "RNWindowGuard";

    @Retention(SOURCE)
    @StringDef({LEFT, TOP, RIGHT, BOTTOM, NOTCH})
    public @interface WindowInsets {
    }

    public static final String LEFT = "leftInset";
    public static final String TOP = "topInset";
    public static final String RIGHT = "rightInset";
    public static final String BOTTOM = "bottomInset";
    public static final String NOTCH = "hasNotch";

    private float density;

    public RNWindowGuardModule(@Nonnull ReactApplicationContext reactContext) {
        super(reactContext);
        density = ((float) reactContext.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    @Nonnull
    @Override
    public String getName() {
        return MODULE_NAME;
    }

    //TODO: just for debug purposes
    @ReactMethod
    public void changeNavigationBarVisibility(boolean visibile) {
        int fullscreenLayout = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;

        Window window = getCurrentActivity().getWindow();
        View decorView = window.getDecorView();
        decorView.post(() -> {
            if (visibile) {
                decorView.setSystemUiVisibility(fullscreenLayout);
            } else {
                decorView.setSystemUiVisibility(fullscreenLayout | SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            }
        });
    }

    //TODO: just for debug purposes
    @ReactMethod
    public void changeStatusBarVisibility(boolean visibile) {
        int fullscreenLayout = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;

        Window window = getCurrentActivity().getWindow();
        View decorView = window.getDecorView();
        decorView.post(() -> {
            if (visibile) {
                decorView.setSystemUiVisibility(fullscreenLayout);
            } else {
                decorView.setSystemUiVisibility(fullscreenLayout | SYSTEM_UI_FLAG_FULLSCREEN);
            }
        });
    }

    @ReactMethod
    public void requestWindowInsets(Promise promise) {
        if (getCurrentActivity() == null || getCurrentActivity().getWindow() == null) {
            return;
        }

        Window window = getCurrentActivity().getWindow();
        View view = window.getDecorView().getRootView();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            android.view.WindowInsets insets = view.getRootWindowInsets();
//            if (insets != null) {
//                Log.i(TAG, "requestWindowInsets: " + insets.getSystemWindowInsetTop());
//            }
//        }

        ViewCompat.setOnApplyWindowInsetsListener(view, (v, insets) -> {
            int uiOptions = getCurrentActivity().getWindow().getDecorView().getSystemUiVisibility();
            int windowOptions = getCurrentActivity().getWindow().getAttributes().flags;
            WritableMap insetsMap = convertToWindowInsetsMap(insets, uiOptions, windowOptions);
            Log.i(TAG, "===== Apply Window Insets: " + insetsMap);
            printInsets(insets);
            promise.resolve(insetsMap);
            return insets.consumeSystemWindowInsets();
        });

        view.post(() -> ViewCompat.requestApplyInsets(view));
    }

    WritableMap convertToWindowInsetsMap(WindowInsetsCompat insets, int uiOptions, int windowOptions) {
        WritableMap map = createEmptyInsetsMap();

        applyStatusBarVisibility(map, insets, uiOptions, windowOptions);
        applyNavigationBarVisibility(map, insets, uiOptions, windowOptions);

        adjustWindowInsetsWithCutout(map, insets.getDisplayCutout());

        return map;
    }

    void applyStatusBarVisibility(WritableMap map, WindowInsetsCompat insets, int uiOptions, int windowOptions) {
        boolean isStatusBarHidden = (windowOptions & FLAG_FULLSCREEN) != 0 || (uiOptions & SYSTEM_UI_FLAG_FULLSCREEN) != 0;
        boolean isLayoutUnderStatusBar = (uiOptions & SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN) != 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            isLayoutUnderStatusBar = isLayoutUnderStatusBar || (windowOptions & FLAG_TRANSLUCENT_STATUS) != 0;
        }

        Log.d(TAG, "===== Is status bar hidden: " + isStatusBarHidden);
        Log.d(TAG, "===== Is content below status bar: " + isLayoutUnderStatusBar);

        if (isLayoutUnderStatusBar && !isStatusBarHidden) {
            map.putDouble(TOP, pxToDp(insets.getSystemWindowInsetTop()));
        }
    }

    void applyNavigationBarVisibility(WritableMap map, WindowInsetsCompat insets, int uiOptions, int windowOptions) {
        boolean isNavigationBarHidden = (uiOptions & SYSTEM_UI_FLAG_HIDE_NAVIGATION) != 0;
        boolean isLayoutUnderNavigationBar = (uiOptions & SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION) != 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            isLayoutUnderNavigationBar = isLayoutUnderNavigationBar || (windowOptions & FLAG_TRANSLUCENT_NAVIGATION) != 0;
        }

        Log.d(TAG, "=== Is navigation bar hidden: " + isNavigationBarHidden);
        Log.d(TAG, "=== Is content below navigation bar: " + isLayoutUnderNavigationBar);

        if (isLayoutUnderNavigationBar && !isNavigationBarHidden) {
            map.putDouble(LEFT, pxToDp(insets.getSystemWindowInsetLeft()));
            map.putDouble(BOTTOM, pxToDp(insets.getSystemWindowInsetBottom()));
            map.putDouble(RIGHT, pxToDp(insets.getSystemWindowInsetRight()));
        }
    }

    void adjustWindowInsetsWithCutout(WritableMap map, DisplayCutoutCompat displayCutout) {
        map.putBoolean(NOTCH, displayCutout != null);
        if (displayCutout != null) {
            if (map.getDouble(LEFT) == 0 && displayCutout.getSafeInsetLeft() != 0) {
                map.putDouble(LEFT, pxToDp(displayCutout.getSafeInsetLeft()));
            }

            if (map.getDouble(RIGHT) == 0 && displayCutout.getSafeInsetRight() != 0) {
                map.putDouble(RIGHT, pxToDp(displayCutout.getSafeInsetRight()));
            }

            if (map.getDouble(TOP) == 0 && displayCutout.getSafeInsetTop() > 0) {
                map.putDouble(TOP, pxToDp(displayCutout.getSafeInsetTop()));
            }

            if (map.getDouble(BOTTOM) == 0 && displayCutout.getSafeInsetBottom() != 0) {
                map.putDouble(BOTTOM, pxToDp(displayCutout.getSafeInsetBottom()));
            }

            printCutout(displayCutout);
        }
    }

    WritableMap createEmptyInsetsMap() {
        WritableMap map = Arguments.createMap();
        map.putDouble(LEFT, 0);
        map.putDouble(TOP, 0);
        map.putDouble(RIGHT, 0);
        map.putDouble(BOTTOM, 0);
        return map;
    }

    float pxToDp(float px) {
        return px / density;
    }

    private void printCutout(DisplayCutoutCompat cutout) {
        Log.i(TAG, "========= Cutout Insets:");
        Log.i(TAG, "=== Cutout Insets [top] " + cutout.getSafeInsetTop());
        Log.i(TAG, "=== Cutout Insets [bottom] " + cutout.getSafeInsetBottom());
        Log.i(TAG, "=== Cutout Insets [left] " + cutout.getSafeInsetLeft());
        Log.i(TAG, "=== Cutout Insets [right] " + cutout.getSafeInsetRight());

        Log.i(TAG, "=== Cutout rectangles: ");
        for (Rect rect : cutout.getBoundingRects()) {
            Log.i(TAG, rect.toString());
        }
        Log.i(TAG, "==================");
    }

    private void printInsets(WindowInsetsCompat inset) {
        Log.i(TAG, "========= Window Insets:");

        Log.i(TAG, "=== Stable Insets [top] " + inset.getStableInsetTop());
        Log.i(TAG, "=== Stable Insets [bottom] " + inset.getStableInsetBottom());
        Log.i(TAG, "=== Stable Insets [left] " + inset.getStableInsetLeft());
        Log.i(TAG, "=== Stable Insets [right] " + inset.getStableInsetRight());

        Log.i(TAG, "=== System Insets [top] " + inset.getSystemWindowInsetTop());
        Log.i(TAG, "=== System Insets [bottom] " + inset.getSystemWindowInsetBottom());
        Log.i(TAG, "=== System Insets [left] " + inset.getSystemWindowInsetLeft());
        Log.i(TAG, "=== System Insets [right] " + inset.getSystemWindowInsetRight());
        Log.i(TAG, "==================");
    }
}
