package com.utimer.entity.span;

import androidx.annotation.StringRes;

import com.utimer.R;

import ahtewlg7.utimer.entity.span.ASpanTag;
import ahtewlg7.utimer.util.MyRInfo;

/**
 * Created by lw on 2019/7/2.
 */
public class SpanMoreTag extends ASpanTag {

    @Override
    public @StringRes int getTagRid() {
        return R.string.span_tag_more;
    }

    @Override
    public String getTagName() {
        return MyRInfo.getStringByID(getTagRid());
    }
}
