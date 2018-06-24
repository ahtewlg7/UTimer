package ahtewlg7.utimer.entity;

import com.google.common.collect.Range;

import in.uncod.android.bypass.Element;

public class MdElement{
    public static final String TAG = MdElement.class.getSimpleName();

    protected int startLine;
    protected String rawText;
    protected Element mdElement;
    protected Range<Integer> mdCharRange;
    protected Range<Integer> rawCharRange;

    protected CharSequence mdCharSequence;

    public MdElement(String rawText){
        this.rawText   = rawText;
        setMdCharSequence(rawText);
    }

    public MdElement(int startLine, String rawText){
        this.rawText   = rawText;
        this.startLine = startLine;
        setMdCharSequence(rawText);
    }

    public MdElement(Element mdElement) {
        this.mdElement = mdElement;
    }

    public MdElement(int startLine, Element mdElement) {
        this.startLine = startLine;
        this.mdElement = mdElement;
    }

    public String getText(){
        return rawText;
    }

    public Element getElement() {
        return mdElement;
    }

    public void setMdCharSequence(CharSequence mdCharSequence) {
        this.mdCharSequence = mdCharSequence;
    }

    public CharSequence getMdCharSequence(){
        return mdCharSequence;
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
