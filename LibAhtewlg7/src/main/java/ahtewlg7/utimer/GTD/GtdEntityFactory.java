package ahtewlg7.utimer.GTD;

import android.text.TextUtils;

import ahtewlg7.utimer.common.IdAction;
import ahtewlg7.utimer.entity.gtd.AGtdEntity;
import ahtewlg7.utimer.entity.gtd.GtdInboxEntity;
import ahtewlg7.utimer.entity.gtd.GtdNewEntity;
import ahtewlg7.utimer.enumtype.GtdType;
import ahtewlg7.utimer.mvp.IGtdInfoMvpM;
import ahtewlg7.utimer.mvp.IRecyclerViewMvpM;
import ahtewlg7.utimer.storagerw.EntityDbAction;
import ahtewlg7.utimer.util.Logcat;
import io.reactivex.Flowable;

/**
 * Created by lw on 2017/12/28.
 */

public class GtdEntityFactory implements IRecyclerViewMvpM<AGtdEntity>,IGtdInfoMvpM <AGtdEntity>{
    public static final String TAG = GtdEntityFactory.class.getSimpleName();

    private IdAction idAction;
    private EntityDbAction dbAction;

    public GtdEntityFactory() {
        idAction = new IdAction();
        dbAction = new EntityDbAction();
    }

    @Override
    public Flowable<AGtdEntity> loadAll(){
        return dbAction.loadGtdEntity();
    }

    @Override
    public Flowable<AGtdEntity> loadEntity(String entityID) {
        return loadEntity(Flowable.just(entityID));
    }

    @Override
    public Flowable<AGtdEntity> loadEntity(Flowable<String> idObservable){
        return dbAction.getGtdEntity(idObservable);
    }

    public boolean saveGtdTaskEntity(AGtdEntity gtdEntity){
        boolean saveByDb      = dbAction.saveEntity(gtdEntity);
        Logcat.i(TAG,"saveGtdTaskEntity saveByDb = " + saveByDb);
        return saveByDb;
    }

    public AGtdEntity createGtdEntity(GtdType gtdType){
        return createGtdEntity(gtdType, idAction.getGtdId());
    }

    public AGtdEntity createGtdEntity(GtdType gtdType, String id){
        if(TextUtils.isEmpty(id)) {
            Logcat.i(TAG,"createGtdEntity : id null , cancel");
            return null;
        }
        AGtdEntity gtdEntity = null;
        switch(gtdType){
            case NEW:
                gtdEntity = new GtdNewEntity();
                break;
            case INBOX:
                gtdEntity = new GtdInboxEntity();
                break;
        }
        if(gtdEntity != null)
            gtdEntity.setId(id);
        return gtdEntity;
    }
}
