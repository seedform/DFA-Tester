/***************************************************************************\
 * Copyright 2013 Shudmanul Chowdhury
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
\***************************************************************************/

package com.seedform.dfatester.app;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.seedform.dfatester.R;
import com.seedform.dfatester.util.Data;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public abstract class BaseActivity extends ActionBarActivity {

    private static final String EXTRAS_KEY_TRANSLUCENT_NAVIGATION = "tn";

    private static boolean translucentNav = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        translucentNav = getIntent()
                .getBooleanExtra(EXTRAS_KEY_TRANSLUCENT_NAVIGATION, true);

        // Enable the transparent navigation bar if running 4.4 or higher.
        if (translucentNav && Build.VERSION.SDK_INT >= 19) {
            enableTranslucentNavAndStatus();
        }
    }

    @TargetApi(19)
    private void enableTranslucentNavAndStatus() {
        getWindow().addFlags(android.view.WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getWindow().addFlags(android.view.WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // enable status bar tint
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setTintColor(getResources().getColor(R.color.app_red_bg));
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.enter_slide_right, R.anim.exit_slide_right);
    }

    @Override
    public void onPause() {
        Data.saveData();
        super.onPause();
    }

    /**
     * Opens the specified <tt>Activity</tt>.
     * 
     * @param activity The class object of the target <tt>Activity</tt>.
     * @param enableTranslucentNav Set to <tt>true</tt> to enable translucent
     *            navigation in the target <tt>Activity</tt>.
     * @param forResult Set to <tt>true</tt> to execute
     *            <tt>startActivityForResult()</tt>, <tt>false</tt> otherwise.
     * @param requestCode If >= 0, this code will be returned in
     *            onActivityResult() when the activity exits.
     */
    public void openActivity(Class<? extends Activity> activity, boolean enableTranslucentNav,
            int... requestCode) {

        Intent intent = new Intent(this, activity);

        if (!enableTranslucentNav) {
            intent.putExtra(EXTRAS_KEY_TRANSLUCENT_NAVIGATION, false);
        }

        if (requestCode.length > 0) {
            startActivityForResult(intent, requestCode[0]);
        } else {
            startActivity(intent);
        }

        overridePendingTransition(R.anim.enter_slide_left, R.anim.exit_slide_left);
    }

}
