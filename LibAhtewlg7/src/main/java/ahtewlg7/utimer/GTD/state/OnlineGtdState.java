package ahtewlg7.utimer.GTD.state;

import ahtewlg7.utimer.entity.gtd.GtdInboxEntity;
import ahtewlg7.utimer.entity.gtd.GtdOnlineEntity;
import ahtewlg7.utimer.enumtype.GtdErrCode;
import ahtewlg7.utimer.exception.GtdMachineException;
import ahtewlg7.utimer.util.Logcat;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * Created by lw on 2017/10/4.
 */

public class OnlineGtdState extends TrashGtdState {
    public static final String TAG = OnlineGtdState.class.getSimpleName();

    public OnlineGtdState(GtdStateMachine gtdStateMachine){
        super(gtdStateMachine);
    }

    @Override
    void toWatch(){
        if(gtdEntity == null)
            throw new GtdMachineException(GtdErrCode.ERR_GTDENTITY_NULL);

        Observable.just((GtdOnlineEntity)gtdEntity).doOnNext(new Consumer<GtdOnlineEntity>() {
            @Override
            public void accept(GtdOnlineEntity gtdOnlineEntity) throws Exception {
                if(gtdOnlineEntity.isWatched()){
                    Logcat.d(TAG,"to Watch this task");
                }else{
                    Logcat.d(TAG,"not to Watch this task");
                }
            }
        });
    }

    @Override
    void toStar(){
        if(gtdEntity == null)
            throw new GtdMachineException(GtdErrCode.ERR_GTDENTITY_NULL);

        Observable.just((GtdOnlineEntity)gtdEntity).doOnNext(new Consumer<GtdOnlineEntity>() {
        @Override
        public void accept(GtdOnlineEntity gtdOnlineEntity) throws Exception {
            if(gtdOnlineEntity.isStared()){
                Logcat.d(TAG,"to star this task");
            }else{
                Logcat.d(TAG,"not to star this task");
            }
            }
        });
    }

    @Override
    void toFork(){
        if(gtdEntity == null)
            throw new GtdMachineException(GtdErrCode.ERR_GTDENTITY_NULL);

        Observable.just((GtdOnlineEntity)gtdEntity).doOnNext(new Consumer<GtdOnlineEntity>() {
            @Override
            public void accept(GtdOnlineEntity gtdOnlineEntity) throws Exception {
                if(gtdOnlineEntity.isForked()){
                    Logcat.d(TAG,"to fork this task");
                }else{
                    Logcat.d(TAG,"not to fork this task");
                }
            }
        });
    }

    @Override
    GtdInboxEntity toIncoming(){
        if(gtdEntity == null)
            throw new GtdMachineException(GtdErrCode.ERR_GTDENTITY_NULL);
        else if(!((GtdOnlineEntity)gtdEntity).incomable())
            throw new GtdMachineException(GtdErrCode.ERR_INCOMING_DISABLE);
        return gtdStateMachine.getInboxGtdState().setCurrGtdEntity(gtdEntity).toIncoming();
    }

    /*@Override
    void toMaybe() throws RuntimeException{
        if(gtdEntity == null)
            throw new GtdMachineException(GtdErrCode.ERR_GTDENTITY_NULL);

    }*/
}
