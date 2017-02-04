package jp.aknot.retromo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private static final int REQ_START_ACTIVITY_MAIN_ID = 1;

    private static final int START_ACTIVITY_DELAY_MILLIS = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(this::startMainActivity, START_ACTIVITY_DELAY_MILLIS);
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

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivityForResult(intent, REQ_START_ACTIVITY_MAIN_ID);
    }
}
