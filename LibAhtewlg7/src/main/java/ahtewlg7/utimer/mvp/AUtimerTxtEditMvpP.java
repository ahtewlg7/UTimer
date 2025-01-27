package ahtewlg7.utimer.mvp;


import androidx.annotation.NonNull;

import com.google.common.base.Optional;
import com.google.common.collect.Table;

import org.joda.time.DateTime;
import org.reactivestreams.Subscription;

import java.util.List;
import java.util.Map;

import ahtewlg7.utimer.entity.ABaseMaterialEntity;
import ahtewlg7.utimer.entity.busevent.DeedBusEvent;
import ahtewlg7.utimer.entity.busevent.UTimerBusEvent;
import ahtewlg7.utimer.entity.gtd.GtdDeedEntity;
import ahtewlg7.utimer.entity.md.EditElement;
import ahtewlg7.utimer.entity.md.EditMementoBean;
import ahtewlg7.utimer.entity.md.EditMementoCaretaker;
import ahtewlg7.utimer.entity.md.EditMementoOriginator;
import ahtewlg7.utimer.enumtype.GtdBusEventType;
import ahtewlg7.utimer.factory.EventBusFatory;
import ahtewlg7.utimer.factory.GtdDeedByUuidFactory;
import ahtewlg7.utimer.util.MySafeFlowableOnSubscribe;
import ahtewlg7.utimer.util.MySafeSubscriber;
import ahtewlg7.utimer.util.MySimpleObserver;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lw on 2018/10/20.
 */
public abstract class AUtimerTxtEditMvpP<T extends ABaseMaterialEntity> {
    protected abstract AUtimerEditMvpM getEditMvpM(T t);

    protected Disposable insertDisplose;
    protected Disposable modifyDispose;

    protected T t;

//    protected GtdDeedParser gtdActParser;
    protected IUtimerEditMvpV editMvpV;
    protected AUtimerEditMvpM editMvpM;
    protected EditMementoOriginator mdMementoOriginator;
    protected EditMementoCaretaker mdMementoCaretaker;

    public AUtimerTxtEditMvpP(T t , IUtimerEditMvpV editMvpV) {
        this.t = t;
        this.editMvpV = editMvpV;
        editMvpM      = getEditMvpM(t);
//        gtdActParser  = new GtdDeedParser();
    }

    public boolean toRedo(){
        return mdMementoCaretaker != null && restoreEdit(mdMementoCaretaker.popNextRedo());
    }

    public boolean toUndo(){
        return mdMementoCaretaker != null && restoreEdit(mdMementoCaretaker.popNextUndo());
    }

    public void toRenameAttachFile(final String name){
        Observable.just(name)
                .subscribeOn(Schedulers.io())
                .map(new Function<String, Boolean>() {
                    @Override
                    public Boolean apply(String name) throws Exception {
                        return editMvpM.toRenameAttachFile(name);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MySimpleObserver<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        super.onSubscribe(d);
                        if(editMvpV != null)
                            editMvpV.onRenameStart();
                    }

                    @Override
                    public void onNext(Boolean b) {
                        super.onNext(b);
                        if(editMvpV != null)
                            editMvpV.onRenameEnd(b);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if(editMvpV != null)
                            editMvpV.onRenameErr(e);
                    }
                });
    }

    public void toFinishEdit(@NonNull Flowable<EditElement> editElementRx){
        editMvpM.toSaveElement(editElementRx)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new MySafeSubscriber<Boolean>() {
                @Override
                public void onSubscribe(Subscription s) {
                    super.onSubscribe(s);
                    if(editMvpV != null)
                        editMvpV.onSaveStart();
                }

                @Override
                public void onError(Throwable t) {
                    super.onError(t);
                    if(editMvpV != null)
                        editMvpV.onSaveErr(t);
                }

                @Override
                public void onComplete() {
                    super.onComplete();
                    if(editMvpV != null)
                        editMvpV.onSaveEnd();
                    updateEntity();
                    UTimerBusEvent busEvent = new UTimerBusEvent(GtdBusEventType.SAVE, t);
                    EventBusFatory.getInstance().getDefaultEventBus().postSticky(busEvent);
//                        isChangeSaved = true;
                }
            });
    }
    public void toPostAction(@NonNull final Table<Integer, Integer, EditElement> editElementTable){
        Flowable.create(new MySafeFlowableOnSubscribe<GtdDeedEntity>() {
            @Override
            public void subscribe(FlowableEmitter<GtdDeedEntity> e) throws Exception {
                super.subscribe(e);
                try{
                    Map<Integer,Map<Integer,EditElement>> rowMap = editElementTable.rowMap();
                    for(int i = 0 ; i < rowMap.size() ; i++) {
                        Map<Integer, EditElement> columnMap = rowMap.get(i);
                        EditElement eLast = columnMap.get(columnMap.size() - 1);
//                        Optional<GtdDeedEntity> gtdActionOptional = gtdActParser.toParseAction(eLast.getMdCharSequence().toString());
                        Optional<GtdDeedEntity> gtdActionOptional = Optional.absent();
                        if (!gtdActionOptional.isPresent())
                            continue;
                        EditElement eFirst = columnMap.get(0);
                        Optional<GtdDeedEntity> firstGtdActionOptional =
                                GtdDeedByUuidFactory.getInstance().getDeedByTitle(columnMap.get(0).getMdCharSequence().toString());
                        if (columnMap.size() == 1
                                && !firstGtdActionOptional.isPresent()) {
                            //new
                            e.onNext(gtdActionOptional.get());
                        } else if (columnMap.size() > 1) {
                            if (firstGtdActionOptional.isPresent() && !eLast.getRawText().equals(eFirst.getMdCharSequence().toString())) {
                                //means : action is edit
//                                firstGtdActionOptional.get().update(gtdActionOptional.get());
                                e.onNext(firstGtdActionOptional.get());
                            } else {
                                //new
                                e.onNext(gtdActionOptional.get());
                            }
                        }
                    }
                    e.onComplete();
                }catch (Exception exc){
                    e.onError(exc.getCause());
                }
            }
        }, BackpressureStrategy.MISSING)
        .subscribeOn(Schedulers.computation())
        .subscribe(new MySafeSubscriber<GtdDeedEntity>() {
            @Override
            public void onNext(GtdDeedEntity entity) {
                super.onNext(entity);
                DeedBusEvent actionBusEvent = new DeedBusEvent(GtdBusEventType.CREATE, entity);
                EventBusFatory.getInstance().getDefaultEventBus().postSticky(actionBusEvent);
            }
        });
    }

    public void cancelInsert(){
        if(insertDisplose != null && !insertDisplose.isDisposed())
            insertDisplose.dispose();
        insertDisplose = null;
    }
    public void cancelModify(){
        if(modifyDispose != null && !modifyDispose.isDisposed())
            modifyDispose.dispose();
        modifyDispose = null;
    }

    protected void initMemento(){
        mdMementoCaretaker   = new EditMementoCaretaker();
        mdMementoOriginator  = new EditMementoOriginator();
    }
    protected void initMemento(List<EditElement> mdElementList){
        mdMementoCaretaker   = new EditMementoCaretaker();
        mdMementoOriginator  = new EditMementoOriginator(mdElementList);
    }

    protected boolean restoreEdit(Optional<EditMementoBean> mementoOptional){
        if(mementoOptional.isPresent()){
            mdMementoOriginator.restoreMemento(mementoOptional.get());
//            editMvpV.onRestoreEnd(mementoOptional.get());
            return true;
        }
        return false;
    }
    protected void updateEntity(){
        if(t != null)
            t.setLastAccessTime(DateTime.now());
    }

    public abstract class AUtimerEditMvpM {
        public abstract Flowable<Boolean> toSaveElement(@NonNull Flowable<EditElement> elementObservable);
        public boolean toRenameAttachFile(String newName){
            if(t == null || !t.ifValid())
                return false;
            t.setTitle(newName);
            return t.getAttachFile().renameFile(t.getTitle());
        }
    }

    public interface IUtimerEditMvpV{
        public boolean ifTxtChanged();
        public void onSaveStart();
        public void onSaveErr(Throwable e);
        public void onSaveEnd();

        public void onRenameStart();
        public void onRenameEnd(boolean succ);
        public void onRenameErr(Throwable e);
    }
}