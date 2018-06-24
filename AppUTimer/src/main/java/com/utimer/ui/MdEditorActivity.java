package com.utimer.ui;

import android.support.annotation.NonNull;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.utimer.R;

import ahtewlg7.utimer.ui.ANoteEditorActivity;
import ahtewlg7.utimer.view.md.MdEditText;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lw on 2017/12/27.
 */

public class MdEditorActivity extends ANoteEditorActivity {
    public static final String TAG = MdEditorActivity.class.getSimpleName();

    @BindView(R.id.activity_md_editor_textview)
    MdEditText editTextView;

    @Override
    protected void toInitView() {
        setContentView(R.layout.activity_md_editor);
        ButterKnife.bind(this);
    }

    @Override
    protected @NonNull MdEditText getEditView() {
        return editTextView;
    }

    @Override
    public LifecycleProvider getRxLifeCycleBindView() {
        return this;
    }
}
