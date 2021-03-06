package jp.aknot.retromo;

import android.app.Application;

import com.facebook.stetho.Stetho;

import timber.log.Timber;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new MyTree());
        }
        Stetho.initializeWithDefaults(this);
    }
}
