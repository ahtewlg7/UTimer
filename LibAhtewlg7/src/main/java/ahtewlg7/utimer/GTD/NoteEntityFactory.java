package ahtewlg7.utimer.GTD;

import org.joda.time.DateTime;

import ahtewlg7.utimer.common.IdAction;
import ahtewlg7.utimer.entity.NoteEntity;
import ahtewlg7.utimer.storagerw.EntityDbAction;
import ahtewlg7.utimer.util.DateTimeAction;
import ahtewlg7.utimer.util.Logcat;
import io.reactivex.Flowable;

/**
 * Created by lw on 2017/12/28.
 */

public class NoteEntityFactory{
    public static final String TAG = NoteEntityFactory.class.getSimpleName();

    private EntityDbAction dbAction;
    private DateTimeAction dateTimeAction;

    public NoteEntityFactory() {
        dbAction       = new EntityDbAction();
        dateTimeAction = new DateTimeAction();
    }

    public Flowable<NoteEntity> loadEntity(Flowable<String> idObservable) {
        return dbAction.getNoteEntity(idObservable);
    }

    public NoteEntity createNoteEntity(){
        Logcat.i(TAG,"createNoteEntity");
        String id = new IdAction().getNoteId();
        DateTime now = dateTimeAction.toNow();

        NoteEntity noteEntity = new NoteEntity();
        noteEntity.setId(id);
        noteEntity.setNoteName(dateTimeAction.toFormat(now));
        noteEntity.setCreateDateTime(now);
        noteEntity.setLastModifyDateTime(now);
        return noteEntity;
    }

    public boolean saveNoteEntity(NoteEntity noteEntity){
        boolean saveByDb = dbAction.saveEntity(noteEntity);
        Logcat.i(TAG,"saveNoteEntity saveByDb = " + saveByDb) ;
        return saveByDb;
    }
}
