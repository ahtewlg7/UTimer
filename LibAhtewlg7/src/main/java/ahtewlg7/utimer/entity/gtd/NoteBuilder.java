package ahtewlg7.utimer.entity.gtd;

import android.support.annotation.NonNull;

import ahtewlg7.utimer.entity.AUtimerBuilder;

/**
 * Created by lw on 2018/10/26.
 */
public class NoteBuilder extends AUtimerBuilder<NoteEntity, NoteBuilder> {
    public static final String TAG = NoteBuilder.class.getSimpleName();

    protected GtdProjectEntity projectEntity;

    public NoteBuilder setProjectEntity(GtdProjectEntity projectEntity) {
        this.projectEntity = projectEntity;
        return this;
    }

    @NonNull
    @Override
    public NoteEntity build() {
        return new NoteEntity(this);
    }
}
