package jp.aknot.retromo;

import android.os.Process;

import timber.log.Timber;

public class MyTree extends Timber.DebugTree {
    @Override
    protected void log(int priority, String tag, String message, Throwable t) {
        String tagPrefix = "@";
        String messagePrefix = "tid[" + Process.myTid() + "|" + (Utils.isUiThread() ? "U" : "W") + "] ";
        super.log(priority, tagPrefix + tag, messagePrefix + message, t);
    }
}
