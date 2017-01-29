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

@RunWith(AndroidJUnit4.class)
public class PrefectureTest {

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

    private static final long currentTimeMillis = System.currentTimeMillis();

    private static final Prefecture[] PREFECTURES = new Prefecture[]{
            // 北海道
            new Prefecture(1, "01", "北海道", currentTimeMillis),
            // 東北
            new Prefecture(2, "02", "青森県", currentTimeMillis),
            new Prefecture(3, "03", "岩手県", currentTimeMillis),
            new Prefecture(4, "04", "宮城県", currentTimeMillis),
            new Prefecture(5, "05", "秋田県", currentTimeMillis),
            new Prefecture(6, "06", "山形県", currentTimeMillis),
            new Prefecture(7, "07", "福島県", currentTimeMillis),
            // 関東
            new Prefecture(8, "08", "茨城県", currentTimeMillis),
            new Prefecture(9, "09", "栃木県", currentTimeMillis),
            new Prefecture(10, "10", "群馬県", currentTimeMillis),
            new Prefecture(11, "11", "埼玉県", currentTimeMillis),
            new Prefecture(12, "12", "千葉県", currentTimeMillis),
            new Prefecture(13, "13", "東京都", currentTimeMillis),
            new Prefecture(14, "14", "神奈川県", currentTimeMillis),
            // 中部
            new Prefecture(15, "15", "新潟県", currentTimeMillis),
            new Prefecture(16, "16", "富山県", currentTimeMillis),
            new Prefecture(17, "17", "石川県", currentTimeMillis),
            new Prefecture(18, "18", "福井県", currentTimeMillis),
            new Prefecture(19, "19", "山梨県", currentTimeMillis),
            new Prefecture(20, "20", "長野県", currentTimeMillis),
            new Prefecture(21, "21", "岐阜県", currentTimeMillis),
            new Prefecture(22, "22", "静岡県", currentTimeMillis),
            new Prefecture(23, "23", "愛知県", currentTimeMillis),
            // 近畿
            new Prefecture(24, "24", "三重県", currentTimeMillis),
            new Prefecture(25, "25", "滋賀県", currentTimeMillis),
            new Prefecture(26, "26", "京都府", currentTimeMillis),
            new Prefecture(27, "27", "大阪府", currentTimeMillis),
            new Prefecture(28, "28", "兵庫県", currentTimeMillis),
            new Prefecture(29, "29", "奈良県", currentTimeMillis),
            new Prefecture(30, "30", "和歌山県", currentTimeMillis),
            // 中国
            new Prefecture(31, "31", "鳥取県", currentTimeMillis),
            new Prefecture(32, "32", "島根県", currentTimeMillis),
            new Prefecture(33, "33", "岡山県", currentTimeMillis),
            new Prefecture(34, "34", "広島県", currentTimeMillis),
            new Prefecture(35, "35", "山口県", currentTimeMillis),
            // 四国
            new Prefecture(36, "36", "徳島県", currentTimeMillis),
            new Prefecture(37, "37", "香川県", currentTimeMillis),
            new Prefecture(38, "38", "愛媛県", currentTimeMillis),
            new Prefecture(39, "39", "高知県", currentTimeMillis),
            // 九州
            new Prefecture(40, "40", "福岡県", currentTimeMillis),
            new Prefecture(41, "41", "佐賀県", currentTimeMillis),
            new Prefecture(42, "42", "長崎県", currentTimeMillis),
            new Prefecture(43, "43", "熊本県", currentTimeMillis),
            new Prefecture(44, "44", "大分県", currentTimeMillis),
            new Prefecture(45, "45", "宮崎県", currentTimeMillis),
            new Prefecture(46, "46", "鹿児島県", currentTimeMillis),
            new Prefecture(47, "47", "沖縄県", currentTimeMillis)
    };

    @Test
    public void insert() throws Exception {
        long rowId = orma.insertIntoPrefecture(PREFECTURES[0]);
        assertThat(rowId, is(greaterThan(0L)));
    }

    @Test
    public void insert_withTransaction() throws Exception {
        orma.transactionSync(() -> {
            Inserter<Prefecture> inserter = orma.prepareInsertIntoPrefecture();
            long rowId = inserter.execute(PREFECTURES[0]);
            assertThat(rowId, is(greaterThan(0L)));
        });
    }

    @Test
    public void insertAll_withTransaction() throws Exception {
        orma.transactionSync(() -> {
            Inserter<Prefecture> inserter = orma.prepareInsertIntoPrefecture();
            inserter.executeAll(Arrays.asList(PREFECTURES));
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
                    .execute();
            assertThat(cursor.getCount(), is(1));
            assertThat(cursor.getColumnCount(), is(4));
            if (cursor.moveToFirst()) {
                do {
                    assertThat(cursor.getString(cursor.getColumnIndex("code")), is("34"));
                    assertThat(cursor.getString(cursor.getColumnIndex("name")), is("広島県"));
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
                    .executeWithColumns("max(length(name)) as max_length_name");
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
