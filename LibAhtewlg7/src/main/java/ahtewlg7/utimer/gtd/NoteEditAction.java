package ahtewlg7.utimer.gtd;


import ahtewlg7.utimer.entity.gtd.NoteEntity;

/**
 * Created by lw on 2018/10/18.
 */
public class NoteEditAction extends BaseTxtEditAction<NoteEntity> {
    public static final String TAG = NoteEditAction.class.getSimpleName();

    public NoteEditAction(NoteEntity entity) {
        super(entity);
    }
}
