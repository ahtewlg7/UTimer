package ahtewlg7.utimer.mvp;

import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.blankj.utilcode.util.FileUtils;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.RxActivity;

import org.reactivestreams.Subscription;

import java.io.File;
import java.util.List;

import ahtewlg7.utimer.common.MdElementAction;
import ahtewlg7.utimer.entity.md.MdElement;
import ahtewlg7.utimer.entity.NoteEntity;
import ahtewlg7.utimer.enumtype.EditModeType;
import ahtewlg7.utimer.enumtype.UnLoadType;
import ahtewlg7.utimer.enumtype.MdContextErrCode;
import ahtewlg7.utimer.exception.MdContextException;
import ahtewlg7.utimer.util.Logcat;
import ahtewlg7.utimer.util.MySafeSubscriber;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

import static ahtewlg7.utimer.enumtype.MdContextErrCode.ERR_CONTEXT_PARSE;

public class NoteMdEditMvpP {
    public static final String TAG = NoteMdEditMvpP.class.getSimpleName();

    private List<MdElement> mdElementList;
    private INoteMdEditMvpV mdFileEditMvpV;
    private INoteMdEditMvpM mdFileEditMvpM;

    private EditModeType currEditMode;
    private GestureListener gestureListener;

    public NoteMdEditMvpP(@NonNull INoteMdEditMvpV mdFileEditMvpV){
        this.mdFileEditMvpV     = mdFileEditMvpV;
        mdFileEditMvpM          = new MdElementAction();
        mdElementList           = Lists.newArrayList();

        currEditMode    = EditModeType.EDIT_MODE_MD;
        mdFileEditMvpV.onMdShowMode(null);

        gestureListener = new GestureListener();
        mdFileEditMvpV.registeEditViewEvent(gestureListener);
    }
    //====================================MdContextMvpV Chain========================================
    /*public void toRegisterMdContextMvpV(){
        Optional<MdContextMvpP> mdContextMvpP = mdContextEditMvpV.getMdContextMvpP();
        if(mdContextMvpP.isPresent())
            mdContextMvpP.get().setMdContextMvpV(mdContextEditMvpV);
    }
    public void toUnregisterMdContextMvpV(){
        Optional<MdContextMvpP> binderServiceOptional = mdContextEditMvpV.getMdContextMvpP();
        if(binderServiceOptional.isPresent())
            binderServiceOptional.get().setMdContextMvpV(null);
    }*/
    public void toParseNoteEnity(@NonNull NoteEntity noteEntity){
        Optional<MdFileMvpP> mdContextMvpP = mdFileEditMvpV.getMdContextMvpP();
        if(!mdContextMvpP.isPresent()){
            Logcat.i(TAG,"toParseNoteEnity err : can not to parse md");
            mdFileEditMvpV.onContextParseErr(new MdContextException(ERR_CONTEXT_PARSE));
            return;
        }

        if(noteEntity.isNoteFileExist() && noteEntity.getLoadType() == UnLoadType.DB) {
            Logcat.i(TAG,"toParseNoteEnity : " + noteEntity.getFileAbsPath());
            mdContextMvpP.get().toParseMdFile(Flowable.just(noteEntity)
                .map(new Function<NoteEntity, File>() {
                    @Override
                    public File apply(NoteEntity noteEntity) throws Exception {
                        if(TextUtils.isEmpty(noteEntity.getFileAbsPath()))
                            throw new MdContextException(MdContextErrCode.ERR_CONTEXT_FILEPATH_INVALID);
                        else if(!noteEntity.isNoteFileExist())
                            throw new MdContextException(MdContextErrCode.ERR_CONTEXT_FILE_NO_EXIST);
                        return FileUtils.getFileByPath(noteEntity.getFileAbsPath());
                    }
                }))
            .doOnNext(new Consumer<MdElement>() {
                @Override
                public void accept(MdElement mdElement) throws Exception {
                    mdFileEditMvpM.handleElement(mdElement);
                }
            })
            .compose(((RxActivity) mdFileEditMvpV.getRxLifeCycleBindView()).<MdElement>bindUntilEvent(ActivityEvent.DESTROY))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new MySafeSubscriber<MdElement>(){
                @Override
                public void onSubscribe(Subscription s) {
                    super.onSubscribe(s);
                    mdFileEditMvpV.onContextParseStart();
                }

                @Override
                public void onNext(MdElement mdElement) {
                    super.onNext(mdElement);
                    mdElementList.add(mdElement);
                    mdFileEditMvpV.onContextParsing(mdElement);
                }

                @Override
                public void onError(Throwable t) {
                    super.onError(t);
                    mdFileEditMvpV.onContextParseErr(t);
                }

                @Override
                public void onComplete() {
                    super.onComplete();
                    mdFileEditMvpV.onContextParseEnd();
                }
            });
        } else {
            Logcat.i(TAG,"toParseNoteEnity : no such file ,to create one");
            mdFileEditMvpV.onRawEditMode(null);
            currEditMode = EditModeType.EDIT_MODE_RAW;
        }
    }

    public void toParseEditRawText(@NonNull Flowable<Optional<String>> rawTextFlowable){
        Optional<MdFileMvpP> mdContextMvpP = mdFileEditMvpV.getMdContextMvpP();
        if(!mdContextMvpP.isPresent()){
            Logcat.i(TAG,"toParseEditRawText err : can not to parse md");
            mdFileEditMvpV.onContextParseErr(new MdContextException(ERR_CONTEXT_PARSE));
            return;
        }

        mdContextMvpP.get().toParseMd(rawTextFlowable)
            .doOnNext(new Consumer<MdElement>() {
                @Override
                public void accept(MdElement mdElement) throws Exception {
                    mdFileEditMvpM.handleElement(mdElement);
                }
            })
            .compose(((RxActivity) mdFileEditMvpV.getRxLifeCycleBindView()).<MdElement>bindUntilEvent(ActivityEvent.DESTROY))
            .subscribe(new MySafeSubscriber<MdElement>(){
                @Override
                public void onNext(MdElement mdElement) {
                    super.onNext(mdElement);

                }
            });
    }

    public void toHandleTextChange(@NonNull Observable<TextViewTextChangeEvent> textChagneEventObservable){
        toParseEditRawText(textChagneEventObservable
                .skipWhile(new Predicate<TextViewTextChangeEvent>() {
                    @Override
                    public boolean test(TextViewTextChangeEvent textViewTextChangeEvent) throws Exception {
                        return false;
                    }
                })
                .map(new Function<TextViewTextChangeEvent, Optional<String>>() {
                    @Override
                    public Optional<String> apply(TextViewTextChangeEvent textViewTextChangeEvent) throws Exception {
                        return Optional.of(textViewTextChangeEvent.text().toString());
                    }
                })
                .toFlowable(BackpressureStrategy.MISSING));
    }

    public void toSaveMdFile(@NonNull NoteEntity noteEntity){
        Optional<MdFileMvpP> mdContextMvpP = mdFileEditMvpV.getMdContextMvpP();
        if(mdContextMvpP.isPresent()) {
            Logcat.i(TAG,"toSaveMdFile");
            MdElement mdElement = getEditElement(currEditMode);
            if (currEditMode == EditModeType.EDIT_MODE_RAW && mdElement != null) {
                    mdElementList.clear();//todo
                    mdElementList.add(mdElement);
            }
            mdContextMvpP.get().toSaveMdFile(new File(noteEntity.getFileAbsPath()),
                    Flowable.fromIterable(mdElementList).map(new Function<MdElement, Optional<String>>() {
                        @Override
                        public Optional<String> apply(MdElement element) throws Exception {
                            return Optional.fromNullable(element.getText());
                        }
                    }));
        }
    }

    private MdElement getEditElement(EditModeType currEditMode){
        MdElement editElement = null;
        /*if(currEditMode == EditModeType.EDIT_MODE_MD ) {
            editElement = new MdElement()
        }*/
        ;
        if(mdFileEditMvpV.getPreTextChangeEvent().isPresent())
            editElement = new MdElement(mdFileEditMvpV.getPreTextChangeEvent().get().text().toString());
        return editElement;
    }


    public interface INoteMdEditMvpM {
        public CharSequence handleElement(@NonNull MdElement element);
    }
    public interface INoteMdEditMvpV extends MdFileMvpP.IMdContextMvpV , IRxLifeCycleBindView {
        public int getCurrLine();
        public Optional<TextViewTextChangeEvent> getPreTextChangeEvent();
        public Optional<MdFileMvpP> getMdContextMvpP();
        public void registeEditViewEvent(@NonNull GestureDetector.OnGestureListener gestureListener);
        public void onRawEditMode(MdElement mdElement);
        public void onMdShowMode(MdElement mdElement);
    }

    class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            if (e.getAction() != MotionEvent.ACTION_UP)
                return false;
            MdElement mdElement = getEditElement(currEditMode);

            if (currEditMode == EditModeType.EDIT_MODE_RAW){
                if (mdElement != null) {
                    mdElementList.clear();//todo
                    mdElementList.add(mdElement);
                }
                mdFileEditMvpV.onMdShowMode(mdElement);
            }else
                mdFileEditMvpV.onRawEditMode(mdElement);
            currEditMode = currEditMode == EditModeType.EDIT_MODE_RAW ? EditModeType.EDIT_MODE_MD : EditModeType.EDIT_MODE_RAW;
            return true;
        }
    }
}
