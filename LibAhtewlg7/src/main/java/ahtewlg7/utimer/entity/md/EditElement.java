package ahtewlg7.utimer.entity.md;

import android.text.TextUtils;

import in.uncod.android.bypass.Element;

public class EditElement {
    public static final String TAG = EditElement.class.getSimpleName();

    protected String rawText;
    protected Element mdElement;
    protected CharSequence mdCharSequence;

    public EditElement(String rawText){
        this.rawText   = rawText;
        setMdCharSequence(rawText);
    }

    public String getRawText(){
        return rawText;
    }


    public void setElement(Element mdElement) {
        this.mdElement = mdElement;
    }

    public Element getElement() {
        return mdElement;
    }

    public void setMdCharSequence(CharSequence mdCharSequence) {
        this.mdCharSequence = mdCharSequence;
    }

    public CharSequence getMdCharSequence(){
        return TextUtils.isEmpty(mdCharSequence) ? rawText: mdCharSequence;
    }
}
