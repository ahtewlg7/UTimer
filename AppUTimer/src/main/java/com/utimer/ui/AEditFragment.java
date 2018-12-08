package com.utimer.ui;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;

import com.blankj.utilcode.util.ToastUtils;
import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.utimer.R;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import ahtewlg7.utimer.mvp.AUtimerEditMvpP;
import ahtewlg7.utimer.util.Logcat;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by lw on 2018/11/8.
 */
public abstract class AEditFragment extends AToolbarBkFragment{
    public static final String TAG = AEditFragment.class.getSimpleName();

    protected abstract @NonNull AUtimerEditMvpP getEditMvpP();
    protected abstract boolean ifEnvOk();

    protected int preEditPosition = -1;

    protected AUtimerEditMvpP editMvpP;
    protected Disposable editViewDisposable;
    protected Map<Integer,EditViewBean> editViewMap;
    protected PublishSubject<EditViewBean> editViewPublishSubject;

    @Override
    public void onViewCreated(View inflateView) {
        super.onViewCreated(inflateView);

        if(!ifEnvOk()){
            Logcat.i(TAG,"the env is not ready , so pop it");
            ToastUtils.showShort(R.string.entity_invalid);
            pop();
            return;
        }
        editViewPublishSubject = PublishSubject.create();
        editViewMap            = Maps.newHashMap();
        editMvpP               = getEditMvpP();

        editMvpP.toLoadTxt();
        createEditViewListen();
    }

    @Override
    public void onStop() {
        super.onStop();
        onEditEnd();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden)
            onEditEnd();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        toStopEditWatch();
    }

    protected void createEditViewListen(){
        editViewDisposable = editViewPublishSubject.subscribe(new Consumer<EditViewBean>() {
            @Override
            public void accept(EditViewBean editViewBean) throws Exception {
                editMvpP.toModify(editViewBean.getIndex(), getTextWatcher(editViewBean.getEditTextView()));
            }
        });
    }

    protected Observable<String> getTextWatcher(EditText editText){
        return RxTextView.textChanges(editText).debounce(10, TimeUnit.SECONDS)
                .map(new Function<CharSequence, String>() {
                    @Override
                    public String apply(CharSequence charSequence) throws Exception {
                        return charSequence.toString();
                    }
                });
    }

    protected void toStopEditWatch(){
        if(editViewDisposable != null && !editViewDisposable.isDisposed())
            editViewDisposable.dispose();
        editViewMap.clear();
        editViewDisposable = null;
    }
    protected void onEditViewLockChange(EditViewBean editViewBean){
        boolean isEditing = editViewBean.isEditing();
        Logcat.i(TAG,"onEditViewLockChange isEditing = " + isEditing);
        editViewBean.getEditTextView().setFocusable(isEditing);
        editViewBean.getEditTextView().setFocusableInTouchMode(isEditing);
    }
    protected Optional<EditViewBean> getEditViewBean(int position){
        if(position < 0 || position >= editViewMap.size()) {
            Logcat.i(TAG,"onEditViewLockChange : position invalid");
            return Optional.absent();
        }
        return Optional.fromNullable(editViewMap.get(position));
    }
    protected void onEditEnd(){
        editMvpP.toFinishEdit();
    }

    class EditViewBean{
        private int index;
        private boolean isEditing;
        private EditText editTextView;

        public EditViewBean(int index, EditText editTextView) {
            this.index = index;
            this.editTextView = editTextView;
        }

        public boolean isEditing() {
            return isEditing;
        }

        public void setEditing(boolean editing) {
            isEditing = editing;
        }

        public int getIndex() {
            return index;
        }

        public EditText getEditTextView() {
            return editTextView;
        }
    }
}
