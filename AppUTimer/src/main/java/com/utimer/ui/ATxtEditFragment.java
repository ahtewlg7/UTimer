package com.utimer.ui;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;

import com.google.common.base.Optional;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.concurrent.TimeUnit;

import ahtewlg7.utimer.entity.md.EditElement;
import ahtewlg7.utimer.enumtype.EditMode;
import ahtewlg7.utimer.util.Logcat;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by lw on 2019/1/17.
 */
public abstract class ATxtEditFragment extends AEditFragment {
    public static final String TAG = ATxtEditFragment.class.getSimpleName();

    protected abstract Optional<EditText> getEditTextItem(int index);

    protected Disposable editViewDisposable;
    protected PublishSubject<Integer> editPositionSubject;

    @Override
    public void onViewCreated(View inflateView) {
        super.onViewCreated(inflateView);
        editPositionSubject = PublishSubject.create();

        getEditMvpP().toLoadTxt();
        createEditViewListen();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        toStopEditWatch();
    }

    /**********************************************IShorthandEditMvpV**********************************************/
    public void onEditMode(int position, @NonNull EditMode editMode, @NonNull Optional<EditElement> editElementOptional) {
        Optional<EditText> optional = getEditTextItem(position);
        Logcat.i(TAG,"onEditMode isPresent = " + optional.isPresent() + ", editMode = " + editMode.name());
        if(editMode == EditMode.OFF && optional.isPresent()){
            optional.get().setFocusable(false);
            optional.get().setFocusableInTouchMode(false);
            if(editElementOptional.isPresent())
                optional.get().setText(editElementOptional.get().getMdCharSequence().toString());
        }else if(editMode == EditMode.ON && optional.isPresent()){
            optional.get().setFocusable(true);
            optional.get().setFocusableInTouchMode(true);
            editPositionSubject.onNext(position);
            if(editElementOptional.isPresent())
                optional.get().setText(editElementOptional.get().getRawText());
        }
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
}

