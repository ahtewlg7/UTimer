package ahtewlg7.utimer.gtd;

import com.google.common.base.Optional;

import ahtewlg7.utimer.entity.AUtimerEntity;
import ahtewlg7.utimer.entity.un.IUtimerEntity;
import ahtewlg7.utimer.entity.gtd.GtdProjectEntity;
import ahtewlg7.utimer.entity.gtd.ShortHandEntity;
import ahtewlg7.utimer.entity.gtd.TipsEntity;
import ahtewlg7.utimer.entity.material.AMaterialEntity;
import ahtewlg7.utimer.enumtype.GtdType;
import ahtewlg7.utimer.util.Logcat;
import io.reactivex.Observable;
import io.reactivex.functions.Function;

public class GtdInboxState extends BaseGtdState {
    public static final String TAG = GtdInboxState.class.getSimpleName();

    public GtdInboxState(GtdMachine gtdMachine){
        super(gtdMachine);
    }

    @Override
    public Observable<ShortHandEntity> toInbox(Observable<AMaterialEntity> materialObservable) {
        return materialObservable.map(new Function<AMaterialEntity, ShortHandEntity>() {
            @Override
            public ShortHandEntity apply(AMaterialEntity materialEntity) throws Exception {

                return null;
            }
        });
    }

    @Override
    public Observable<Optional<GtdProjectEntity>> toProject(Observable<AUtimerEntity> inboxObservable) {
        return inboxObservable.map(new Function<AUtimerEntity, Optional<GtdProjectEntity>>() {
            @Override
            public Optional<GtdProjectEntity> apply(AUtimerEntity entity) throws Exception {
                if(entity == null || !entity.ifValid()){
                    Logcat.d(TAG,"toProject cancel");
                    return Optional.absent();
                }

                if(entity.getGtdType() == GtdType.TIPS)
                    return Optional.of(new GtdProjectEntity((TipsEntity)entity));
                else if(entity.getGtdType() == GtdType.SHORTHAND)
                    return Optional.of(new GtdProjectEntity((ShortHandEntity)entity));
                return Optional.absent();
            }
        });
    }

    @Override
    public void toAddAction(IUtimerEntity utimerEntity) {
        super.toAddAction(utimerEntity);
    }
}
