
package com.android.settings.deviceinfo;

import android.os.SystemProperties;

public class VersionUtils {
    public static String getstagVersion(){
        return SystemProperties.get("ro.stag.version","");
    }
}
