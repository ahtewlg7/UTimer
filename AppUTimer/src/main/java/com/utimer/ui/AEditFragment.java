package com.utimer.ui;

import android.view.View;
import android.widget.EditText;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import ahtewlg7.utimer.entity.md.EditMementoBean;
import ahtewlg7.utimer.util.Logcat;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by lw on 2018/11/8.
 */
public abstract class AEditFragment extends AToolbarBkFragment{
    public static final String TAG = AEditFragment.class.getSimpleName();

    protected abstract void onEditMode(int index, Observable<String> rawTxtRx);

    protected int preEditPosition = -1;
    protected EditMementoBean preEditMementoBean;
    protected Disposable editViewDisposable;
    protected HashMap<Integer, EditViewBean> editViewMap;
    protected PublishSubject<EditViewBean> editViewPublishSubject;

    @Override
    public void onViewCreated(View inflateView) {
        super.onViewCreated(inflateView);
        editViewPublishSubject = PublishSubject.create();
        editViewMap            = Maps.newHashMap();
        createEditViewListen();
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
                editViewMap.put(editViewBean.getIndex(), editViewBean);
                onEditMode(editViewBean.getIndex(), getTextWatcher(editViewBean.getEditTextView()));
            }
        });
    }

    protected Observable<String> getTextWatcher(EditText editText){
        return RxTextView.textChanges(editText).throttleLast(10, TimeUnit.SECONDS)
                .filter(new Predicate<CharSequence>() {
                    @Override
                    public boolean test(CharSequence charSequence) throws Exception {
                        String preMdTxt = "";
                        if(preEditMementoBean != null && preEditMementoBean.getMdDocTxt().isPresent())
                            preMdTxt = preEditMementoBean.getMdDocTxt().get();
                        return !charSequence.toString().equals(preMdTxt);
                    }
                })
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
    protected void onEditViewLock(EditViewBean editViewBean){
        boolean isEditing = editViewBean.isEditing();
        Logcat.i(TAG,"onEditViewLock isEditing = " + isEditing);
        editViewBean.getEditTextView().setFocusable(isEditing);
        editViewBean.getEditTextView().setFocusableInTouchMode(isEditing);
    }
    protected Optional<EditViewBean> onEditViewLock(int position){
        if(position < 0 || position >= editViewMap.size()) {
            Logcat.i(TAG,"onEditViewLock : position invalid");
            return Optional.absent();
        }
        return Optional.fromNullable(editViewMap.get(position));
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
