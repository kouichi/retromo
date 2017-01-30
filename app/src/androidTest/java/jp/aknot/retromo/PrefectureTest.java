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
import jp.aknot.retromo.data.Prefecture;
import jp.aknot.retromo.data.Prefecture_Selector;
import jp.aknot.retromo.data.Region;

@RunWith(AndroidJUnit4.class)
public class PrefectureTest {

    private OrmaDatabase orma;

    @Before
    public void setUp() throws Exception {
        orma = OrmaDatabase.builder(InstrumentationRegistry.getTargetContext())
                .trace(true)
                .build();

        orma.transactionSync(() -> {
            Inserter<Region> regionInserter = orma.prepareInsertIntoRegion();
            regionInserter.executeAll(Arrays.asList(REGIONS));
        });

    }

    @After
    public void tearDown() throws Exception {
        orma.deleteAll();
        orma = null;
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
            // 九州, REGIONS[6]
            new Prefecture("40", "福岡県", REGIONS[7]),
            new Prefecture("41", "佐賀県", REGIONS[7]),
            new Prefecture("42", "長崎県", REGIONS[7]),
            new Prefecture("43", "熊本県", REGIONS[7]),
            new Prefecture("44", "大分県", REGIONS[7]),
            new Prefecture("45", "宮崎県", REGIONS[7]),
            new Prefecture("46", "鹿児島県", REGIONS[7]),
            new Prefecture("47", "沖縄県", REGIONS[7])
    };

    @Test
    public void insert() throws Exception {
        orma.insertIntoRegion(REGIONS[0]);
        long rowId = orma.insertIntoPrefecture(PREFECTURES[0]);
        assertThat(rowId, is(greaterThan(0L)));
    }

    @Test
    public void insert_withTransaction() throws Exception {
        orma.transactionSync(() -> {
//            Inserter<Region> regionInserter = orma.prepareInsertIntoRegion();
//            regionInserter.execute(REGIONS[0]);

            Inserter<Prefecture> prefectureInserter = orma.prepareInsertIntoPrefecture();
            long rowId = prefectureInserter.execute(PREFECTURES[0]);
            assertThat(rowId, is(greaterThan(0L)));
        });
    }

    @Test
    public void insertAll_withTransaction() throws Exception {
        orma.transactionSync(() -> {
            Inserter<Prefecture> prefectureInserter = orma.prepareInsertIntoPrefecture();
            prefectureInserter.executeAll(Arrays.asList(PREFECTURES));
        });
    }

    @Test
    public void select_cursor() throws Exception {
        orma.transactionSync(() -> {
            Inserter<Prefecture> inserter = orma.prepareInsertIntoPrefecture();
            inserter.executeAll(Arrays.asList(PREFECTURES));
        });

        Cursor cursor = null;
        try {
            cursor = orma.selectFromPrefecture()
                    .codeEq("34")
                    .executeWithColumns("p1.code as code", "p1.name as name", "r2.code as region_code", "r2.name as region_name");
            assertThat(cursor.getCount(), is(1));
            assertThat(cursor.getColumnCount(), is(4));
            if (cursor.moveToFirst()) {
                do {
                    assertThat(cursor.getString(cursor.getColumnIndex("code")), is("34"));
                    assertThat(cursor.getString(cursor.getColumnIndex("name")), is("広島県"));
                    assertThat(cursor.getString(cursor.getColumnIndex("region_code")), is("06"));
                    assertThat(cursor.getString(cursor.getColumnIndex("region_name")), is("中国地方"));
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
            Inserter<Prefecture> inserter = orma.prepareInsertIntoPrefecture();
            inserter.executeAll(Arrays.asList(PREFECTURES));
        });

        Prefecture_Selector selector = orma.selectFromPrefecture()
                .orderByCodeAsc();

        long count = selector.count();
        for (int i = 0; i < count; i++) {
            assertThat(selector.get(i).code, is(PREFECTURES[i].code));
            assertThat(selector.get(i).name, is(PREFECTURES[i].name));
        }
    }

    @Test
    public void select_empty() throws Exception {
        Prefecture_Selector selector = orma.selectFromPrefecture()
                .codeEq("01");
        assertThat(selector.isEmpty(), is(true));
    }

    @Test
    public void count() throws Exception {
        orma.transactionSync(() -> {
            Inserter<Prefecture> inserter = orma.prepareInsertIntoPrefecture();
            inserter.executeAll(Arrays.asList(PREFECTURES));
        });

        int count = orma.selectFromPrefecture()
                .codeEq("34")
                .count();
        assertThat(count, is(1));
    }

    @Test
    public void select_max() throws Exception {
        orma.transactionSync(() -> {
            Inserter<Prefecture> inserter = orma.prepareInsertIntoPrefecture();
            inserter.executeAll(Arrays.asList(PREFECTURES));
        });

        Cursor cursor = null;
        try {
            cursor = orma.selectFromPrefecture()
                    .executeWithColumns("max(length(p1.name)) as max_length_name");
            assertThat(cursor.getCount(), is(1));
            assertThat(cursor.getColumnCount(), is(1));
            if (cursor.moveToFirst()) {
                do {
                    assertThat(cursor.getInt(cursor.getColumnIndex("max_length_name")), is(4));
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
        orma.insertIntoPrefecture(PREFECTURES[0]);

        long numberOfRowsAffected = orma.updatePrefecture()
                .codeEq("01")
                .name("北海道(更新)")
                .execute();
        assertThat(numberOfRowsAffected, is(1L));

        Prefecture_Selector selector = orma.selectFromPrefecture()
                .codeEq("01");

        Prefecture prefecture = selector.get(0);
        assertThat(prefecture.code, is("01"));
        assertThat(prefecture.name, is("北海道(更新)"));
    }

    @Test
    public void delete() throws Exception {
        orma.insertIntoPrefecture(PREFECTURES[0]);
        long numberOfRowsDeleted = orma.deleteFromPrefecture()
                .codeEq("01")
                .execute();
        assertThat(numberOfRowsDeleted, is(1L));
    }
}
