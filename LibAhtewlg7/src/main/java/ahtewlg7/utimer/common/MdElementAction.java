package ahtewlg7.utimer.common;

import ahtewlg7.utimer.entity.md.EditElement;
import ahtewlg7.utimer.mvp.un.NoteMdEditMvpP;
import io.reactivex.annotations.NonNull;

public class MdElementAction implements NoteMdEditMvpP.INoteMdEditMvpM {

    @Override
    public CharSequence handleElement(@NonNull EditElement element) {
        return element.getMdCharSequence();
    }
}
