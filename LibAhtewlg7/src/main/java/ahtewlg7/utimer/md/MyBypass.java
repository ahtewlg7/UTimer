package ahtewlg7.utimer.md;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.LeadingMarginSpan;
import android.text.style.QuoteSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.text.style.URLSpan;
import android.util.Patterns;
import android.view.View;

import com.blankj.utilcode.util.Utils;

import ahtewlg7.utimer.entity.md.EditElement;
import ahtewlg7.utimer.util.Logcat;
import ahtewlg7.utimer.view.md.ClickableImageSpan;
import in.uncod.android.bypass.Bypass;
import in.uncod.android.bypass.Document;
import in.uncod.android.bypass.Element;
import in.uncod.android.bypass.ReverseSpannableStringBuilder;
import in.uncod.android.bypass.style.HorizontalLineSpan;

/**
 * Created by lw on 2016/6/2.
 */
public class MyBypass extends Bypass{
    public static final String TAG = MyBypass.class.getSimpleName();

    private SpanClickListener spanClickListener;

    public MyBypass() {
        super(Utils.getApp().getApplicationContext());
    }

    public MyBypass(Options options) {
        super(Utils.getApp().getApplicationContext(), options);
    }

    public void setSpanClickListener(SpanClickListener spanClickListener){
        this.spanClickListener = spanClickListener;
    }

    public EditElement toParseMd(String rawTxt) {
        return toParseMd(rawTxt ,null);
    }

    public EditElement toParseMd(String rawTxt, final MyImageGetter imageGetter) {
        Document document = processMarkdown(rawTxt);
        int size = document.getElementCount();
        CharSequence[] charSequenceArray = new CharSequence[size];

        for (int i = 0; i < size; i++)
            charSequenceArray[i] = recurseElement(document.getElement(i), i, size, imageGetter);

        EditElement editElement = new EditElement(rawTxt);
        editElement.setMdCharSequence(TextUtils.concat(charSequenceArray));
        return editElement;
    }
    protected CharSequence recurseElement(Element element, int indexWithinParent, int numberOfSiblings,
                                          MyImageGetter imageGetter){
        if(element == null)
            return null;

        Element.Type type = element.getType();

        boolean isOrderedList = false;
        if (type == Element.Type.LIST) {
            String flagsStr = element.getAttribute("flags");
            if (flagsStr != null) {
                int flags = Integer.parseInt(flagsStr);
                isOrderedList = (flags & Element.F_LIST_ORDERED) != 0;
                if (isOrderedList) {
                    mOrderedListNumber.put(element, 1);
                }
            }
        }

        int size = element.size();
        CharSequence[] spans = new CharSequence[size];

        for (int i = 0; i < size; i++)
            spans[i] = recurseElement(element.getChildren(i), i, size, imageGetter);

        // Clean up after we're done
        if (isOrderedList) {
            mOrderedListNumber.remove(this);
        }

        CharSequence concat = TextUtils.concat(spans);

        SpannableStringBuilder builder = new ReverseSpannableStringBuilder();

        String text = element.getText();
        if (element.size() == 0
                && element.getParent() != null
                && !System.lineSeparator().equals(text)//add by lw , at 20190606
                && element.getParent().getType() != Element.Type.BLOCK_CODE) {
            text = text.replace('\n', ' ');
        }

        // Retrieve the image now so we know whether we're going to have something to display later
        // If we don't, then show the alt text instead (if available).
        Drawable imageDrawable = null;
        //add by lw, 20160608
        if(imageGetter == null)
            Logcat.d(TAG,"recurseElement: imageGetter == null");
        //add end
        if (type == Element.Type.IMAGE && imageGetter != null && !TextUtils.isEmpty(element.getAttribute("link"))) {
            imageDrawable = imageGetter.getDrawable(element);
        }

        switch (type) {
            case LIST:
                if (element.getParent() != null
                        && element.getParent().getType() == Element.Type.LIST_ITEM) {
                    builder.append("\n");
                }
                break;
            case LINEBREAK:
                builder.append("\n");
                break;
            case LIST_ITEM:
                builder.append(" ");
                if (mOrderedListNumber.containsKey(element.getParent())) {
                    int number = mOrderedListNumber.get(element.getParent());
                    builder.append(Integer.toString(number) + ".");
                    mOrderedListNumber.put(element.getParent(), number + 1);
                }
                else {
                    builder.append(mOptions.mUnorderedListItem);
                }
                builder.append("  ");
                break;
            case AUTOLINK:
                builder.append(element.getAttribute("link"));
                break;
            case HRULE:
                // This ultimately gets drawn over by the line span, but
                // we need something here or the span isn't even drawn.
                builder.append("-");
                break;
            case IMAGE:
                // Display alt text (or title text) if there is no image
//                if (imageDrawable == null) {
                    String show = element.getAttribute("alt");
                    if (TextUtils.isEmpty(show)) {
                        show = element.getAttribute("title");
                    }
                    if (!TextUtils.isEmpty(show)) {
                        show = "[" + show + "]\n";
                        builder.append(show);
                    }
//                }
//                else {
//                    // Character to be replaced
//                    builder.append("\uFFFC");
//                }
                break;
        }

        builder.append(text);
        builder.append(concat);

        // Don't auto-append whitespace after last item in document. The 'numberOfSiblings'
        // is the number of children the parent of the current element has (including the
        // element itself), hence subtracting a number from that count gives us the index
        // of the last child within the parent.
        if (element.getParent() != null || indexWithinParent < (numberOfSiblings - 1)) {
            if (type == Element.Type.LIST_ITEM) {
                if (element.size() == 0 || !element.getChildren(element.size() - 1).isBlockElement()) {
                    builder.append("\n");
                }
            }
            else if (element.isBlockElement() && type != Element.Type.BLOCK_QUOTE) {
                if (type == Element.Type.LIST) {
                    // If this is a nested list, don't include newlines
                    if (element.getParent() == null || element.getParent().getType() != Element.Type.LIST_ITEM) {
                        builder.append("\n");
                    }
                }
                else if (element.getParent() != null
                        && element.getParent().getType() == Element.Type.LIST_ITEM) {
                    // List items should never double-space their entries
                    builder.append("\n");
                }
                else {
                    builder.append("\n\n");
                }
            }
        }
        toSetSpan(type,element, builder, imageDrawable);
        return builder;
    }

    private void toSetSpan(Element.Type type, Element element, SpannableStringBuilder builder, Drawable imageDrawable){
        ElementAction elementAction = new ElementAction();
        switch (type) {
            case HEADER:
                String levelStr = elementAction.getLevel(element);
                int level = Integer.parseInt(levelStr);
                setSpan(builder, new RelativeSizeSpan(mOptions.mHeaderSizes[level - 1]));
                setSpan(builder, new StyleSpan(Typeface.BOLD));
                break;
            case LIST:
                setBlockSpan(builder, new LeadingMarginSpan.Standard(mListItemIndent));
                break;
            case EMPHASIS:
                setSpan(builder, new StyleSpan(Typeface.ITALIC));
                break;
            case DOUBLE_EMPHASIS:
                setSpan(builder, new StyleSpan(Typeface.BOLD));
                break;
            case TRIPLE_EMPHASIS:
                setSpan(builder, new StyleSpan(Typeface.BOLD_ITALIC));
                break;
            case BLOCK_CODE:
                setSpan(builder, new LeadingMarginSpan.Standard(mCodeBlockIndent));
                setSpan(builder, new TypefaceSpan("monospace"));
                break;
            case CODE_SPAN:
                setSpan(builder, new TypefaceSpan("monospace"));
                break;
            case LINK:
            case AUTOLINK:
                String link = elementAction.getLink(element);
                if (!TextUtils.isEmpty(link) && Patterns.EMAIL_ADDRESS.matcher(link).matches()) {
                    link = "mailto:" + link;
                }
                setSpan(builder, new URLSpan(link));
                break;
            case BLOCK_QUOTE:
                // We add two leading margin spans so that when the order is reversed,
                // the QuoteSpan will always be in the same spot.
                setBlockSpan(builder, new LeadingMarginSpan.Standard(mBlockQuoteIndent));
                setBlockSpan(builder, new QuoteSpan(mOptions.mBlockQuoteColor));
                setBlockSpan(builder, new LeadingMarginSpan.Standard(mBlockQuoteIndent));
                setBlockSpan(builder, new StyleSpan(Typeface.ITALIC));
                break;
            case STRIKETHROUGH:
                setSpan(builder, new StrikethroughSpan());
                break;
            case HRULE:
                setSpan(builder, new HorizontalLineSpan(mOptions.mHruleColor, mHruleSize, mHruleTopBottomPadding));
                break;
            case IMAGE:
                if (imageDrawable != null)
                    setImageClickSpan(builder,element,imageDrawable);
                break;
        }
    }

    private void setImageClickSpan(SpannableStringBuilder builder, final Element element, final Drawable imageDrawable){
       ClickableImageSpan clickableSpan = new ClickableImageSpan(imageDrawable) {
            public void onClick(View view) {
                Logcat.d(TAG,"clickableSpan onClick");
                if(spanClickListener != null)
                    spanClickListener.onSpanClick(view,element);
            }
        };
        builder.setSpan(clickableSpan, 0, builder.length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    public static interface MyImageGetter extends ImageGetter {
        public Drawable getDrawable(Element element);
    }
    public static interface SpanClickListener {
        public void onSpanClick(View view, Element element);
    }
}
