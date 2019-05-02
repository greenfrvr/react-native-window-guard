package com.example;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.facebook.react.ReactActivity;

public class MainActivity extends ReactActivity {

    /**
     * Returns the name of the main component registered from JavaScript.
     * This is used to schedule rendering of the component.
     */
    @Override
    protected String getMainComponentName() {
        return "example";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window w = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            w.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//            w.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            w.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        View decorView = getWindow().getDecorView();
        int uiOptions = 0;
        uiOptions |= View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        uiOptions |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        uiOptions |= View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            uiOptions |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            uiOptions |= View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
        }

//
//        uiOptions |= View.SYSTEM_UI_FLAG_FULLSCREEN;
//        uiOptions |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
//        uiOptions |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        decorView.setSystemUiVisibility(uiOptions);

    }
}
