package com.foodsaver.android.util;

import android.content.Context;
import com.cloudinary.android.MediaManager;
import java.util.HashMap;
import java.util.Map;

public class CloudinaryManager {

    private static boolean initialized = false;

    public static void init(Context context) {
        if (initialized) return;
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "dng4mgd2r");
        config.put("api_key",    "416181968564167");
        MediaManager.init(context, config);
        initialized = true;
    }
}