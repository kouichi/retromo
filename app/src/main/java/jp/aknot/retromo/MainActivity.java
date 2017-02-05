package jp.aknot.retromo;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import jp.aknot.retromo.data.OrmaDatabase;
import jp.aknot.retromo.data.Prefecture;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Prefecture>> {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        ItemDividerDecoration dividerDecoration = new ItemDividerDecoration(this);
        recyclerView.addItemDecoration(dividerDecoration);

        getSupportLoaderManager().initLoader(PREFECTURE_LOAD_ID, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private static final int PREFECTURE_LOAD_ID = 1;

    @Override
    public Loader<List<Prefecture>> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case PREFECTURE_LOAD_ID:
                return new PrefectureLoadTask(this);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<List<Prefecture>> loader, List<Prefecture> data) {
        PrefectureAdapter adapter = new PrefectureAdapter(data,
                (view, position) -> Toast.makeText(this, "Clicked: [" + position + "] " + data.get(position).name, Toast.LENGTH_SHORT).show());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onLoaderReset(Loader<List<Prefecture>> loader) {
    }

    private static class PrefectureLoadTask extends AsyncTaskLoader<List<Prefecture>> {

        private boolean isRunning = false;

        public PrefectureLoadTask(Context context) {
            super(context);
        }

        @Override
        public List<Prefecture> loadInBackground() {
            OrmaDatabase orma = OrmaDatabase.builder(getContext())
                    .trace(BuildConfig.DEBUG)
                    .build();
            return Prefecture.relation(orma).selector().toList();
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
}
