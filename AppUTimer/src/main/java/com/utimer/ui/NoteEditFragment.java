package com.utimer.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;

import com.google.common.base.Optional;
import com.utimer.R;

import ahtewlg7.utimer.entity.gtd.NoteBuilder;
import ahtewlg7.utimer.entity.gtd.NoteEntity;
import ahtewlg7.utimer.mvp.AUtimerEditMvpP;
import ahtewlg7.utimer.util.DateTimeAction;
import ahtewlg7.utimer.util.MyRInfo;
import butterknife.BindView;

public class NoteEditFragment extends ATxtEditFragment {
    public static final String TAG = NoteEditFragment.class.getSimpleName();

    public static final String KEY_NOTE = "note";

    @BindView(R.id.activity_utimer_toolbar)
    Toolbar toolbar;

    public static NoteEditFragment newInstance(NoteEntity entity) {
        Bundle args = new Bundle();
        if(entity != null)
            args.putSerializable(KEY_NOTE, entity);
        else {
            String now = new DateTimeAction().toFormatNow().toString();
            NoteEntity e = (NoteEntity)new NoteBuilder().setTitle(now).build();
            args.putSerializable(KEY_NOTE, e);
        }
        NoteEditFragment fragment = new NoteEditFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutRid() {
        return R.layout.un_fragment_recycler;
    }


    @Override
    protected String getTitle() {
        return MyRInfo.getStringByID(R.string.title_note);
    }

    @NonNull
    @Override
    protected Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    protected void initToolbar() {
        toolbar.setTitle(getTitle());
    }

    @Override
    protected Optional<EditText> getEditTextItem(int index) {
        return null;
    }

    @NonNull
    @Override
    protected AUtimerEditMvpP getEditMvpP() {
        return null;
    }

    @Override
    protected NoteEntity getUtimerEntity(){
        return (NoteEntity) getArguments().getSerializable(KEY_NOTE);
    }
}
