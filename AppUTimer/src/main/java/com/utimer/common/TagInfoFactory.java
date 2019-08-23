package com.utimer.common;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.google.common.base.Optional;
import com.utimer.R;

import ahtewlg7.utimer.enumtype.DeedState;
import ahtewlg7.utimer.util.AscII;
import ahtewlg7.utimer.util.MyRInfo;

/**
 * Created by lw on 2019/7/7.
 */
public class TagInfoFactory {
    public static final int INVALID_TAG_RID = 0;

    public Optional<String> getTagTitle(@NonNull DeedState deedState){
        @StringRes int rId = getTagTitleRid(deedState);
        return rId != INVALID_TAG_RID ? Optional.of(MyRInfo.getStringByID(rId)) : Optional.absent();
    }

    public @StringRes int getTagTitleRid(@NonNull DeedState deedState){
        int tmp = INVALID_TAG_RID;
        switch(deedState){
            case SCHEDULE:
                tmp = R.string.title_deed_tag_schedule;
                break;
            case ONE_QUARTER:
                tmp = R.string.title_deed_tag_quarter;
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

    public @StringRes int getTagDetailRid(@NonNull DeedState deedState){
        int tmp = INVALID_TAG_RID;
        switch(deedState){
            case SCHEDULE:
                tmp = R.string.prompt_deed_tag_schedule;
                break;
            case ONE_QUARTER:
                tmp = R.string.prompt_deed_tag_one_quarter;
                break;
            case DEFER:
                tmp = R.string.prompt_deed_tag_defer;
                break;
            case DELEGATE:
                tmp = R.string.prompt_deed_tag_delegate;
                break;
            case WISH:
                tmp = R.string.prompt_deed_tag_wish;
                break;
            case REFERENCE:
                tmp = R.string.prompt_deed_tag_reference;
                break;
            case PROJECT:
                tmp = R.string.prompt_deed_tag_project;
                break;
            case DONE:
                tmp = R.string.prompt_deed_tag_done;
                break;
            case USELESS:
                tmp = R.string.prompt_deed_tag_useless;
                break;
            case TRASH:
                tmp = R.string.prompt_deed_tag_trash;
                break;
            default:
                //todo
                break;
        }
        return tmp;
    }

    public char getTagIconAscii(@NonNull DeedState deedState){
        char tmp = AscII.MiddleDot();
        switch(deedState){
            case SCHEDULE:
                tmp = AscII.YuanSign();
                break;
            case ONE_QUARTER:
                tmp = AscII.OneQuarter();
                break;
            case DEFER:
                tmp = AscII.LeftDoubleAngleQuotationMark();
                break;
            case DELEGATE:
                tmp = AscII.CommercialAtSign();
                break;
            case WISH:
                tmp = AscII.QuestionMark();
                break;
            case REFERENCE:
                tmp = AscII.CopyrightSign();
                break;
            case PROJECT:
                tmp = AscII.Ampersand();
                break;
            case DONE:
                tmp = AscII.ExclamationMark();
                break;
            case USELESS:
                tmp = AscII.LatinSmallOWithStroke();
                break;
            case TRASH:
                tmp = AscII.Tilde();
                break;
            default:
                //todo
                break;
        }
        return tmp;
    }
}
