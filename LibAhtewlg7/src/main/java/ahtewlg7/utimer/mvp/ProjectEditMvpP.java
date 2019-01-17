package ahtewlg7.utimer.mvp;

import ahtewlg7.utimer.entity.gtd.GtdProjectEntity;

/**
 * Created by lw on 2018/10/18.
 */
public class ProjectEditMvpP {
    public static final String TAG = ProjectEditMvpP.class.getSimpleName();

    protected ProjectEditMvpV mvpV;
    protected GtdProjectEntity entity;

    public ProjectEditMvpP(ProjectEditMvpV mvpV, GtdProjectEntity entity) {
        this.mvpV   = mvpV;
        this.entity = entity;
    }

    public void toLoadItem(){

    }

    public class ProjectEditMvpM{



    }

    public interface ProjectEditMvpV{

    }
}
