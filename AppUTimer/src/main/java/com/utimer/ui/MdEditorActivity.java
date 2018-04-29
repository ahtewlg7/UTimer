package com.utimer.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.EditText;
import android.widget.TextView;

import com.utimer.R;

import ahtewlg7.utimer.ui.ANoteEditorActivity;
import ahtewlg7.utimer.util.MyRInfo;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lw on 2017/12/27.
 */

public class MdEditorActivity extends ANoteEditorActivity {
    public static final String TAG = MdEditorActivity.class.getSimpleName();

    public static final String EXTRA_KEY_GTD_ID  = MyRInfo.getStringByID(R.string.extra_gtd_id);
    public static final String EXTRA_KEY_NOTE_ID = MyRInfo.getStringByID(R.string.extra_note_id);

    @BindView(R.id.activity_md_editor_textview)
    EditText editTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_md_editor);
        ButterKnife.bind(this);

        initAcitity();
    }

    @NonNull
    @Override
    protected TextView getEditTextView() {
        return editTextView;
    }

    @NonNull
    @Override
    protected String getNoteIdExtrKey() {
        return EXTRA_KEY_NOTE_ID;
    }

    @NonNull
    @Override
    protected String getGtdIdExtrKey() {
        return EXTRA_KEY_GTD_ID;
    }
}
