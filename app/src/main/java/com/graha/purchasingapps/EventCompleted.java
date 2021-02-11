package com.graha.purchasingapps;

import org.json.JSONException;

public interface EventCompleted {
    void onTaskCompleted(Config vConfig) throws JSONException;
}