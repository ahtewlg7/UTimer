package ahtewlg7.utimer.entity.md;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by lw on 2018/10/21.
 */
public class EditMementoCaretaker {
    public static final String TAG = EditMementoCaretaker.class.getSimpleName();

    private List<EditMementoBean> mementoUndoList;
    private List<EditMementoBean> mementoRedoList;

    public EditMementoCaretaker() {
        mementoUndoList = Lists.newArrayList();
        mementoRedoList = Lists.newArrayList();
    }

    public void saveMemento(EditMementoBean memento){
        mementoUndoList.add(memento);
        mementoRedoList.clear();
    }

    public Optional<EditMementoBean> getTopDo(){
        return Optional.fromNullable(mementoRedoList.get(mementoRedoList.size() - 1));
    }

    public Optional<EditMementoBean> popNextUndo(){
        if(mementoUndoList.size() >= 1){
            EditMementoBean memento = mementoUndoList.remove(mementoUndoList.size() -1);
            mementoRedoList.add(memento);
            return Optional.of(memento);
        }
        return Optional.absent();
    }
    public Optional<EditMementoBean> popNextRedo(){
        if(mementoRedoList.size() >= 1){
            EditMementoBean memento = mementoRedoList.remove(mementoRedoList.size() -1);
            mementoUndoList.add(memento);
            return Optional.of(memento);
        }
        return Optional.absent();
    }
}
