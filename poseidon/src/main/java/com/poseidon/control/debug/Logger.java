package com.poseidon.control.debug;
import android.util.Log;

public final class Logger {

    private static final String TAG = "ripper";
    private static final int STACK_COUNT = 3;

    private Logger() {
    }

    public static void d(String str) {
        if (DebugOption.isDebug()) {
            Log.d(TAG, str);
        }
    }

    public static void d(String str, Object obj) {
        if (DebugOption.isDebug()) {
            String log = merge(str, obj);
            Log.e(TAG, "merge finished");
            Log.d(TAG, log);
        }
    }

    public static void i(String str) {
        if (DebugOption.isDebug()) {
            Log.i(TAG, str);
        }
    }

    public static void i(String str, Object obj) {
        if (DebugOption.isDebug()) {
            String log = merge(str, obj);
            Log.e(TAG, "merge finished");
            Log.i(TAG, log);
        }
    }

    public static void w(String str) {
        if (DebugOption.isDebug()) {
            Log.w(TAG, str);
        }
    }

    public static void w(String str, Object obj) {
        if (DebugOption.isDebug()) {
            String log = merge(str, obj);
            Log.e(TAG, "merge finished");
            Log.w(TAG, log);
        }
    }

    public static void e(String str) {
        if (DebugOption.isDebug()) {
            Log.e(TAG, str);
        }
    }

    public static void e(String str, Object obj) {
        if (DebugOption.isDebug()) {
            String log = merge(str, obj);
            Log.e(TAG, "merge finished");
            Log.e(TAG, log);
        }
    }

    public static void markStart() {
        if (DebugOption.isDebug()) {
            StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
            StackTraceElement element = stacktrace[3];
            String className = element.getClassName();
            String methodName = element.getMethodName();
            int lineNumber = element.getLineNumber();
            StringBuilder sb = new StringBuilder();
            sb.append("====================").
                    append(className).
                    append(" ").
                    append(methodName).
                    append(" line:").
                    append(lineNumber).
                    append(" start").
                    append("====================");
            Log.i(TAG, sb.toString());
        }
    }


    public static void markEnd() {
        if (DebugOption.isDebug()) {
            StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
            StackTraceElement element = stacktrace[3];
            String className = element.getClassName();
            String methodName = element.getMethodName();
            int lineNumber = element.getLineNumber();
            StringBuilder sb = new StringBuilder();
            sb.append("====================").
                    append(className).
                    append(" ").
                    append(methodName).
                    append(" line:").
                    append(lineNumber).
                    append(" end").
                    append("====================");
            Log.i(TAG, sb.toString());
        }
    }

    private static String merge(String str, Object obj) {
        if (obj != null) {
            return "str=" + str + "&" + "obj=" + obj.toString();
        }else{
            return "str=" + str + "&" + "obj is null";
        }
    }
}