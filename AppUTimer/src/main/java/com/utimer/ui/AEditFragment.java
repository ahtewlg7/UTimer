package com.utimer.ui;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;

import com.blankj.utilcode.util.ToastUtils;
import com.google.common.base.Optional;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.utimer.R;

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
    protected abstract Optional<EditText> getEditTextItem(int index);

    protected Disposable editViewDisposable;
    protected PublishSubject<Integer> editPositionSubject;

    @Override
    public void onViewCreated(View inflateView) {
        super.onViewCreated(inflateView);

        if(!ifEnvOk()){
            Logcat.i(TAG,"the env is not ready , so pop it");
            ToastUtils.showShort(R.string.entity_invalid);
            pop();
            return;
        }
        editPositionSubject = PublishSubject.create();

        getEditMvpP().toLoadTxt();
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
        editViewDisposable = editPositionSubject.subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer position) throws Exception {
                Optional<EditText> editTextOptional = getEditTextItem(position);
                Logcat.i(TAG,"editTextOptional isPresent = " + editTextOptional.isPresent());
                if(editTextOptional.isPresent())
                    getEditMvpP().toModify(position, getTextWatcher(editTextOptional.get()));
            }
        });
    }

    protected Observable<String> getTextWatcher(EditText editText){
        return RxTextView.textChanges(editText).debounce(5, TimeUnit.SECONDS)
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
        editViewDisposable = null;
    }

    protected void onEditEnd(){
        getEditMvpP().toFinishEdit();
    }
}
