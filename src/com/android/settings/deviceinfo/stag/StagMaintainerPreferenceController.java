/*
 * Copyright (C) 2019 The LineageOS Project
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

package com.android.settings.deviceinfo.stag;

import android.content.Context;
import android.os.SystemProperties;
import android.content.res.Resources;
import android.text.TextUtils;

import androidx.preference.Preference;
import android.util.Log;

import com.android.settings.R;
import com.android.settings.core.BasePreferenceController;

import java.lang.Exception;

public class StagMaintainerPreferenceController extends BasePreferenceController {

    private static final String TAG = "StagMaintainerPreferenceController";
    private static final String KEY_STAG_BUILD_TYPE = "ro.stag.releasetype";
    private static final String KEY_STAG_DEVICE = "ro.stag.device";

    public StagMaintainerPreferenceController(Context context, String key) {
        super(context, key);
    }

    public int getAvailabilityStatus() {
        return AVAILABLE;
    }

    public CharSequence getSummary() {
         String build_type = SystemProperties.get(KEY_STAG_BUILD_TYPE,
                mContext.getString(R.string.stag_build_default));
         String device = SystemProperties.get(KEY_STAG_DEVICE,
                mContext.getString(R.string.stag_build_default));
	if (build_type.equals("OFFICIAL")){
		String rs = "device_" + device + "_maintainer";
		int resourceId = mContext.getResources().getIdentifier(rs, "string", mContext.getPackageName());
		try{
			String maintainer = mContext.getResources().getString(resourceId);
	                return maintainer;
		}catch(Exception e){
			Log.w(TAG, "Error finding maintainer name with id " + resourceId);
			return "Not Official Maintainer";
		}
	}
	return "Unofficial Maintainer";
    }
}
