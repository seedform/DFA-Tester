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

package com.seedform.dfatester.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

/**
 * Provides several useful methods for simplifying commonly used tasks.
 * 
 * @author Shudmanul Chowdhury
 */
public final class Tool {

    private static Toast mToast;
    private static Context sAppContext;

    /**
     * Hides the on-screen keyboard.
     * 
     * @param context The application context.
     */
    public static void hideSoftKeyboard(Context context) {
        InputMethodManager inputManager = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        View focus = ((Activity) context).getCurrentFocus();
        if (focus != null) {
            inputManager.hideSoftInputFromWindow(focus.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * Create a <tt>Toast</tt> that will cancel itself if another
     * <tt>Toast</tt> is created by invoking this method.
     * 
     * @param context The context to use.
     * @param message The text to show.
     * @param duration The duration of the message. Either Toast.LENGTH_SHORT or
     *            Toast.LENGTH_LONG.
     */
    public static void createToast(Context context, String message, int duration) {
        if (mToast != null) mToast.cancel();
        mToast = Toast.makeText(context, message, duration);
        mToast.show();
    }

    /**
     * @param context The context to use.
     * @param stringResId The text to show, given as a string resource id.
     * @param duration The duration of the message. Either Toast.LENGTH_SHORT or
     *            Toast.LENGTH_LONG.
     * @see {@link #createToast(Context, String, int)}
     */
    public static void createToast(Context context, int stringResId, int duration) {
        String message = context.getResources().getString(stringResId);
        createToast(context, message, duration);
    }

    /**
     * Get information on the current device within a formatted string, suited
     * for using in bug reports, displaying details, etc.
     * 
     * @return The formatted string with information about the current device.
     */
    public static String getSystemInfoAsFormattedString() {
        return "\nManufacturer: "   + android.os.Build.MANUFACTURER
                + "\nModel: "       + android.os.Build.MODEL
                + "\nDevice: "      + android.os.Build.DEVICE
                + "\nRelease: "     + android.os.Build.VERSION.RELEASE
                + "\nId: "          + android.os.Build.ID
                + "\nBrand: "       + android.os.Build.BRAND
                + "\nCPU ABI: "     + android.os.Build.CPU_ABI
                + "\nCPU ABI2: "    + android.os.Build.CPU_ABI2
                + "\nHardware: "    + android.os.Build.HARDWARE;
    }

    /**
     * Return the context of the single, global Application object of the
     * current process. Use for data storage/retrieval purposes only.
     * 
     * @return The application context.
     */
    public static Context getAppContext() {
        return sAppContext;
    }

    /**
     * Stores the context of the global Application object so that it may be
     * accessed in a static manner. Use for data storage/retrieval purposes
     * only.
     * 
     * @param context The application context to be stored.
     */
    public static void setAppContext(Context context) {
        sAppContext = context.getApplicationContext();
    }

}
