package jp.aknot.retromo.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import jp.aknot.retromo.BuildConfig;
import jp.aknot.retromo.R;
import jp.aknot.retromo.data.OrmaDatabase;
import jp.aknot.retromo.data.Prefecture;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Prefecture>> {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView drawerMenu = (ListView) findViewById(R.id.left_drawer_list_view);

        // TODO: 外部リソースに抽出する
        String[] itemList = new String[]{"都道府県一覧", "写真ギャラリー", "マップ"};
        drawerMenu.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, itemList));

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
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
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

    private static class PrefectureLoadTask extends AbstractAsyncTaskLoader<List<Prefecture>> {

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
    }
}
