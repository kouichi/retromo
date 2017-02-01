package jp.aknot.retromo;

import android.os.Looper;

public class Utils {
    private Utils() {
        throw new AssertionError("No instances.");
    }

    public static boolean isUiThread() {
        return Thread.currentThread().equals(Looper.getMainLooper().getThread());
    }
}
