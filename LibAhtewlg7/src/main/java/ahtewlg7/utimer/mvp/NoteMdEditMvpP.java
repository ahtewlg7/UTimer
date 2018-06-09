package ahtewlg7.utimer.mvp;

import android.text.TextUtils;

import com.blankj.utilcode.util.FileUtils;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.RxActivity;

import org.reactivestreams.Subscription;

import java.io.File;
import java.util.List;

import ahtewlg7.utimer.common.MdElementAction;
import ahtewlg7.utimer.entity.MdElement;
import ahtewlg7.utimer.entity.NoteEntity;
import ahtewlg7.utimer.enumtype.MdContextErrCode;
import ahtewlg7.utimer.exception.MdContextException;
import ahtewlg7.utimer.util.MySafeSubscriber;
import ahtewlg7.utimer.view.IRxLifeCycleBindView;
import in.uncod.android.bypass.Element;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class NoteMdEditMvpP {
    public static final String TAG = NoteMdEditMvpP.class.getSimpleName();

    private List<MdElement> mdElementList;
    private INoteMdEditMvpV mdFileEditMvpV;
    private INoteMdEditMvpM mdFileEditMvpM;

    public NoteMdEditMvpP(@NonNull INoteMdEditMvpV mdFileEditMvpV){
        this.mdFileEditMvpV     = mdFileEditMvpV;
        mdFileEditMvpM          = new MdElementAction();
        mdElementList           = Lists.newArrayList();
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
        Optional<MdContextMvpP> mdContextMvpP = mdFileEditMvpV.getMdContextMvpP();
        if(mdContextMvpP.isPresent()) {
            mdContextMvpP.get().toParseMdFile(Flowable.just(noteEntity)
                .map(new Function<NoteEntity, File>() {
                    @Override
                    public File apply(NoteEntity noteEntity) throws Exception {
                        if(TextUtils.isEmpty(noteEntity.getFileAbsPath()))
                            throw new MdContextException(MdContextErrCode.ERR_CONTEXT_FILEPATH_INVALID);
                        else if(!FileUtils.isFileExists(noteEntity.getFileAbsPath()))
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
        }
    }

    public void toSaveNoteEntity(@NonNull NoteEntity noteEntity){
        Optional<MdContextMvpP> mdContextMvpP = mdFileEditMvpV.getMdContextMvpP();
        if(mdContextMvpP.isPresent()) {
            mdContextMvpP.get().toSaveMdFile(new File(noteEntity.getFileAbsPath()),
                    Flowable.fromIterable(mdElementList).map(new Function<Element, Optional<String>>() {
                        @Override
                        public Optional<String> apply(Element element) throws Exception {
                            return Optional.fromNullable(element.getText());
                        }
                    }));
        }
    }

    public interface INoteMdEditMvpM {
        public CharSequence handleElement(@NonNull MdElement element);
    }
    public interface INoteMdEditMvpV extends MdContextMvpP.IMdContextMvpV , IRxLifeCycleBindView {
        public Optional<MdContextMvpP> getMdContextMvpP();
    }
}
