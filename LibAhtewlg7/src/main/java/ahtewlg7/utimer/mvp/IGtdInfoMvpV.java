package ahtewlg7.utimer.mvp;

import ahtewlg7.utimer.entity.gtd.AGtdEntity;

/**
 * Created by lw on 2018/3/17.
 */

public interface IGtdInfoMvpV {
    public void updateView(AGtdEntity gtdEntity);

    public void onIdEnptyErr();
    public void onInfoStart();
    public void onInfoErr();
    public void onInfoEnd();
}
