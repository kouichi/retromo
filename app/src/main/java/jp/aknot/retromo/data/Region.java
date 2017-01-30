package jp.aknot.retromo.data;

import android.support.annotation.NonNull;

import com.github.gfx.android.orma.annotation.Column;
import com.github.gfx.android.orma.annotation.OnConflict;
import com.github.gfx.android.orma.annotation.PrimaryKey;
import com.github.gfx.android.orma.annotation.Setter;
import com.github.gfx.android.orma.annotation.Table;

@Table
public class Region {

    @Setter
    public Region(String code, String name) {
        this.code = code;
        this.name = name;
    }

    @PrimaryKey(onConflict = OnConflict.IGNORE)
    public final String code;

    @Column(indexed = true, uniqueOnConflict = OnConflict.IGNORE)
    public final String name;

    @NonNull
    public Prefecture_Relation getPrefectures(OrmaDatabase orma) {
        return orma.relationOfPrefecture().regionEq(this);
    }

    public Prefecture createPrefecture(OrmaDatabase orma, String code, String name) {
        return orma.createPrefecture(() -> new Prefecture(code, name, Region.this));
    }
}
