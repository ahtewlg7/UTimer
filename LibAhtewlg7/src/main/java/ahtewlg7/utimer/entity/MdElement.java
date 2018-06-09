package ahtewlg7.utimer.entity;

import in.uncod.android.bypass.Element;

public class MdElement extends Element {
    public static final String TAG = MdElement.class.getSimpleName();

    protected CharSequence mdCharSequence;

    public MdElement(String text, int type) {
        super(text, type);
    }

    public void setMdCharSequence(CharSequence mdCharSequence) {
        this.mdCharSequence = mdCharSequence;
    }

    public CharSequence getMdCharSequence(){
        return mdCharSequence;
    }

}
