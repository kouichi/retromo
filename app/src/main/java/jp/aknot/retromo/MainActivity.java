package jp.aknot.retromo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

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

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        List<String> dataList = Arrays.asList(
                "green apple", "avocado", "apricot", "strawberry", "fig", "orange", "persimmon",
                "raspberry", "kiwi", "chestnut", "grapefruit", "coconut", "cherry", "watermelon",
                "durian", "pear", "pineapple", "banana", "papaya", "loquat", "grape", "plum",
                "blueberry", "muscat", "muskmelon", "mango", "mandarin orange", "melon", "peach",
                "yuzu", "lime", "raspberry", "apple", "lemon");
        MyAdapter adapter = new MyAdapter(dataList,
                (view, position) -> Toast.makeText(this, "Clicked: [" + position + "] " + dataList.get(position), Toast.LENGTH_SHORT).show());
        recyclerView.setAdapter(adapter);

        ItemDividerDecoration dividerDecoration = new ItemDividerDecoration(this);
        recyclerView.addItemDecoration(dividerDecoration);
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
}
