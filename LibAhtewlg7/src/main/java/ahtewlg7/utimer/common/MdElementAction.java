package ahtewlg7.utimer.common;

import ahtewlg7.utimer.entity.MdElement;
import ahtewlg7.utimer.mvp.NoteMdEditMvpP;
import io.reactivex.annotations.NonNull;

public class MdElementAction implements NoteMdEditMvpP.INoteMdEditMvpM {

    @Override
    public CharSequence handleElement(@NonNull MdElement element) {
        return element.getMdCharSequence();
    }
}
