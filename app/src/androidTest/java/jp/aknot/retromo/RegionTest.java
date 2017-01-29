package jp.aknot.retromo;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

import android.database.Cursor;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.github.gfx.android.orma.Inserter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;

import jp.aknot.retromo.data.OrmaDatabase;
import jp.aknot.retromo.data.Region;
import jp.aknot.retromo.data.Region_Selector;

@RunWith(AndroidJUnit4.class)
public class RegionTest {

    private OrmaDatabase orma;

    @Before
    public void setUp() throws Exception {
        orma = OrmaDatabase.builder(InstrumentationRegistry.getTargetContext())
                .trace(true)
                .build();
    }

    @After
    public void tearDown() throws Exception {
        orma.deleteAll();
        orma = null;
    }

    private static final Region[] REGIONS = new Region[]{
            new Region(1, "01", "北海道地方"),
            new Region(2, "02", "東北地方"),
            new Region(3, "03", "関東地方"),
            new Region(4, "04", "中部地方"),
            new Region(5, "05", "近畿地方"),
            new Region(6, "06", "中国地方"),
            new Region(7, "07", "四国地方"),
            new Region(8, "08", "九州地方"),
    };

    @Test
    public void insert() throws Exception {
        long rowId = orma.insertIntoRegion(REGIONS[0]);
        assertThat(rowId, is(greaterThan(0L)));
    }

    @Test
    public void insert_withTransaction() throws Exception {
        orma.transactionSync(() -> {
            Inserter<Region> inserter = orma.prepareInsertIntoRegion();
            long rowId = inserter.execute(REGIONS[0]);
            assertThat(rowId, is(greaterThan(0L)));
        });
    }

    @Test
    public void insertAll_withTransaction() throws Exception {
        orma.transactionSync(() -> {
            Inserter<Region> inserter = orma.prepareInsertIntoRegion();
            inserter.executeAll(Arrays.asList(REGIONS));
        });
    }

    @Test
    public void select_cursor() throws Exception {
        orma.transactionSync(() -> {
            Inserter<Region> inserter = orma.prepareInsertIntoRegion();
            inserter.executeAll(Arrays.asList(REGIONS));
        });

        Cursor cursor = null;
        try {
            cursor = orma.selectFromRegion()
                    .codeEq("03")
                    .execute();
            assertThat(cursor.getCount(), is(1));
            assertThat(cursor.getColumnCount(), is(3));
            if (cursor.moveToFirst()) {
                do {
                    assertThat(cursor.getString(cursor.getColumnIndex("code")), is("03"));
                    assertThat(cursor.getString(cursor.getColumnIndex("name")), is("関東地方"));
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Test
    public void select() throws Exception {
        orma.transactionSync(() -> {
            Inserter<Region> inserter = orma.prepareInsertIntoRegion();
            inserter.executeAll(Arrays.asList(REGIONS));
        });

        Region_Selector selector = orma.selectFromRegion()
                .orderByCodeAsc();

        long count = selector.count();
        for (int i = 0; i < count; i++) {
            assertThat(selector.get(i).code, is(REGIONS[i].code));
            assertThat(selector.get(i).name, is(REGIONS[i].name));
        }
    }

    @Test
    public void select_empty() throws Exception {
        Region_Selector selector = orma.selectFromRegion()
                .codeEq("01");
        assertThat(selector.isEmpty(), is(true));
    }

    @Test
    public void count() throws Exception {
        orma.transactionSync(() -> {
            Inserter<Region> inserter = orma.prepareInsertIntoRegion();
            inserter.executeAll(Arrays.asList(REGIONS));
        });

        int count = orma.selectFromRegion()
                .codeEq("03")
                .count();
        assertThat(count, is(1));
    }

    @Test
    public void select_max() throws Exception {
        orma.transactionSync(() -> {
            Inserter<Region> inserter = orma.prepareInsertIntoRegion();
            inserter.executeAll(Arrays.asList(REGIONS));
        });

        Cursor cursor = null;
        try {
            cursor = orma.selectFromRegion()
                    .executeWithColumns("max(length(name)) as max_length_name");
            assertThat(cursor.getCount(), is(1));
            assertThat(cursor.getColumnCount(), is(1));
            if (cursor.moveToFirst()) {
                do {
                    assertThat(cursor.getInt(cursor.getColumnIndex("max_length_name")), is(5));
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Test
    public void update() throws Exception {
        orma.insertIntoRegion(REGIONS[0]);

        long numberOfRowsAffected = orma.updateRegion()
                .codeEq("01")
                .name("北海道地方(更新)")
                .execute();
        assertThat(numberOfRowsAffected, is(1L));

        Region_Selector selector = orma.selectFromRegion()
                .codeEq("01");

        Region Region = selector.get(0);
        assertThat(Region.code, is("01"));
        assertThat(Region.name, is("北海道地方(更新)"));
    }

    @Test
    public void delete() throws Exception {
        orma.insertIntoRegion(REGIONS[0]);
        long numberOfRowsDeleted = orma.deleteFromRegion()
                .codeEq("01")
                .execute();
        assertThat(numberOfRowsDeleted, is(1L));
    }
}
