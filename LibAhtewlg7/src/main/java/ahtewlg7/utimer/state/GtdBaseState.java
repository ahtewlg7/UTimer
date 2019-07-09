package ahtewlg7.utimer.state;

import androidx.annotation.NonNull;

import com.google.common.base.Optional;

import ahtewlg7.utimer.entity.AUtimerEntity;
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
        Optional<GtdDeedEntity> deedEntityOptional =
                GtdDeedByUuidFactory.getInstance().create(title, detail, INBOX);
        if(!deedEntityOptional.isPresent())
            return Optional.absent();
        BaseEventBusBean actionBusEvent = new DeedBusEvent(GtdBusEventType.SAVE, deedEntityOptional.get());
        EventBusFatory.getInstance().getDefaultEventBus().postSticky(actionBusEvent);
        return Optional.of(actionBusEvent);
    }

    public Optional<BaseEventBusBean> toTrash(@NonNull AUtimerEntity entity){
        return Optional.absent();
    }

    public Optional<BaseEventBusBean> toActive(@NonNull AUtimerEntity entity){
        return Optional.absent();
    }
    public Optional<BaseEventBusBean> toBeDone(@NonNull AUtimerEntity entity){
        return Optional.absent();
    }
    public Optional<BaseEventBusBean> toBeUseless(@NonNull AUtimerEntity entity){
        return Optional.absent();
    }
    public Optional<BaseEventBusBean> toBeReference(@NonNull AUtimerEntity entity){
        return Optional.absent();
    }
    //it will be done in 2 mins
    public Optional<BaseEventBusBean> toBeQuarterJob(@NonNull AUtimerEntity entity){
        return Optional.absent();
    }
    //it is a multi-step job
    public Optional<BaseEventBusBean> toBeProject(@NonNull AUtimerEntity entity){
        return Optional.absent();
    }
    //it could be reminder by calendar
    public Optional<BaseEventBusBean> toBeCalendarJob(@NonNull AUtimerEntity entity){
        return Optional.absent();
    }
    //it will be not fixed, but it is useful
    public Optional<BaseEventBusBean> toBeWishJob(@NonNull AUtimerEntity entity){
        return Optional.absent();
    }
    //it will be fix by me as soon as I can
    public Optional<BaseEventBusBean> toBeDeferJob(@NonNull AUtimerEntity entity){
        return Optional.absent();
    }
    //it will be fix by someone else
    public Optional<BaseEventBusBean> toBeDelegateJob(@NonNull AUtimerEntity entity){
        return Optional.absent();
    }

    protected GtdMachine getGtdMachine(){
        return gtdMachine;
    }
}
