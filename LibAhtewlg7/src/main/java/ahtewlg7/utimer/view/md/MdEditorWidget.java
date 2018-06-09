package ahtewlg7.utimer.view.md;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.ToastUtils;

import ahtewlg7.utimer.R;
import ahtewlg7.utimer.md.IEditAction;
import ahtewlg7.utimer.util.AndrManagerFactory;

/**
 * Created by lw on 2016/5/17.
 */
public class MdEditorWidget extends RelativeLayout {
    public static final String TAG = MdEditorWidget.class.getSimpleName();

    public ImageButton actionUndo;
    public ImageButton actionRedo;
    public ImageButton actionBold;
    public ImageButton actionItalic;
    public ImageButton actionSubscript;
    public ImageButton actionSuperscript;
    public ImageButton actionStrikethrough;
    public ImageButton actionUnderline;
    public ImageButton actionHorizontalRule;
    public ImageButton actionHeading1;
    public ImageButton actionHeading2;
    public ImageButton actionHeading3;
    public ImageButton actionHeading4;
    public ImageButton actionHeading5;
    public ImageButton actionHeading6;
    public ImageButton actionTxtColor;
    public ImageButton actionTxtBgColor;
    public ImageButton actionIndent;
    public ImageButton actionOutdent;
    public ImageButton actionAlignLeft;
    public ImageButton actionAlignCenter;
    public ImageButton actionAlignRight;
    public ImageButton actionBlockquote;
    public ImageButton actionInsertImage;
    public ImageButton actionInsertLink;
    public ImageButton actionInsertUnorderedList;
    public ImageButton actionInsertOrderedList;
    public ImageButton actionInsertCheckbox;

    private boolean ifToast;
    private IEditAction mEditor;

    public MdEditorWidget(Context context) {
        super(context);
        findView();
    }
    public MdEditorWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        findView();
    }

    public void setEditorListener(IEditAction mEditor) {
        this.mEditor = mEditor;
    }

    public void setIfToast(boolean ifToast){
        this.ifToast = ifToast;
    }

    private void findView(){
        View layout = null;
        try{
            layout = new AndrManagerFactory().getLayoutInflater().inflate(R.layout.view_editor_widget, null);
            actionUndo           = (ImageButton)layout.findViewById(R.id.action_undo);
            actionRedo           = (ImageButton)layout.findViewById(R.id.action_redo);
            actionBold           = (ImageButton)layout.findViewById(R.id.action_bold);
            actionItalic         = (ImageButton)layout.findViewById(R.id.action_italic);
            actionSubscript      = (ImageButton)layout.findViewById(R.id.action_subscript);
            actionSuperscript    = (ImageButton)layout.findViewById(R.id.action_superscript);
            actionStrikethrough  = (ImageButton)layout.findViewById(R.id.action_strikethrough);
            actionUnderline      = (ImageButton)layout.findViewById(R.id.action_underline);
            actionHorizontalRule = (ImageButton)layout.findViewById(R.id.action_horizontal_rule);
            actionHeading1       = (ImageButton)layout.findViewById(R.id.action_heading1);
            actionHeading2       = (ImageButton)layout.findViewById(R.id.action_heading2);
            actionHeading3       = (ImageButton)layout.findViewById(R.id.action_heading3);
            actionHeading4       = (ImageButton)layout.findViewById(R.id.action_heading4);
            actionHeading5       = (ImageButton)layout.findViewById(R.id.action_heading5);
            actionHeading6       = (ImageButton)layout.findViewById(R.id.action_heading6);
            actionTxtColor       = (ImageButton)layout.findViewById(R.id.action_txt_color);
            actionTxtBgColor     = (ImageButton)layout.findViewById(R.id.action_bg_color);
            actionIndent         = (ImageButton)layout.findViewById(R.id.action_indent);
            actionOutdent        = (ImageButton)layout.findViewById(R.id.action_outdent);
            actionAlignLeft      = (ImageButton)layout.findViewById(R.id.action_align_left);
            actionAlignCenter    = (ImageButton)layout.findViewById(R.id.action_align_center);
            actionAlignRight     = (ImageButton)layout.findViewById(R.id.action_align_right);
            actionBlockquote     = (ImageButton)layout.findViewById(R.id.action_blockquote);
            actionInsertImage    = (ImageButton)layout.findViewById(R.id.action_insert_image);
            actionInsertLink     = (ImageButton)layout.findViewById(R.id.action_insert_link);
            actionInsertUnorderedList   = (ImageButton)layout.findViewById(R.id.action_insert_list_unordered);
            actionInsertOrderedList     = (ImageButton)layout.findViewById(R.id.action_insert_ordered_list);
            actionInsertCheckbox = (ImageButton)layout.findViewById(R.id.action_insert_checkbox);
        }catch (Exception e){
            e.printStackTrace();
        }
        this.addView(layout);
    }

    public boolean initClickListener(){
        if(mEditor == null)
            return false;

        actionUndo.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                if(ifToast)
                    ToastUtils.showShort(R.string.editor_undo);
                if(mEditor != null)
                    mEditor.toUndo();
            }
        });
        actionRedo.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                if(ifToast)
                    ToastUtils.showShort(R.string.editor_redo);
                if(mEditor != null)
                    mEditor.toRedo();
            }
        });
        actionBold.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                if(ifToast)
                    ToastUtils.showShort(R.string.editor_bold);
                if(mEditor != null)
                    mEditor.toBold();
            }
        });
        actionItalic.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                if(ifToast)
                    ToastUtils.showShort(R.string.editor_italic);
                if(mEditor != null)
                    mEditor.toItalic();
            }
        });
        actionSubscript.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                if(ifToast)
                    ToastUtils.showShort(R.string.editor_subscript);
                if(mEditor != null)
                    mEditor.toSubscript();
            }
        });
        actionSuperscript.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                if(ifToast)
                    ToastUtils.showShort(R.string.editor_superscript);
                if(mEditor != null)
                    mEditor.toSuperscript();
            }
        });
        actionStrikethrough.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                if(ifToast)
                    ToastUtils.showShort(R.string.editor_strike_through);
                if(mEditor != null)
                    mEditor.toStrikeThrough();
            }
        });
        actionUnderline.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                if(ifToast)
                    ToastUtils.showShort(R.string.editor_under_line);
                if(mEditor != null)
                    mEditor.toUnderLine();
            }
        });
        actionHorizontalRule.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                if(ifToast)
                    ToastUtils.showShort(R.string.action_horizontal_rule);
                if(mEditor != null)
                    mEditor.toHorizontalRule();
            }
        });
        actionHeading1.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                if(ifToast)
                    ToastUtils.showShort(R.string.editor_heading1);
                if(mEditor != null)
                    mEditor.toHeading(IEditAction.Head.H1);
            }
        });
        actionHeading2.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                if(ifToast)
                    ToastUtils.showShort(R.string.editor_heading2);
                if(mEditor != null)
                    mEditor.toHeading(IEditAction.Head.H2);
            }
        });
        actionHeading3.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                if(ifToast)
                    ToastUtils.showShort(R.string.editor_heading3);
                if(mEditor != null)
                    mEditor.toHeading(IEditAction.Head.H3);
            }
        });
        actionHeading4.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                if(ifToast)
                    ToastUtils.showShort(R.string.editor_heading4);
                if(mEditor != null)
                    mEditor.toHeading(IEditAction.Head.H4);
            }
        });
        actionHeading5.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                if(ifToast)
                    ToastUtils.showShort(R.string.editor_heading5);
                if(mEditor != null)
                    mEditor.toHeading(IEditAction.Head.H5);
            }
        });
        actionHeading6.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                if(ifToast)
                    ToastUtils.showShort(R.string.editor_heading6);
                if(mEditor != null)
                    mEditor.toHeading(IEditAction.Head.H6);
            }
        });
        actionTxtColor.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                if(ifToast)
                    ToastUtils.showShort(R.string.editor_text_color);
                if(mEditor != null)
                    mEditor.toTextColor();
            }
        });
        actionTxtBgColor.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                if(ifToast)
                    ToastUtils.showShort(R.string.editor_text_background_color);
                if(mEditor != null)
                    mEditor.toTextBackgroundColor();
            }
        });
        actionIndent.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                if(ifToast)
                    ToastUtils.showShort(R.string.editor_indent);
                if(mEditor != null)
                    mEditor.toIndent();
            }
        });
        actionOutdent.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                if(ifToast)
                    ToastUtils.showShort(R.string.editor_outdent);
                if(mEditor != null)
                    mEditor.toOutdent();
            }
        });
        actionAlignLeft.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                if(ifToast)
                    ToastUtils.showShort(R.string.editor_align_left);
                if(mEditor != null)
                    mEditor.toAlignLeft();
            }
        });
        actionAlignCenter.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                if(ifToast)
                    ToastUtils.showShort(R.string.editor_align_center);
                if(mEditor != null)
                    mEditor.toAligenCenter();
            }
        });
        actionAlignRight.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                if(ifToast)
                    ToastUtils.showShort(R.string.editor_align_right);
                if(mEditor != null)
                    mEditor.toAlignRight();
            }
        });
        actionBlockquote.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                if(ifToast)
                    ToastUtils.showShort(R.string.editor_block_quote);
                if(mEditor != null)
                    mEditor.toBlockQuote();
            }
        });
        actionInsertImage.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                if(ifToast)
                    ToastUtils.showShort(R.string.editor_insert_file);
                if(mEditor != null)
                    mEditor.toInsertFile();
            }
        });
        actionInsertLink.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                if(ifToast)
                    ToastUtils.showShort(R.string.editor_insert_link);
                if(mEditor != null)
                    mEditor.toInsertLink();
            }
        });
        actionInsertUnorderedList.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                if(ifToast)
                    ToastUtils.showShort(R.string.editor_insert_unordered_list);
                if(mEditor != null)
                    mEditor.toInsertUnorderedList();
            }
        });
        actionInsertOrderedList.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                if(ifToast)
                    ToastUtils.showShort(R.string.editor_insert_ordered_list);
                if(mEditor != null)
                    mEditor.toInsertOrderedList();
            }
        });
       actionInsertCheckbox.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                if(ifToast)
                    ToastUtils.showShort(R.string.editor_insert_todo);
                if(mEditor != null)
                    mEditor.toInsertTodo();
            }
        });
        return true;
    }

    /*if(ifToast)
        ToastUtils.showShort(R.string.editor_align_top);

    if(ifToast)
        ToastUtils.showShort(R.string.editor_align_bottom);

    if(ifToast)
            ToastUtils.showShort(R.string.editor_align_middle);

    if(ifToast)
            ToastUtils.showShort(R.string.editor_align_center_hori);

    if(ifToast)
            ToastUtils.showShort(R.string.editor_font_color);

    if(ifToast)
            ToastUtils.showShort(R.string.editor_font_size);

    if(ifToast)
            ToastUtils.showShort(R.string.editor_font_size);

    if(ifToast)
            ToastUtils.showShort(R.string.editor_background_color);

    if(ifToast)
            ToastUtils.showShort(R.string.editor_background);

    if(ifToast)
            ToastUtils.showShort(R.string.editor_remove_format);

     if(ifToast)
            ToastUtils.showShort(R.string.editor_bullet);

            */
}
