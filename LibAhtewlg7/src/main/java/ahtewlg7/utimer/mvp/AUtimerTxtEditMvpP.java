package ahtewlg7.utimer.mvp;


import android.support.annotation.NonNull;

import com.google.common.base.Optional;
import com.google.common.collect.Table;

import org.reactivestreams.Subscription;

import java.util.List;
import java.util.Map;

import ahtewlg7.utimer.gtd.GtdActParser;
import ahtewlg7.utimer.entity.AUtimerEntity;
import ahtewlg7.utimer.entity.busevent.ActionBusEvent;
import ahtewlg7.utimer.entity.busevent.UTimerBusEvent;
import ahtewlg7.utimer.entity.gtd.GtdActionEntity;
import ahtewlg7.utimer.entity.md.EditElement;
import ahtewlg7.utimer.entity.md.EditMementoBean;
import ahtewlg7.utimer.entity.md.EditMementoCaretaker;
import ahtewlg7.utimer.entity.md.EditMementoOriginator;
import ahtewlg7.utimer.enumtype.GtdBusEventType;
import ahtewlg7.utimer.factory.EventBusFatory;
import ahtewlg7.utimer.factory.GtdActionByUuidFactory;
import ahtewlg7.utimer.util.MySafeFlowableOnSubscribe;
import ahtewlg7.utimer.util.MySafeSubscriber;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lw on 2018/10/20.
 */
public abstract class AUtimerTxtEditMvpP<T extends AUtimerEntity> implements IUtimerEditMvpP{
    protected abstract IUtimerEditMvpM getEditMvpM(T t);

    protected Disposable insertDisplose;
    protected Disposable modifyDispose;

    protected T t;

    protected GtdActParser gtdActParser;
    protected IUtimerEditMvpV editMvpV;
    protected IUtimerEditMvpM editMvpM;
    protected EditMementoOriginator mdMementoOriginator;
    protected EditMementoCaretaker mdMementoCaretaker;

    public AUtimerTxtEditMvpP(T t , IUtimerEditMvpV editMvpV) {
        this.t = t;
        this.editMvpV = editMvpV;
        editMvpM      = getEditMvpM(t);
        gtdActParser  = new GtdActParser();
    }

    public void toDel(int fromIndex, int toIndex){
//        editMvpV.onParseStart();
        if(toIndex < fromIndex || fromIndex < 0 || mdMementoOriginator == null || fromIndex > mdMementoOriginator.getMdElementList().size()){
//            editMvpV.onParseErr(new IndexOutOfBoundsException());
            return;
        }

        /*List<EditElement> delList = mdMementoOriginator.getMdElementList().subList(fromIndex, toIndex);
        Optional<EditMementoBean>  mementoOptional = mdMementoOriginator.createMemento(fromIndex, ElementEditType.DELETE, delList);
        if(mementoOptional.isPresent()) {
            mdMementoCaretaker.saveMemento(mementoOptional.get());
            editMvpV.onParseSucc(mementoOptional.get());
        }*/
    }

    public boolean toRedo(){
        return mdMementoCaretaker != null && restoreEdit(mdMementoCaretaker.popNextRedo());
    }

    public boolean toUndo(){
        return mdMementoCaretaker != null && restoreEdit(mdMementoCaretaker.popNextUndo());
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
                        UTimerBusEvent busEvent = new UTimerBusEvent(GtdBusEventType.SAVE, t);
                        EventBusFatory.getInstance().getDefaultEventBus().postSticky(busEvent);
//                        isChangeSaved = true;
                    }
                });
    }
    public void toPostAction(@NonNull final Table<Integer, Integer, EditElement> editElementTable){
        Flowable.create(new MySafeFlowableOnSubscribe<GtdActionEntity>() {
            @Override
            public void subscribe(FlowableEmitter<GtdActionEntity> e) throws Exception {
                super.subscribe(e);
                try{
                    Map<Integer,Map<Integer,EditElement>> rowMap = editElementTable.rowMap();
                    for(int i = 0 ; i < rowMap.size() ; i++) {
                        Map<Integer, EditElement> columnMap = rowMap.get(i);
                        EditElement eLast = columnMap.get(columnMap.size() - 1);
                        Optional<GtdActionEntity> gtdActionOptional = gtdActParser.toParseAction(eLast.getMdCharSequence().toString());
                        if (!gtdActionOptional.isPresent())
                            continue;
                        EditElement eFirst = columnMap.get(0);
                        Optional<GtdActionEntity> firstGtdActionOptional =
                                GtdActionByUuidFactory.getInstance().getActionByDetail(columnMap.get(0).getMdCharSequence().toString());
                        if (columnMap.size() == 1
                                && !firstGtdActionOptional.isPresent()) {
                            GtdActionByUuidFactory.getInstance().add(gtdActionOptional.get().getUuid(), gtdActionOptional.get());
                            e.onNext(gtdActionOptional.get());
                        } else if (columnMap.size() > 1) {
                            if (firstGtdActionOptional.isPresent() && !eLast.getRawText().equals(eFirst.getMdCharSequence().toString())) {
                                //means : action is edit
                                firstGtdActionOptional.get().update(gtdActionOptional.get());
                                GtdActionByUuidFactory.getInstance().update(firstGtdActionOptional.get().getUuid(), firstGtdActionOptional.get());
                                e.onNext(firstGtdActionOptional.get());
                            } else {
                                //add
                                GtdActionByUuidFactory.getInstance().add(gtdActionOptional.get().getUuid(), gtdActionOptional.get());
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
        .subscribe(new MySafeSubscriber<GtdActionEntity>() {
            @Override
            public void onNext(GtdActionEntity entity) {
                super.onNext(entity);
                ActionBusEvent actionBusEvent = new ActionBusEvent(GtdBusEventType.SAVE, entity);
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

    public interface IUtimerEditMvpM{
        public Flowable<Boolean> toSaveElement(@NonNull Flowable<EditElement> elementObservable);
    }

    public interface IUtimerEditMvpV{
        public boolean ifTxtChanged();
        public void onSaveStart();
        public void onSaveErr(Throwable e);
        public void onSaveEnd();
    }
}