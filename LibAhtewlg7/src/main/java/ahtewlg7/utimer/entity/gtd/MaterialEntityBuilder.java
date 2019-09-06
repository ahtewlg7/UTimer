package ahtewlg7.utimer.entity.gtd;

import androidx.annotation.NonNull;

import ahtewlg7.utimer.db.entity.MaterialEntityGdBean;
import ahtewlg7.utimer.entity.ABaseMaterialEntityBuilder;

/**
 * Created by lw on 2018/10/26.
 */
public class MaterialEntityBuilder extends ABaseMaterialEntityBuilder<MaterialEntity, MaterialEntityBuilder> {
    public static final String TAG = MaterialEntityBuilder.class.getSimpleName();

    protected GtdProjectEntity projectEntity;
    protected MaterialEntityGdBean gdBean;

    public MaterialEntityBuilder setProjectEntity(GtdProjectEntity projectEntity) {
        this.projectEntity = projectEntity;
        return this;
    }
    public MaterialEntityBuilder setGbBean(MaterialEntityGdBean gdBean){
        this.gdBean = gdBean;
        return this;
    }


    @NonNull
    @Override
    public MaterialEntity build() {
        return new MaterialEntity(this);
    }
}
