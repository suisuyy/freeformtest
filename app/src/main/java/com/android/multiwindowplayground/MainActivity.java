/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.multiwindowplayground;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.multiwindowplayground.activities.AdjacentActivity;
import com.android.multiwindowplayground.activities.BasicActivity;
import com.android.multiwindowplayground.activities.CustomConfigurationChangeActivity;
import com.android.multiwindowplayground.activities.LaunchBoundsActivity;
import com.android.multiwindowplayground.activities.LoggingActivity;
import com.android.multiwindowplayground.activities.MinimumSizeActivity;
import com.android.multiwindowplayground.activities.UnresizableActivity;

public class MainActivity extends LoggingActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View multiDisabledMessage = findViewById(R.id.warning_multiwindow_disabled);
        // Display an additional message if the app is not in multiwindow mode.
        if (!isInMultiWindowMode()) {
            multiDisabledMessage.setVisibility(View.VISIBLE);
        } else {
            multiDisabledMessage.setVisibility(View.GONE);
        }
    }

    public void onStartUnresizableClick(View view) {
        Log.d(mLogTag, "** starting UnresizableActivity");

        /*
         * This activity is marked as 'unresizable' in the AndroidManifest. We need to specify the
         * FLAG_ACTIVITY_NEW_TASK flag here to launch it into a new task stack, otherwise the
         * properties from the root activity would have been inherited (which was here marked as
         * resizable by default).
        */
        Intent intent = new Intent(this, UnresizableActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void onStartMinimumSizeActivity(View view) {
        Log.d(mLogTag, "** starting MinimumSizeActivity");

        startActivity(new Intent(this, MinimumSizeActivity.class));
    }

    public void onStartAdjacentActivity(View view) {
        Log.d(mLogTag, "** starting AdjacentActivity");

        /*
         * Start this activity adjacent to the focused activity (ie. this activity) if possible.
         * Note that this flag is just a hint to the system and may be ignored. For example,
         * if the activity is launched within the same task, it will be launched on top of the
         * previous activity that started the Intent. That's why the Intent.FLAG_ACTIVITY_NEW_TASK
         * flag is specified here in the intent - this will start the activity in a new task.
         */
        Intent intent = new Intent(this, AdjacentActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void onStartLaunchBoundsActivity(View view) {
        Log.d(mLogTag, "** starting LaunchBoundsActivity");

        // Define the bounds in which the Activity will be launched into.
        Rect bounds = new Rect(500, 300, 100, 0);

        // Set the bounds as an activity option.
        ActivityOptions options = ActivityOptions.makeBasic();
        options.setLaunchBounds(bounds);

        // Start the LaunchBoundsActivity with the specified options
        Intent intent = new Intent(this, LaunchBoundsActivity.class);
        startActivity(intent, options.toBundle());

    }

    public void onStartBasicActivity(View view) {
        Log.d(mLogTag, "** starting BasicActivity");

        // Start an Activity with the default options in the 'singleTask' launch mode as defined in
        // the AndroidManifest.xml.
       // startActivity(new Intent(this, BasicActivity.class));
        FreeformLauncher.test(this,new Intent(this, BasicActivity.class));
    }

    public void onStartCustomConfigurationActivity(View view) {
        Log.d(mLogTag, "** starting CustomConfigurationChangeActivity");

        // Start an Activity that handles all configuration changes itself.
        startActivity(new Intent(this, CustomConfigurationChangeActivity.class));

    }
}




 class FreeformLauncher {

    // Function to launch an app in freeform window mode using an Intent
    public static void launchAppInFreeformWindow(Context context, Intent intent, int left, int top, int width, int height) {
        // Set the bounds for the freeform window
        Rect bounds = new Rect(left, top, left + width, top + height);

        // Get the freeform window mode id based on the Android version
        int freeformWindowModeId = getFreeformWindowModeId();

        // Create an ActivityOptions instance
        ActivityOptions options = ActivityOptions.makeBasic();

        // Set the freeform window mode and bounds using reflection
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                // Set the windowing mode to freeform
                setActivityOptionMethod(options, "setLaunchWindowingMode", freeformWindowModeId);

                // Set the window bounds for the freeform window
                options.setLaunchBounds(bounds);
            }
        } catch (Exception e) {
            // Handle exceptions if reflection fails
            e.printStackTrace();
        }

        // Start the activity with the options bundle
        context.startActivity(intent, options.toBundle());
    }

    private static int getFreeformWindowModeId() {
        // Define the constants for windowing modes (taken from hidden API values)
        final int WINDOWING_MODE_FREEFORM = 5;

        // Return the freeform window mode id
        return WINDOWING_MODE_FREEFORM;
    }

    private static void setActivityOptionMethod(ActivityOptions options, String methodName, int value) throws Exception {
        // Use reflection to call a method on ActivityOptions to set the windowing mode
        java.lang.reflect.Method method = ActivityOptions.class.getMethod(methodName, int.class);
        method.invoke(options, value);
    }

    public static void test(Context context, Intent intent){
        //Intent intent = new Intent(context, YourActivity.class);
        int left = 50; // x position
        int top = 200; // y position
        int width = 500; // width of the freeform window
        int height = 300; // height of the freeform window
        FreeformLauncher.launchAppInFreeformWindow(context, intent, left, top, width, height);
    }
}


