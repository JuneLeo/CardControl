package com.poseidon.control.control.helper;

import android.os.Bundle;

/**
 * Created by spf on 2018/11/16.
 */
public interface ILifecycle {

    void onCreate(Bundle bundle);

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onDestroy();
}
