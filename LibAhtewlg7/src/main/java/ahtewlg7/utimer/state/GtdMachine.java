package ahtewlg7.utimer.state;


import com.google.common.base.Optional;

import ahtewlg7.utimer.entity.AUtimerEntity;
import ahtewlg7.utimer.state.un.BaseGtdState;
import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

public class GtdMachine {
    public static final String TAG = GtdMachine.class.getSimpleName();

    private static GtdMachine instance;

    private BaseGtdState inboxState;
    private BaseGtdState actionState;
    private BaseGtdState projectState;
    private BaseGtdState dailyState;

    private GtdMachine(){
    }

    public static GtdMachine getInstance(){
        if(instance == null)
            instance = new GtdMachine();
        return instance;
    }

    public Flowable<Optional<ABaseGtdState>> getCurrState(@NonNull Flowable<AUtimerEntity> entityFlowable){
        return entityFlowable.map(new Function<AUtimerEntity, Optional<ABaseGtdState>>() {
            @Override
            public Optional<ABaseGtdState> apply(AUtimerEntity aUtimerEntity) throws Exception {
                return getCurrState(aUtimerEntity);
            }
        });
    }


    /*public Flowable<Optional<AUtimerEntity>> toDailyCheck(@NonNull Flowable<AUtimerEntity> entityFlowable){
        return entityFlowable.map(new Function<AUtimerEntity, Optional<AUtimerEntity>>() {
            @Override
            public Optional<AUtimerEntity> apply(AUtimerEntity t) throws Exception {
                Optional<ABaseGtdState> currState = getCurrState(t);
                if(!currState.isPresent())
                    return Optional.of(t);
                return currState.get().toDailyCheck(t);
            }
        });
    }*/



    protected Optional<ABaseGtdState> getCurrState(AUtimerEntity utimerEntity){
        if(utimerEntity == null)
            return Optional.absent();

        ABaseGtdState currGtdSate = null;
        switch (utimerEntity.getGtdType()){
            case SHORTHAND:
                currGtdSate = new ShortHandGtdState(utimerEntity);
                break;
            /*case ACTION:
                currGtdSate = getActionState();
                break;
            case PROJECT:
                currGtdSate = getProjectState();
                break;
            case DAILY:
                currGtdSate = getDailyState();
                break;*/
        }
        return Optional.fromNullable(currGtdSate);
    }

}
