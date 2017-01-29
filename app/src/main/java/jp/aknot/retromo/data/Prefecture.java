package jp.aknot.retromo.data;

import com.github.gfx.android.orma.annotation.Column;
import com.github.gfx.android.orma.annotation.PrimaryKey;
import com.github.gfx.android.orma.annotation.Setter;
import com.github.gfx.android.orma.annotation.Table;

@Table
public class Prefecture {

    @Setter
    public Prefecture(long id, String code, String name, long createdTimeMillis) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.createdTimeMillis = createdTimeMillis;
    }

    @PrimaryKey(autoincrement = true)
    public final long id;

    @Column(indexed = true)
    public final String code;

    @Column
    public final String name;

    @Column
    public final long createdTimeMillis;
}
