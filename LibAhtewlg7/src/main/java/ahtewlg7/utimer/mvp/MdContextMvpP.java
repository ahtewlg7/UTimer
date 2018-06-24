package ahtewlg7.utimer.mvp;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.blankj.utilcode.util.FileUtils;
import com.google.common.base.Optional;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.reactivestreams.Subscription;

import java.io.File;
import java.lang.ref.WeakReference;

import ahtewlg7.utimer.busevent.NoteDeleteEvent;
import ahtewlg7.utimer.common.EventBusFatory;
import ahtewlg7.utimer.common.MdFileFsAction;
import ahtewlg7.utimer.entity.MdElement;
import ahtewlg7.utimer.enumtype.MdContextErrCode;
import ahtewlg7.utimer.exception.MdContextException;
import ahtewlg7.utimer.util.Logcat;
import ahtewlg7.utimer.util.MySafeSubscriber;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MdContextMvpP {
    public static final String TAG = MdContextMvpP.class.getSimpleName();

    private EventBus eventBus;
    private IMdContextMvpM mdContextMvpM;
    private WeakReference<IMdContextMvpV> mdContextMvpVWeakReference;

    public MdContextMvpP(){
        mdContextMvpM      = new MdFileFsAction();
        eventBus           = EventBusFatory.getInstance().getDefaultEventBus();
    }

    public void setMdContextMvpV(IMdContextMvpV mdContextMvpV){
        clearWeakReference();
        if(mdContextMvpV != null)
            mdContextMvpVWeakReference = new WeakReference<IMdContextMvpV>(mdContextMvpV);
    }
    private void clearWeakReference(){
        mdContextMvpVWeakReference.clear();
        mdContextMvpVWeakReference = null;
        System.gc();
    }
    //=======================================EventBus================================================
    public void toRegisterEventBus(){
        Logcat.i(TAG,"toRegisterEventBus");
        if(eventBus != null && !eventBus.isRegistered(MdContextMvpP.this))
            eventBus.register(MdContextMvpP.this);
    }
    public void toUnregisterEventBus(){
        Logcat.i(TAG,"toUnregisterEventBus");
        if(eventBus != null && eventBus.isRegistered(MdContextMvpP.this))
            eventBus.unregister(MdContextMvpP.this);
    }

    //EventBus callback
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onNoteDeleteEvent(NoteDeleteEvent event) {
        Logcat.i(TAG,"onNoteDeleteEvent  : noteDeleteEvent = " + event.toString());
        toDelMdFile(Flowable.just(event.getNoteFilePath())
            .map(new Function<Optional<String>, File>() {
                @Override
                public File apply(Optional<String> stringOptional) throws Exception {
                    if(!stringOptional.isPresent() || TextUtils.isEmpty(stringOptional.get()))
                        throw new MdContextException(MdContextErrCode.ERR_CONTEXT_FILEPATH_INVALID);
                    else if(!FileUtils.isFileExists(stringOptional.get()))
                        throw new MdContextException(MdContextErrCode.ERR_CONTEXT_FILE_NO_EXIST);
                    return FileUtils.getFileByPath(stringOptional.get());
                }
            }));
    }
    //=======================================EventBus================================================
    public Flowable<MdElement> toParseMdFile(@NonNull Flowable<File> filePathObserver){
        return mdContextMvpM.toParseRawContext(mdContextMvpM.toReadRawContext(filePathObserver));
    }

    public Flowable<MdElement> toParseMd(@NonNull Flowable<Optional<String>> mdContextFlowable){
        return mdContextMvpM.toParseRawContext(mdContextFlowable);
    }

    public void toParseContext(@NonNull Flowable<Optional<String>> mdContextFlowable){
        mdContextMvpM.toParseRawContext(mdContextFlowable)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new MySafeSubscriber<MdElement>(){
                @Override
                public void onSubscribe(Subscription s) {
                    super.onSubscribe(s);
                    if(mdContextMvpVWeakReference.get() != null )
                        mdContextMvpVWeakReference.get().onContextParseStart();
                }

                @Override
                public void onNext(MdElement element) {
                    super.onNext(element);
                    if(mdContextMvpVWeakReference.get() != null )
                        mdContextMvpVWeakReference.get().onContextParsing(element);
                }

                @Override
                public void onError(Throwable t) {
                    super.onError(t);
                    if(mdContextMvpVWeakReference.get() != null )
                        mdContextMvpVWeakReference.get().onContextParseErr(t);
                }

                @Override
                public void onComplete() {
                    super.onComplete();
                    if(mdContextMvpVWeakReference.get() != null )
                        mdContextMvpVWeakReference.get().onContextParseEnd();
                }
            });
    }

    public void toSaveMdFile(@NonNull File mdFile, @NonNull final Flowable<Optional<String>> contextFlowable){
        mdContextMvpM.setMdSaveFilePath(mdFile);
        mdContextMvpM.toSaveContext(contextFlowable)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new MySafeSubscriber<Boolean>(){
                @Override
                public void onSubscribe(Subscription s) {
                    super.onSubscribe(s);
                    if(mdContextMvpVWeakReference != null && mdContextMvpVWeakReference.get() != null )
                        mdContextMvpVWeakReference.get().onContextSaveStart();
                }

                @Override
                public void onNext(Boolean aBoolean) {
                    super.onNext(aBoolean);
                    if(mdContextMvpVWeakReference != null && mdContextMvpVWeakReference.get() != null )
                        mdContextMvpVWeakReference.get().onContextSaving();
                }

                @Override
                public void onError(Throwable t) {
                    super.onError(t);
                    if(mdContextMvpVWeakReference != null && mdContextMvpVWeakReference.get() != null )
                        mdContextMvpVWeakReference.get().onContextSaveErr(t);
                }

                @Override
                public void onComplete() {
                    super.onComplete();
                    if(mdContextMvpVWeakReference != null && mdContextMvpVWeakReference.get() != null )
                        mdContextMvpVWeakReference.get().onContextSaveEnd();
                }
            });
    }

    public void toDelMdFile(@NonNull Flowable<File> filePathFlowable){
        mdContextMvpM.toDeleteContext(filePathFlowable)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MySafeSubscriber<Boolean>(){
                    @Override
                    public void onSubscribe(Subscription s) {
                        super.onSubscribe(s);
                        if(mdContextMvpVWeakReference.get() != null )
                            mdContextMvpVWeakReference.get().onContextDelStart();
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        super.onNext(aBoolean);
                        if(mdContextMvpVWeakReference.get() != null )
                            mdContextMvpVWeakReference.get().onContextDeling();
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        if(mdContextMvpVWeakReference.get() != null )
                            mdContextMvpVWeakReference.get().onContextDelErr(t);
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        if(mdContextMvpVWeakReference.get() != null )
                            mdContextMvpVWeakReference.get().onContextDelEnd();
                    }
                });
    }

    public interface IMdContextMvpM {
        public Flowable<Optional<String>> toReadRawContext(@NonNull Flowable<File> contextFileObserver);
        public Flowable<MdElement> toParseRawContext(@NonNull Flowable<Optional<String>> rawContextFlowable);

        public void setMdSaveFilePath(File mdFile);
        public Flowable<Boolean> toSaveContext(@NonNull final Flowable<Optional<String>> mdFlowable);

        public Flowable<Boolean> toDeleteContext(@NonNull Flowable<File> filePathFlowable);
    }
    public interface IMdContextMvpV {
        public void onContextParseStart();
        public void onContextParsing(MdElement element);
        public void onContextParseErr(Throwable t);
        public void onContextParseEnd();

        public void onContextSaveStart();
        public void onContextSaving();
        public void onContextSaveErr(Throwable t);
        public void onContextSaveEnd();

        public void onContextDelStart();
        public void onContextDeling();
        public void onContextDelErr(Throwable t);
        public void onContextDelEnd();
    }

    /*//==========================================INoteContextSaveMvpM============================================
    public Flowable<Boolean> toSaveContext(NoteEntity noteEntity) {
        return Flowable.just(noteEntity)
                .map(new Function<NoteEntity, Boolean>() {
                    @Override
                    public Boolean apply(NoteEntity noteEntity) throws Exception {
                        boolean result = noteContextFsAction.writeNoteContext(noteEntity);
                        Logcat.i(TAG,"toSaveContext map , result = " + result + ", noteEntity = " + noteEntity.toString()) ;
                        return result;
                    }
                });
    }

    @Override
    public Flowable<Boolean> toDeleteContext(String filePath) {
        return Flowable.just(filePath)
                .subscribeOn(Schedulers.io())
                .map(new Function<String, Boolean>() {
                    @Override
                    public Boolean apply(String s) throws Exception {
                        boolean result = noteContextFsAction.deleteNoteContext(s);
                        Logcat.i(TAG,"toDeleteContext map , result = " + result) ;
                        return result;
                    }
                });
    }*/
}
