package com.graha.purchasingapps.global;

import org.json.JSONException;

public interface EventCompleted {
    void onTaskCompleted(Config vConfig) throws JSONException;
}