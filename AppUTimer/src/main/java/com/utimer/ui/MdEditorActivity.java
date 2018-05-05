package com.utimer.ui;

import android.widget.EditText;
import android.widget.TextView;

import com.trello.rxlifecycle2.components.RxActivity;
import com.utimer.R;

import ahtewlg7.utimer.ui.ANoteEditorActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lw on 2017/12/27.
 */

public class MdEditorActivity extends ANoteEditorActivity {
    public static final String TAG = MdEditorActivity.class.getSimpleName();

    @BindView(R.id.activity_md_editor_textview)
    EditText editTextView;

    @Override
    protected void toInitView() {
        setContentView(R.layout.activity_md_editor);
        ButterKnife.bind(this);
    }

    @Override
    public TextView getEditView() {
        return editTextView;
    }

    @Override
    public RxActivity getUiContext() {
        return this;
    }
}
