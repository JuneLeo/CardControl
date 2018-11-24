package com.poseidon.control.event;

import android.text.TextUtils;

/**
 * Created by spf on 2018/11/24.
 */
public class Event {
    public String group;
    public String key;
    public Object data;
    public boolean isUiThread;


    public Event(String group, String key, Object data) {
        this.group = group;
        this.key = key;
        this.data = data;
    }

    public static Event create(String group, String key, Object data) {
        return new Event(group, key, data);
    }

    public static Event createUI(String group, String key, Object data) {
        Event event = new Event(group, key, data);
        event.isUiThread = true;
        return event;
    }

    public boolean checkNull() {
        if (TextUtils.isEmpty(group) || TextUtils.isEmpty(key)) {
            return true;
        }
        return false;
    }
}
