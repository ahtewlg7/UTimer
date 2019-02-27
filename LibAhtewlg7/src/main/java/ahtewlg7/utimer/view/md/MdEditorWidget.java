package ahtewlg7.utimer.view.md;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import ahtewlg7.utimer.R;
import ahtewlg7.utimer.md.IMdEditListener;
import ahtewlg7.utimer.util.AndrManagerFactory;

/**
 * Created by lw on 2016/5/17.
 */
public class MdEditorWidget extends RelativeLayout {
    public static final String TAG = MdEditorWidget.class.getSimpleName();

    private ImageButton actionUndo;
    private ImageButton actionRedo;
    private ImageButton actionBold;
    private ImageButton actionItalic;
    private ImageButton actionSubscript;
    private ImageButton actionSuperscript;
    private ImageButton actionStrikethrough;
    private ImageButton actionUnderline;
    private ImageButton actionHorizontalRule;
    private ImageButton actionHeading1;
    private ImageButton actionHeading2;
    private ImageButton actionHeading3;
    private ImageButton actionHeading4;
    private ImageButton actionHeading5;
    private ImageButton actionHeading6;
    private ImageButton actionTxtColor;
    private ImageButton actionTxtBgColor;
    private ImageButton actionIndent;
    private ImageButton actionOutdent;
    private ImageButton actionAlignLeft;
    private ImageButton actionAlignCenter;
    private ImageButton actionAlignRight;
    private ImageButton actionBlockquote;
    private ImageButton actionInsertImage;
    private ImageButton actionInsertLink;
    private ImageButton actionInsertUnorderedList;
    private ImageButton actionInsertOrderedList;

    private IMdEditListener editListener;

    public MdEditorWidget(Context context) {
        super(context);
        findView();
    }
    public MdEditorWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        findView();
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

            initClickListener();
        }catch (Exception e){
            e.printStackTrace();
        }
        this.addView(layout);
    }

    public void setEditListener(IMdEditListener editListener) {
        this.editListener = editListener;
    }

    public boolean initClickListener(){
        actionUndo.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                if(editListener != null)
                    editListener.toUndo();
            }
        });
        actionRedo.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                if(editListener != null)
                    editListener.toRedo();
            }
        });
        actionBold.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                if(editListener != null)
                    editListener.toBold();
            }
        });
        actionItalic.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                if(editListener != null)
                    editListener.toItalic();
            }
        });
        actionSubscript.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                if(editListener != null)
                    editListener.toSubscript();
            }
        });
        actionSuperscript.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                if(editListener != null)
                    editListener.toSuperscript();
            }
        });
        actionStrikethrough.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                if(editListener != null)
                    editListener.toStrikeThrough();
            }
        });
        actionUnderline.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                if(editListener != null)
                    editListener.toUnderLine();
            }
        });
        actionHorizontalRule.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                if(editListener != null)
                    editListener.toHorizontalRule();
            }
        });
        actionHeading1.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                if(editListener != null)
                    editListener.toHead1();
            }
        });
        actionHeading2.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                if(editListener != null)
                    editListener.toHead2();
            }
        });
        actionHeading3.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                if(editListener != null)
                    editListener.toHead3();
            }
        });
        actionHeading4.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                if(editListener != null)
                    editListener.toHead4();
            }
        });
        actionHeading5.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                if(editListener != null)
                    editListener.toHead5();
            }
        });
        actionHeading6.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                if(editListener != null)
                    editListener.toHead6();
            }
        });
        actionTxtColor.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                if(editListener != null)
                    editListener.toTextColor();
            }
        });
        actionTxtBgColor.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                if(editListener != null)
                    editListener.toTextBackgroundColor();
            }
        });
        actionIndent.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                if(editListener != null)
                    editListener.toIndent();
            }
        });
        actionOutdent.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                if(editListener != null)
                    editListener.toOutdent();
            }
        });
        actionAlignLeft.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                if(editListener != null)
                    editListener.toAlignLeft();
            }
        });
        actionAlignCenter.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                if(editListener != null)
                    editListener.toAligenCenter();
            }
        });
        actionAlignRight.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                if(editListener != null)
                    editListener.toAlignRight();
            }
        });
        actionBlockquote.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                if(editListener != null)
                    editListener.toBlockQuote();
            }
        });
        actionInsertImage.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                if(editListener != null)
                    editListener.toInsertFile();
            }
        });
        actionInsertLink.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                if(editListener != null)
                    editListener.toInsertLink();
            }
        });
        actionInsertUnorderedList.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                if(editListener != null)
                    editListener.toInsertUnorderedList();
            }
        });
        actionInsertOrderedList.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                if(editListener != null)
                    editListener.toInsertOrderedList();
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
