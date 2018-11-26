package ahtewlg7.utimer.entity.md;

import android.text.TextUtils;

import com.google.common.collect.Range;

import in.uncod.android.bypass.Element;

public class EditElement {
    public static final String TAG = EditElement.class.getSimpleName();

    protected String rawText;
    protected String modifyRawText;
    protected Element mdElement;
    protected Range<Integer> mdCharRange;
    protected Range<Integer> rawCharRange;

    protected CharSequence mdCharSequence;

    public EditElement(String rawText){
        this.rawText   = rawText;
        setMdCharSequence(rawText);
    }

    public String getRawText(){
        return rawText;
    }

    public String getModifyRawText() {
        return modifyRawText;
    }

    public void setModifyRawText(String modifyRawText) {
        this.modifyRawText = modifyRawText;
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

    public Range<Integer> getMdCharRange() {
        return mdCharRange;
    }

    public void setMdCharRange(Range<Integer> mdCharRange) {
        this.mdCharRange = mdCharRange;
    }

    public Range<Integer> getRawCharRange() {
        return rawCharRange;
    }

    public void setRawCharRange(Range<Integer> rawCharRange) {
        this.rawCharRange = rawCharRange;
    }
}
