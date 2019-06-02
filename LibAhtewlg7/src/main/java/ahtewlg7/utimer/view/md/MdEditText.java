package ahtewlg7.utimer.view.md;

import android.content.Context;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.inputmethod.InputMethodManager;

import com.google.common.collect.Range;

import ahtewlg7.utimer.md.ClickableMovementMethod;
import ahtewlg7.utimer.util.AndrManagerFactory;
import io.reactivex.annotations.NonNull;

/**
 * Created by lw on 2016/5/18.
 */
public class MdEditText extends android.support.v7.widget.AppCompatEditText {
    public static  final int INVALID_LINE_INDEX     = -1;
    public static  final int DEFAULT_PADDING_LEFT   = 20;
    public static  final int DEFAULT_PADDING_RIGHT  = 0;
    public static  final int DEFAULT_PADDING_TOP    = 0;
    public static  final int DEFAULT_PADDING_BOTTOM = 0;

    private InputMethodManager imm;

    public MdEditText(Context context) {
        super(context);
        init();
    }
    public MdEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public MdEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public int getCurrLineIndex() {
        int selectionStart = getSelectionStart();
        Layout layout = getLayout();
        if (layout != null && selectionStart != INVALID_LINE_INDEX)
            return layout.getLineForOffset(selectionStart);
        return INVALID_LINE_INDEX;
    }
    public int getLineStartOffset(int line){
        if(line == INVALID_LINE_INDEX || line > getLineCount())
            return INVALID_LINE_INDEX;
        Layout layout = getLayout();
        if (layout != null)
            return layout.getLineStart(line);
        return INVALID_LINE_INDEX;
    }
    public int getLineEndOffset(int line){
        if(line == INVALID_LINE_INDEX || line > getLineCount())
            return INVALID_LINE_INDEX;
        if(line == getLineCount())
            return getText().length();

        int nextLineStartOffset = getLineStartOffset(line + 1);
        if( nextLineStartOffset != INVALID_LINE_INDEX)
            return nextLineStartOffset - 1;
        return INVALID_LINE_INDEX;
    }

    public int getCursorColumnIndex(int line) {
        if(line == INVALID_LINE_INDEX || line > getLineCount())
            return INVALID_LINE_INDEX;

        int selectionStart = getSelectionStart();
        Layout layout = getLayout();
        if (layout != null && selectionStart != INVALID_LINE_INDEX)
            return selectionStart - layout.getLineStart(line);
        return INVALID_LINE_INDEX;
    }

    public void setCursorToStart(){
        setCursorIndex(0);
    }
    public void setCursorToEnd(){
        if(TextUtils.isEmpty(getText()))
            setCursorIndex(0);
        else
            setCursorIndex(getText().length());
    }
    public void setCursorIndex(int index){
        CharSequence text = getText();
        if(text instanceof Spannable) {
            int tmp = index;
            if(index < 0)
                tmp = 0;
            else if(index > text.length())
                tmp = text.length();
            Selection.setSelection((Spannable)text, tmp);

        }
    }

    public void enableEdit(boolean enable,boolean immFlag){
        if(enable){
            setFocusableInTouchMode(true);
            setFocusable(true);
            requestFocus();
            if(immFlag)
                imm.showSoftInput(MdEditText.this, InputMethodManager.SHOW_FORCED);
        }else{
            setFocusableInTouchMode(false);
            setFocusable(false);
            clearFocus();
            if(immFlag)
                imm.hideSoftInputFromWindow(getWindowToken(),0);
        }
    }
    public boolean ifEditable(){
        return isFocusable();
    }

    public void goToNewLine(){
        insert("\n");
    }

    public boolean ifHasSelected(){
        int stCursorPosition  = getSelectionStart();
        int endCursorPosition = getSelectionEnd();
        return stCursorPosition != endCursorPosition;
    }

    public void setAlignLeft(){
        setGravity(Gravity.LEFT);
    }
    public void setAlignCenter(){
        setGravity(Gravity.CENTER);
    }
    public void setAlignRight(){
        setGravity(Gravity.RIGHT);
    }
    public void insert(String content){
        int cursorPosition = getSelectionStart();
        if(cursorPosition < 0)
            cursorPosition = 0;
        getEditableText().insert(cursorPosition, content);
        setSelection(cursorPosition + content.length());
    }

    public void insert(String content, int startPosition){
        int cursorPosition = getSelectionStart();
        getEditableText().insert(startPosition, content);
        setSelection(cursorPosition + content.length());
    }

    public void insert(String stContent, String endContent){
        int stCursorPosition  = getSelectionStart();
        int endCursorPosition = getSelectionEnd();
        getEditableText().insert(stCursorPosition, stContent);
        getEditableText().insert(endCursorPosition + stContent.length(), endContent);
        setSelection(endCursorPosition + stContent.length() + endContent.length());
    }

    public void replace(@NonNull Range<Integer> srcRange, @NonNull String replaceContent){
        getEditableText().replace(srcRange.lowerEndpoint(), srcRange.upperEndpoint(),replaceContent);
    }

    public void replace(@NonNull Range<Integer> srcRange, @NonNull String replaceContent, @NonNull Range<Integer> replaceRange){
        getEditableText().replace(srcRange.lowerEndpoint(), srcRange.upperEndpoint(),
                replaceContent, replaceRange.lowerEndpoint(),replaceRange.upperEndpoint());
    }

    private void init(){
        setCursorVisible(true);
        setPadding(DEFAULT_PADDING_LEFT,DEFAULT_PADDING_TOP,DEFAULT_PADDING_RIGHT,DEFAULT_PADDING_BOTTOM);
        setMovementMethod(ClickableMovementMethod.getInstance());

        imm = new AndrManagerFactory().getInputMethodManager();
    }
}
