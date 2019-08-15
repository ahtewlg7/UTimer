package ahtewlg7.utimer.entity.gtd;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.joda.time.LocalDate;

import java.util.List;

/**
 * Created by lw on 2019/7/20.
 */
public class DeedSchemeInfo{
    private LocalDate localDate;
    private List<DeedSchemeEntity> deedSchemeEntityList;

    public DeedSchemeInfo(@NonNull LocalDate localDate, List<DeedSchemeEntity> deedSchemeEntityList) {
        this.localDate = localDate;
        this.deedSchemeEntityList = deedSchemeEntityList;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return obj instanceof DeedSchemeInfo && localDate == ((DeedSchemeInfo) obj).getLocalDate();
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public List<DeedSchemeEntity> getDeedSchemeEntityList() {
        return deedSchemeEntityList;
    }
}
