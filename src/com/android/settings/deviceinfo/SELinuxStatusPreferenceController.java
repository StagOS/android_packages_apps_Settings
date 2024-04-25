/*
 * Copyright (C) 2017 The Android Open Source Project
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
package com.android.settings.deviceinfo;

import android.content.Context;
import android.os.SELinux;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.Log;

import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;

import java.io.BufferedReader;
import java.io.StringBufferInputStream;
import java.io.InputStreamReader;
import java.lang.Runtime;

import com.android.settings.R;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.core.AbstractPreferenceController;

public class SELinuxStatusPreferenceController extends AbstractPreferenceController implements
        PreferenceControllerMixin {

    private static final String KEY_SELINUX_STATUS = "selinux_status";
    private static final String TAG = "SelinuxStatusCtrl";

    public SELinuxStatusPreferenceController(Context context) {
        super(context);
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public String getPreferenceKey() {
        return KEY_SELINUX_STATUS;
    }
    	
    public boolean isSeLinuxEnforcing() {
		StringBuffer output = new StringBuffer();
		Process p;
		try {
			p = Runtime.getRuntime().exec("getenforce");
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = "";
			while ((line = reader.readLine())!= null) {
				output.append(line);
			}
		} catch (Exception e) {
			Log.e(TAG, "OS does not support getenforce");
			// If getenforce is not available to the device, assume the device is not enforcing
			e.printStackTrace();
			return false;
		}
		String response = output.toString();
		if ("Enforcing".equals(response)) {
			return true;
		} else if ("Permissive".equals(response)) {
			return false;
		} else {
			Log.e(TAG, "getenforce returned unexpected value, unable to determine selinux!");
			// If getenforce is modified on this device, assume the device is not enforcing
			return false;
		}
	}

    @Override
    public void displayPreference(PreferenceScreen screen) {
        super.displayPreference(screen);
        final Preference pref = screen.findPreference(KEY_SELINUX_STATUS);
        if (pref == null) {
            return;
        }
        if (!SELinux.isSELinuxEnabled()) {
            String status = mContext.getResources().getString(R.string.selinux_status_disabled);
            pref.setSummary(status);
        } else if (isSeLinuxEnforcing()) {
            String status = mContext.getResources().getString(R.string.selinux_status_enforcing);
            pref.setSummary(status);
        } else {
            String status = mContext.getResources().getString(R.string.selinux_status_permissive);
            pref.setSummary(status);
        }
    }
}

