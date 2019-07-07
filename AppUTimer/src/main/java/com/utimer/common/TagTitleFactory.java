package com.utimer.common;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.google.common.base.Optional;
import com.utimer.R;

import ahtewlg7.utimer.enumtype.DeedState;
import ahtewlg7.utimer.util.MyRInfo;

/**
 * Created by lw on 2019/7/7.
 */
public class TagTitleFactory {
    public static final int INVALID_TAG_RID = 0;


    public Optional<String> getTagTitle(@NonNull DeedState deedState){
        @StringRes int rId = getTagTitleRid(deedState);
        return rId != INVALID_TAG_RID ? Optional.of(MyRInfo.getStringByID(rId)) : Optional.absent();
    }

    public @StringRes int getTagTitleRid(@NonNull DeedState deedState){
        int tmp = INVALID_TAG_RID;
        switch(deedState){
            case TWO_MIN:
                tmp = R.string.title_deed_tag_two_min;
                break;
            case DEFER:
                tmp = R.string.title_deed_tag_defer;
                break;
            case DELEGATE:
                tmp = R.string.title_deed_tag_delegate;
                break;
            case WISH:
                tmp = R.string.title_deed_tag_wish;
                break;
            case REFERENCE:
                tmp = R.string.title_deed_tag_reference;
                break;
            case PROJECT:
                tmp = R.string.title_deed_tag_project;
                break;
            case DONE:
                tmp = R.string.title_deed_tag_done;
                break;
            case USELESS:
                tmp = R.string.title_deed_tag_useless;
                break;
            case TRASH:
                tmp = R.string.title_deed_tag_trash;
                break;
            default:
                //todo
                break;
        }
        return tmp;
    }
}
