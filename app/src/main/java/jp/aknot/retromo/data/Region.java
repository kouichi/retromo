package jp.aknot.retromo.data;

import com.github.gfx.android.orma.annotation.Column;
import com.github.gfx.android.orma.annotation.PrimaryKey;
import com.github.gfx.android.orma.annotation.Setter;
import com.github.gfx.android.orma.annotation.Table;

@Table
public class Region {

    @Setter
    public Region(long id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }

    @PrimaryKey(autoincrement = true)
    public final long id;

    @Column(indexed = true)
    public final String code;

    @Column
    public final String name;
}
