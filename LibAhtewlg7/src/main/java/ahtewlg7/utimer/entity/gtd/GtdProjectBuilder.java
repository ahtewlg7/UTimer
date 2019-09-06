package ahtewlg7.utimer.entity.gtd;

import androidx.annotation.NonNull;

import ahtewlg7.utimer.db.entity.ProjectEntityGdBean;
import ahtewlg7.utimer.entity.ABaseMaterialEntityBuilder;

/**
 * Created by lw on 2018/10/26.
 */
public class GtdProjectBuilder extends ABaseMaterialEntityBuilder<GtdProjectEntity, GtdProjectBuilder> {
    protected ProjectEntityGdBean gdBean;

    public GtdProjectBuilder setGdBean(ProjectEntityGdBean gdBean) {
        this.gdBean = gdBean;
        return this;
    }

    @NonNull
    @Override
    public GtdProjectEntity build() {
        return new GtdProjectEntity(this);
    }
}
