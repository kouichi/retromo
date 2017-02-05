package jp.aknot.retromo.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;

import com.github.gfx.android.orma.Inserter;

import java.util.Arrays;

import jp.aknot.retromo.BuildConfig;
import jp.aknot.retromo.R;
import jp.aknot.retromo.data.OrmaDatabase;
import jp.aknot.retromo.data.Prefecture;
import jp.aknot.retromo.data.Region;
import timber.log.Timber;

public class SplashActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Void> {

    private static final int REQ_START_ACTIVITY_MAIN_ID = 1;

    private static final int LOADER_ID = 1;

    private static final int START_ACTIVITY_DELAY_MILLIS = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        if (shouldPrepareInitialData()) {
            getSupportLoaderManager().initLoader(LOADER_ID, null, this);
        } else {
            startMainActivity();
        }
    }

    private boolean shouldPrepareInitialData = true;

    private boolean shouldPrepareInitialData() {
        return shouldPrepareInitialData;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQ_START_ACTIVITY_MAIN_ID:
                switch (resultCode) {
                    case RESULT_CANCELED:
                        finish();
                        break;
                    case RESULT_OK:
                    default:
                        break;
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    @Override
    protected void onResume() {
        Timber.d("onResume");
        super.onResume();
        if (!shouldPrepareInitialData()) {
            startMainActivity();
        }
    }

    private static final Region[] REGIONS = new Region[]{
            new Region("01", "北海道地方"),
            new Region("02", "東北地方"),
            new Region("03", "関東地方"),
            new Region("04", "中部地方"),
            new Region("05", "近畿地方"),
            new Region("06", "中国地方"),
            new Region("07", "四国地方"),
            new Region("08", "九州地方"),
    };

    private static final Prefecture[] PREFECTURES = new Prefecture[]{
            // 北海道
            new Prefecture("01", "北海道", REGIONS[0]),
            // 東北
            new Prefecture("02", "青森県", REGIONS[1]),
            new Prefecture("03", "岩手県", REGIONS[1]),
            new Prefecture("04", "宮城県", REGIONS[1]),
            new Prefecture("05", "秋田県", REGIONS[1]),
            new Prefecture("06", "山形県", REGIONS[1]),
            new Prefecture("07", "福島県", REGIONS[1]),
            // 関東
            new Prefecture("08", "茨城県", REGIONS[2]),
            new Prefecture("09", "栃木県", REGIONS[2]),
            new Prefecture("10", "群馬県", REGIONS[2]),
            new Prefecture("11", "埼玉県", REGIONS[2]),
            new Prefecture("12", "千葉県", REGIONS[2]),
            new Prefecture("13", "東京都", REGIONS[2]),
            new Prefecture("14", "神奈川県", REGIONS[2]),
            // 中部
            new Prefecture("15", "新潟県", REGIONS[3]),
            new Prefecture("16", "富山県", REGIONS[3]),
            new Prefecture("17", "石川県", REGIONS[3]),
            new Prefecture("18", "福井県", REGIONS[3]),
            new Prefecture("19", "山梨県", REGIONS[3]),
            new Prefecture("20", "長野県", REGIONS[3]),
            new Prefecture("21", "岐阜県", REGIONS[3]),
            new Prefecture("22", "静岡県", REGIONS[3]),
            new Prefecture("23", "愛知県", REGIONS[3]),
            // 近畿
            new Prefecture("24", "三重県", REGIONS[4]),
            new Prefecture("25", "滋賀県", REGIONS[4]),
            new Prefecture("26", "京都府", REGIONS[4]),
            new Prefecture("27", "大阪府", REGIONS[4]),
            new Prefecture("28", "兵庫県", REGIONS[4]),
            new Prefecture("29", "奈良県", REGIONS[4]),
            new Prefecture("30", "和歌山県", REGIONS[4]),
            // 中国
            new Prefecture("31", "鳥取県", REGIONS[5]),
            new Prefecture("32", "島根県", REGIONS[5]),
            new Prefecture("33", "岡山県", REGIONS[5]),
            new Prefecture("34", "広島県", REGIONS[5]),
            new Prefecture("35", "山口県", REGIONS[5]),
            // 四国
            new Prefecture("36", "徳島県", REGIONS[6]),
            new Prefecture("37", "香川県", REGIONS[6]),
            new Prefecture("38", "愛媛県", REGIONS[6]),
            new Prefecture("39", "高知県", REGIONS[6]),
            // 九州
            new Prefecture("40", "福岡県", REGIONS[7]),
            new Prefecture("41", "佐賀県", REGIONS[7]),
            new Prefecture("42", "長崎県", REGIONS[7]),
            new Prefecture("43", "熊本県", REGIONS[7]),
            new Prefecture("44", "大分県", REGIONS[7]),
            new Prefecture("45", "宮崎県", REGIONS[7]),
            new Prefecture("46", "鹿児島県", REGIONS[7]),
            new Prefecture("47", "沖縄県", REGIONS[7])
    };

    private static class PrepareInitialDataTaskLoader extends AsyncTaskLoader<Void> {

        private boolean isRunning = false;

        public PrepareInitialDataTaskLoader(Context context) {
            super(context);
        }

        @Override
        public Void loadInBackground() {
            Timber.v("loadInBackground:");
            // TODO: Modelクラスへ実装を移動する
            OrmaDatabase orma = OrmaDatabase.builder(getContext())
                    .trace(BuildConfig.DEBUG)
                    .build();
            orma.transactionSync(() -> {
                Inserter<Region> regionInserter = orma.prepareInsertIntoRegion();
                regionInserter.executeAll(Arrays.asList(REGIONS));

                Inserter<Prefecture> prefectureInserter = orma.prepareInsertIntoPrefecture();
                prefectureInserter.executeAll(Arrays.asList(PREFECTURES));
            });
            return null;
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

    @Override
    public Loader<Void> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LOADER_ID:
                Timber.v("onCreateLoader:");
                return new PrepareInitialDataTaskLoader(this);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Void> loader, Void data) {
        Timber.v("onLoadFinished:");
        this.shouldPrepareInitialData = false;
        new Handler().postDelayed(this::startMainActivity, START_ACTIVITY_DELAY_MILLIS);
    }

    @Override
    public void onLoaderReset(Loader<Void> loader) {
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivityForResult(intent, REQ_START_ACTIVITY_MAIN_ID);
    }
}
