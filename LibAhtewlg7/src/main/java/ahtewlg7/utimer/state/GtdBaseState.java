package ahtewlg7.utimer.state;

import androidx.annotation.NonNull;

import com.google.common.base.Optional;

import ahtewlg7.utimer.entity.BaseGtdEntity;
import ahtewlg7.utimer.entity.BaseEventBusBean;
import ahtewlg7.utimer.entity.busevent.DeedBusEvent;
import ahtewlg7.utimer.entity.gtd.GtdDeedEntity;
import ahtewlg7.utimer.enumtype.GtdBusEventType;
import ahtewlg7.utimer.factory.EventBusFatory;
import ahtewlg7.utimer.factory.GtdDeedByUuidFactory;

import static ahtewlg7.utimer.enumtype.DeedState.INBOX;

/**
 * Created by lw on 2019/1/19.
 */
public class GtdBaseState {
    protected GtdMachine gtdMachine;

    GtdBaseState(@NonNull GtdMachine gtdMachine){
        this.gtdMachine = gtdMachine;
    }

    public Optional<BaseEventBusBean> toInbox(String title, String detail){
        Optional<GtdDeedEntity> deedEntityOptional = GtdDeedByUuidFactory.getInstance().create(title, detail, INBOX);
        if(!deedEntityOptional.isPresent())
            return Optional.absent();
        BaseEventBusBean actionBusEvent = new DeedBusEvent(GtdBusEventType.CREATE, deedEntityOptional.get());
        EventBusFatory.getInstance().getDefaultEventBus().postSticky(actionBusEvent);
        return Optional.of(actionBusEvent);
    }

    public Optional<BaseEventBusBean> toEdit(@NonNull GtdDeedEntity entity, String title, String detail){
        Optional<Boolean> deedEntityOptional = GtdDeedByUuidFactory.getInstance().updateContent(entity, title, detail);
        if(!deedEntityOptional.isPresent() || !deedEntityOptional.get())
            return Optional.absent();
        BaseEventBusBean actionBusEvent = new DeedBusEvent(GtdBusEventType.EDIT, entity);
        EventBusFatory.getInstance().getDefaultEventBus().postSticky(actionBusEvent);
        return Optional.of(actionBusEvent);
    }

    public Optional<BaseEventBusBean> toTrash(@NonNull BaseGtdEntity entity){
        return Optional.absent();
    }

    public Optional<BaseEventBusBean> toActive(@NonNull BaseGtdEntity entity){
        return Optional.absent();
    }
    public Optional<BaseEventBusBean> toBeDone(@NonNull BaseGtdEntity entity){
        return Optional.absent();
    }
    public Optional<BaseEventBusBean> toBeUseless(@NonNull BaseGtdEntity entity){
        return Optional.absent();
    }
    public Optional<BaseEventBusBean> toBeReference(@NonNull BaseGtdEntity entity){
        return Optional.absent();
    }
    //it must be done in 24 hour from now
    public Optional<BaseEventBusBean> toBeScheduleJob(@NonNull BaseGtdEntity entity){
        return Optional.absent();
    }
    //it will be done in 2 mins
    public Optional<BaseEventBusBean> toBeQuarterJob(@NonNull BaseGtdEntity entity){
        return Optional.absent();
    }
    //it is a multi-step job
    public Optional<BaseEventBusBean> toBeProject(@NonNull BaseGtdEntity entity){
        return Optional.absent();
    }
    //it could be reminder by calendar
    public Optional<BaseEventBusBean> toBeCalendarJob(@NonNull BaseGtdEntity entity){
        return Optional.absent();
    }
    //it will be not fixed, but it is useful
    public Optional<BaseEventBusBean> toBeWishJob(@NonNull BaseGtdEntity entity){
        return Optional.absent();
    }
    //it will be fix by me as soon as I can
    public Optional<BaseEventBusBean> toBeDeferJob(@NonNull BaseGtdEntity entity){
        return Optional.absent();
    }
    //it will be fix by someone else
    public Optional<BaseEventBusBean> toBeDelegateJob(@NonNull BaseGtdEntity entity){
        return Optional.absent();
    }

    protected GtdMachine getGtdMachine(){
        return gtdMachine;
    }
}
