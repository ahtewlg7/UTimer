package ahtewlg7.utimer.entity.md;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import java.util.List;

import ahtewlg7.utimer.enumtype.ElementEditType;
import ahtewlg7.utimer.util.Logcat;

/**
 * Created by lw on 2018/10/20.
 */
public class EditMementoOriginator {
    public static final String TAG = EditMementoOriginator.class.getSimpleName();

    private int preEditIndex = 0;
    private List<EditElement> elementList;

    public EditMementoOriginator() {
        elementList = Lists.newArrayList();
        elementList.add(new EditElement(""));
    }

    public EditMementoOriginator(List<EditElement> elementList) {
        this.elementList = elementList;
    }

    public int getPreEditIndex() {
        return getValidIndex(preEditIndex);
    }

    public List<EditElement> getMdElementList() {
        return elementList;
    }

    public Optional<EditMementoBean> createMemento(int index, ElementEditType editType, List<EditElement> mdElementList)
            throws RuntimeException {
        if (editType == null || mdElementList == null || mdElementList.isEmpty()) {
            Logcat.i(TAG, "createMemento err ：null");
            return Optional.absent();
        }
        EditMementoBean memento = null;
        switch (editType) {
            case DELETE:
                if (index >= 0 && index < elementList.size()) {
                    Logcat.i(TAG, "createMemento ：to del");
                    elementList.removeAll(mdElementList);
                    memento = new EditMementoBean(index, editType, mdElementList);
                }
                break;
            case MODIFY:
                if (index >= 0 && index < elementList.size()) {
                    elementList.remove(index);
                }
                elementList.addAll(getValidIndex(index), mdElementList);
                memento = new EditMementoBean(index, editType, mdElementList);
                break;
            case INSERT:
                elementList.addAll(getValidIndex(index), mdElementList);
                memento = new EditMementoBean(index, editType, mdElementList);
                break;
        }
        preEditIndex = index;
        return Optional.fromNullable(memento);
    }

    public boolean restoreMemento(EditMementoBean memento) throws RuntimeException {
        if (memento == null || !memento.ifValid()) {
            Logcat.i(TAG, "restoreMemento err ：null");
            return false;
        }
        switch (memento.getEditType()) {
            case DELETE:
                elementList.addAll(memento.getIndex(), memento.getElementList());
                break;
            case MODIFY:
                for(int num = 0, index = memento.getIndex() ; num < memento.getSize() ; num++, index++)
                    elementList.remove(index);
                elementList.addAll(memento.getIndex(), memento.getElementList());
                break;
            case INSERT:
                elementList.removeAll(memento.getElementList());
                break;
        }
        preEditIndex = memento.getIndex();
        return true;
    }

    private int getValidIndex(int index) {
        if (index < 0)
            return 0;
        else if (index >= elementList.size())
            return elementList.size();
        return index;
    }
}
