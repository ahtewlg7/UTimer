package ahtewlg7.utimer.entity.gtd;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.blankj.utilcode.util.FileUtils;

import java.util.ArrayList;
import java.util.List;

import ahtewlg7.utimer.enumtype.GtdErrCode;
import ahtewlg7.utimer.enumtype.GtdType;
import ahtewlg7.utimer.exception.GtdTrashException;
import io.reactivex.Observable;

/**
 * Created by lw on 2017/10/24.
 */

public class GtdMaterialEntity extends AGtdEntity{
    public static final String TAG = GtdMaterialEntity.class.getSimpleName();

    private List<String> linkedEntityIdList;

    public GtdMaterialEntity(@NonNull String fileRPath) {
        super();
        this.fileRPath       = fileRPath;
        linkedEntityIdList   = new ArrayList<String>();
    }

    @Override
    public GtdType getTaskType() {
        return GtdType.MATERIAL;
    }

    public int getReferenceNum() {
        return linkedEntityIdList.size();
    }

    public Observable<String> getLinkIdList(){
        return Observable.fromIterable(linkedEntityIdList);
    }

    public void addLink(AGtdEntity gtdEntity) throws RuntimeException{
        if(gtdEntity != null)
            addLink(gtdEntity.getId());
    }
    public void addLink(String gtdId) throws RuntimeException{
        if(TextUtils.isEmpty(getFileRPath()) && !FileUtils.isFileExists(getFileRPath()))
            throw new GtdTrashException(GtdErrCode.ERR_MATERIAL_NULL);
        if(!TextUtils.isEmpty(gtdId) && !linkedEntityIdList.contains(gtdId))
            linkedEntityIdList.add(gtdId);
    }

    public void removeLink(AGtdEntity gtdEntity) throws RuntimeException{
        if(gtdEntity != null)
            removeLink(gtdEntity.getId());
    }
    public void removeLink(String gtdId) throws RuntimeException{
        if(TextUtils.isEmpty(getFileRPath()) && !FileUtils.isFileExists(getFileRPath()))
            throw new GtdTrashException(GtdErrCode.ERR_MATERIAL_NULL);
        if(TextUtils.isEmpty(gtdId) && linkedEntityIdList.contains(gtdId))
            linkedEntityIdList.remove(gtdId);
    }
}
