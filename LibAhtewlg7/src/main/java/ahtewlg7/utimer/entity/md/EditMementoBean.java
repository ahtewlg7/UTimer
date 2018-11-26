package ahtewlg7.utimer.entity.md;

import java.util.List;

import ahtewlg7.utimer.entity.IValidEntity;
import ahtewlg7.utimer.enumtype.ElementEditType;

/**
 * Created by lw on 2018/10/21.
 */
public class EditMementoBean implements IValidEntity {
    public static final String TAG = EditMementoBean.class.getSimpleName();

    private int size;
    private int index;
    private List<EditElement> elementList;
    private ElementEditType editType;

    public EditMementoBean(int index, ElementEditType editType, List<EditElement> elementList) {
        this.index       = index;
        this.editType    = editType;
        this.elementList = elementList;
        size = elementList == null ? 0 : elementList.size();
    }

    @Override
    public boolean ifValid() {
        return index >= 0 && editType != null && elementList != null && !elementList.isEmpty();
    }

    public int getIndex() {
        return index;
    }

    public int getSize() {
        return size;
    }

    public ElementEditType getEditType() {
        return editType;
    }

    public List<EditElement> getElementList() {
        return elementList;
    }

    @Override
    public String toString() {
        return TAG + " index=" + index + " editType=" + editType.name() + " elementList.size=" + elementList.size();
    }
}
