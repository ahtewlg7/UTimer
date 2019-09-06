package ahtewlg7.utimer.entity.md;

import com.google.common.base.Optional;

import java.util.List;

import ahtewlg7.utimer.enumtype.ElementEditType;

/**
 * Created by lw on 2018/10/21.
 */
public class EditMementoBean {
    public static final String TAG = EditMementoBean.class.getSimpleName();

    private int size;
    private int index;
    private StringBuilder mdDocumentTxt;
    private StringBuilder rawDocumentTxt;
    private List<EditElement> elementList;
    private ElementEditType editType;

    public EditMementoBean(int index, ElementEditType editType, List<EditElement> elementList) {
        this.index       = index;
        this.editType    = editType;
        this.elementList = elementList;
        mdDocumentTxt    = new StringBuilder();
        rawDocumentTxt   = new StringBuilder();
        size = elementList == null ? 0 : elementList.size();
        initDocTxt();
    }

    public boolean ifValid() {
        return index >= 0 && editType != null && elementList != null && !elementList.isEmpty();
    }
    public Optional<String> getMdDocTxt(){
        return Optional.of(mdDocumentTxt.toString());
    }
    public Optional<String> getRawDocTxt(){
        return Optional.of(rawDocumentTxt.toString());
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

    private void initDocTxt(){
        if(!ifValid())
            return;
        for(EditElement element : elementList) {
            mdDocumentTxt.append(element.getMdCharSequence());
            rawDocumentTxt.append(element.getRawText());
        }
    }
    @Override
    public String toString() {
        return TAG + " index=" + index + " editType=" + editType.name() + " elementList.size=" + elementList.size();
    }
}
