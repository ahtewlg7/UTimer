package ahtewlg7.utimer.common;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import java.util.List;

import ahtewlg7.utimer.entity.ABaseMaterialEntity;
import ahtewlg7.utimer.enumtype.StorageType;
import ahtewlg7.utimer.exception.UtimerEditException;
import ahtewlg7.utimer.util.MySafeFlowableOnSubscribe;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.schedulers.Schedulers;

import static ahtewlg7.utimer.enumtype.errcode.NoteEditErrCode.ERR_EDIT_ATTACH_FILE_NOT_READY;
import static ahtewlg7.utimer.enumtype.errcode.NoteEditErrCode.ERR_EDIT_ENTITY_NOT_READY;

/**
 * Created by lw on 2019/4/28.
 */
public class UTimerRawReadAction {

    public Flowable<String> toReadRaw(ABaseMaterialEntity entity){
        if(entity.getStorageType() == StorageType.TXT|| entity.getStorageType() == StorageType.MD)
            return toReadTxt(entity);
        return Flowable.empty();
    }

    private Flowable<String> toReadDb(final ABaseMaterialEntity entity){
        if(entity.getStorageType() == StorageType.TXT|| entity.getStorageType() == StorageType.MD)
            return Flowable.empty();
        return Flowable.empty();
    }
    private Flowable<String> toReadTxt(final ABaseMaterialEntity entity){
        if(entity.getStorageType() == StorageType.TXT|| entity.getStorageType() == StorageType.MD)
            return Flowable.empty();
        return Flowable.create(new MySafeFlowableOnSubscribe<String>() {
            @Override
            public void subscribe(FlowableEmitter<String> e) throws Exception {
                super.subscribe(e);
                try{
                    if(!ifEntityReady(entity))
                        e.onError(new UtimerEditException(ERR_EDIT_ENTITY_NOT_READY));
                    if(!ifAttachFileReady(entity))
                        e.onError(new UtimerEditException(ERR_EDIT_ATTACH_FILE_NOT_READY));

                    List<String> rawList = Files.readLines(entity.getAttachFile().getFile(), Charsets.UTF_8);
                    for(String raw : rawList)
                        e.onNext(raw);
                    e.onComplete();
                }catch (Exception exc){
                    e.onError(exc.getCause());
                }
            }
        }, BackpressureStrategy.MISSING).subscribeOn(Schedulers.io());
    }

    private boolean ifEntityReady(ABaseMaterialEntity utimerEntity){
        return utimerEntity != null && utimerEntity.ifValid();
    }
    private boolean ifAttachFileReady(ABaseMaterialEntity utimerEntity){
        return ifEntityReady(utimerEntity) && utimerEntity.ifRawReadable();
    }
}
