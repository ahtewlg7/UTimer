package ahtewlg7.utimer.view.md;

import android.content.Context;
import android.text.Editable;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;

import ahtewlg7.utimer.R;
import ahtewlg7.utimer.md.ClickableMovementMethod;
import ahtewlg7.utimer.util.Logcat;
import io.github.mthli.knife.KnifeText;

/**
 * Created by lw on 2016/5/18.
 */
public class MdEditorEditText extends KnifeText {
    public static final String TAG = MdEditorEditText.class.getSimpleName();

    private String initedHistory;
    private GestureDetector gestureDetector;

    public MdEditorEditText(Context context) {
        super(context);
        enableEdit(false);
        setMovementMethod(ClickableMovementMethod.getInstance());
    }
    public MdEditorEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        enableEdit(false);
        setMovementMethod(ClickableMovementMethod.getInstance());
    }
    public MdEditorEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        enableEdit(false);
        setMovementMethod(ClickableMovementMethod.getInstance());
    }
    public MdEditorEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        enableEdit(false);
        setMovementMethod(ClickableMovementMethod.getInstance());
    }

    public void setGestureListener(GestureDetector.OnGestureListener gestureListener) {
        gestureDetector = new GestureDetector(Utils.getApp().getApplicationContext(), gestureListener);
    }

    public int getCurrentLineNum() {
        int selectionStart = getSelectionStart();
        Layout layout = getLayout();
        if (layout != null && selectionStart != -1) {
            return layout.getLineForOffset(selectionStart);
        }
        return -1;
    }
    public int getLineStartOffset(int line){
        if(line == -1 || line > getLineCount())
            return -1;
        Layout layout = getLayout();
        if (layout != null)
            return layout.getLineStart(line);
        return -1;
    }
    public int getLineEndOffset(int line){
        if(line == -1 || line > getLineCount())
            return -1;
        if(line == getLineCount())
            return getText().length();

        int nextLineStartOffset = getLineStartOffset(line + 1);
        if( nextLineStartOffset != -1)
            return nextLineStartOffset - 1;
        return -1;
    }

    public int getCursorColumnNum(int line) {
        if(line == -1 || line > getLineCount())
            return -1;

        int selectionStart = getSelectionStart();
        Layout layout = getLayout();
        if (layout != null && selectionStart != -1)
            return selectionStart - layout.getLineStart(line);
        return -1;
    }

    public void changeEditable(){
        if(ifEditable()){
            enableEdit(false);
            ToastUtils.showShort(R.string.editor_disable);
        }else{
            enableEdit(true);
            ToastUtils.showShort(R.string.editor_enable);
        }
    }
    public void enableEdit(boolean enable){
        if(enable){
            historyEnable = true;
            setFocusableInTouchMode(true);
            setFocusable(true);
        }else{
            historyEnable = false;
            setFocusableInTouchMode(false);
            setFocusable(false);
        }
    }
    public boolean ifEditable(){
        return isFocusable();
    }

    public void initHistory(String history){
        this.initedHistory = history;
    }
    public void setHistoryEnable(boolean historyEnable){
        this.historyEnable = historyEnable;
    }
    public boolean getHistoryEnable(){
        return historyEnable;
    }

    public void goToNewLine(){
        insert("\n");
    }

    public boolean ifHasSelected(){
        int stCursorPosition  = getSelectionStart();
        int endCursorPosition = getSelectionEnd();

        Logcat.d(TAG,"ifHasSelected stCursorPosition = " + stCursorPosition + ", endCursorPosition = " + endCursorPosition);
        if(stCursorPosition != endCursorPosition)
            return true;
        return false;
    }
    public void insert(String content){
        int cursorPosition = getSelectionStart();
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
    public void delete(int startPosition , int endPosition){
        if(startPosition > endPosition)
            return;
        getEditableText().delete(startPosition, endPosition);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER)
            goToNewLine();
        Logcat.d(TAG,"onKeyDown =" + event.toString());

        return super.onKeyDown(keyCode,event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(gestureDetector != null)
            gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    // Redo/Undo ===================================================================================
    @Override
    public void beforeTextChanged(CharSequence text, int start, int count, int after) {
        Logcat.d(TAG,"beforeTextChanged historyEnable = " + historyEnable + ",historyCursor = " + historyCursor);
        if(historyCursor > 0)
            super.beforeTextChanged(text,start,count,after);
        else if(historyCursor == 0){
            Logcat.d(TAG,"beforeTextChanged initedHistory = " + initedHistory);
            super.beforeTextChanged(initedHistory,0,initedHistory.length(),after);
        }
    }

    @Override
    public void onTextChanged(CharSequence text, int start, int before, int count) {
        Logcat.d(TAG,"onTextChanged historyEnable = " + historyEnable + ",historyCursor = " + historyCursor);
        super.onTextChanged(text,start,before,count);
    }

    @Override
    public void afterTextChanged(Editable text) {
        Logcat.d(TAG,"afterTextChanged historyEnable = " + historyEnable + ",historyCursor = " + historyCursor);
        super.afterTextChanged(text);
    }
}
