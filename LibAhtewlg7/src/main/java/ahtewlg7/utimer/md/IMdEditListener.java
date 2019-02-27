package ahtewlg7.utimer.md;

/**
 * Created by lw on 2016/5/17.
 */
public interface IMdEditListener {
    public void toAlignLeft();
    public void toAlignRight();
    public void toAlignTop();
    public void toAlignBottom();
    public void toAlignMiddle();
    public void toAligenCenterHori();
    public void toAligenCenter();
    public void toFontColor();
    public void toFontSizeByPx();
    public void toFontSizeByLevel();
    public void toBackgroundColor();
    public void toBackground();
    public void toUndo();
    public void toRedo();
    public void toBold();
    public void toItalic();
    public void toSubscript();
    public void toSuperscript();
    public void toStrikeThrough();
    public void toUnderLine();
    public void toHorizontalRule();
    public void toTextColor();
    public void toTextBackgroundColor();
    public void toRemoveFormat();
    public void toHead1();
    public void toHead2();
    public void toHead3();
    public void toHead4();
    public void toHead5();
    public void toHead6();
    public void toIndent();
    public void toOutdent();
    public void toBlockQuote();
    public void toBullets();
    public void toInsertFile();
    public void toInsertLink();
    public void toInsertOrderedList();
    public void toInsertUnorderedList();
}
