package ahtewlg7.utimer.md;

import ahtewlg7.utimer.R;
import ahtewlg7.utimer.util.MyRInfo;
import in.uncod.android.bypass.Element;

/**
 * Created by lw on 2016/6/7.
 */
public class ElementAction {
    public static final String TAG = ElementAction.class.getSimpleName();

    public static final String ATTR_FLAGS           = MyRInfo.getStringByID(R.string.mypass_element_attr_flags);
    public static final String ATTR_ALT             = MyRInfo.getStringByID(R.string.mypass_element_attr_alt);
    public static final String ATTR_LINK            = MyRInfo.getStringByID(R.string.mypass_element_attr_link);
    public static final String ATTR_TITLE           = MyRInfo.getStringByID(R.string.mypass_element_attr_title);
    public static final String ATTR_LEVEL           = MyRInfo.getStringByID(R.string.mypass_element_attr_level);

    public String getFlags(Element element){
        if(element == null)
            return null;
        return element.getAttribute(ATTR_FLAGS);
    }
    public String getAlt(Element element){
        if(element == null)
            return null;
        return element.getAttribute(ATTR_ALT);
    }
    public String getLink(Element element){
        if(element == null)
            return null;
        return element.getAttribute(ATTR_LINK);
    }
    public String getTitle(Element element){
        if(element == null)
            return null;
        return element.getAttribute(ATTR_TITLE);
    }
    public String getLevel(Element element){
        if(element == null)
            return null;
        return element.getAttribute(ATTR_LEVEL);
    }
}
