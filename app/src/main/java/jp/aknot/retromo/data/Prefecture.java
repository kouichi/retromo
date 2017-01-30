package jp.aknot.retromo.data;

import com.github.gfx.android.orma.annotation.Column;
import com.github.gfx.android.orma.annotation.OnConflict;
import com.github.gfx.android.orma.annotation.PrimaryKey;
import com.github.gfx.android.orma.annotation.Setter;
import com.github.gfx.android.orma.annotation.Table;

@Table
public class Prefecture {

    @Setter
    public Prefecture(String code, String name, Region region) {
        this.code = code;
        this.name = name;
        this.region = region;
    }

    @PrimaryKey(onConflict = OnConflict.IGNORE)
    public final String code;

    @Column
    public final String name;

    @Column(indexed = true)
    public final Region region;
}
