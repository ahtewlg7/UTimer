package ahtewlg7.utimer.storagerw;

import ahtewlg7.utimer.entity.NoteEntity;
import ahtewlg7.utimer.entity.gtd.AGtdEntity;

/**
 * Created by lw on 2018/1/9.
 */

public interface IEntityWAction {
    public boolean saveEntity(NoteEntity entity);
    public boolean saveEntity(AGtdEntity entity);
}
