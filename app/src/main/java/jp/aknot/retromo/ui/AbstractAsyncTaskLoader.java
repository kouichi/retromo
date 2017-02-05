package jp.aknot.retromo.ui;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import timber.log.Timber;


public abstract class AbstractAsyncTaskLoader<D> extends AsyncTaskLoader<D> {

    private boolean isRunning = false;

    public AbstractAsyncTaskLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        Timber.v("onStartLoading:");
        if (!isRunning || takeContentChanged()) {
            forceLoad();
        }
    }

    @Override
    protected void onForceLoad() {
        Timber.v("onForceLoad:");
        super.onForceLoad();
        this.isRunning = true;
    }
}
