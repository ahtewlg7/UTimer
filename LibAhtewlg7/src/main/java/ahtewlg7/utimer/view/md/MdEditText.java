package ahtewlg7.utimer.view.md;

import android.content.Context;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.blankj.utilcode.util.Utils;
import com.google.common.collect.Range;

import ahtewlg7.utimer.md.ClickableMovementMethod;
import ahtewlg7.utimer.util.Logcat;
import io.reactivex.annotations.NonNull;

/**
 * Created by lw on 2016/5/18.
 */
public class MdEditText extends android.support.v7.widget.AppCompatEditText {
    public static final String TAG = MdEditText.class.getSimpleName();

    public static  final int INVALID_LINE_INDEX = -1;

    private GestureDetector gestureDetector;

    public MdEditText(Context context) {
        super(context);
        setMovementMethod(ClickableMovementMethod.getInstance());
    }
    public MdEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setMovementMethod(ClickableMovementMethod.getInstance());
    }
    public MdEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setMovementMethod(ClickableMovementMethod.getInstance());
    }

    public void setGestureListener(GestureDetector.OnGestureListener gestureListener) {
        if(gestureListener != null)
            gestureDetector = new GestureDetector(Utils.getApp().getApplicationContext(), gestureListener);
        else
            gestureDetector = null;
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

    public void enableEdit(boolean enable){
        if(enable){
            setFocusableInTouchMode(true);
            setFocusable(true);
        }else{
            setFocusableInTouchMode(false);
            setFocusable(false);
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

        Logcat.d(TAG,"ifHasSelected stCursorPosition = " + stCursorPosition + ", endCursorPosition = " + endCursorPosition);
        return stCursorPosition != endCursorPosition;
    }
    public void insert(String content){
        int cursorPosition = getSelectionStart();
        getEditableText().insert(cursorPosition, content);
        setSelection(cursorPosition + content.length());
    }

    /*public void insert(String content, int startPosition){
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
    }*/

    public void replace(@NonNull Range<Integer> srcRange, @NonNull String replaceContent){
        getEditableText().replace(srcRange.lowerEndpoint(), srcRange.upperEndpoint(),replaceContent);
    }

    public void replace(@NonNull Range<Integer> srcRange, @NonNull String replaceContent, @NonNull Range<Integer> replaceRange){
        getEditableText().replace(srcRange.lowerEndpoint(), srcRange.upperEndpoint(),
                replaceContent, replaceRange.lowerEndpoint(),replaceRange.upperEndpoint());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(gestureDetector != null)
            gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}
