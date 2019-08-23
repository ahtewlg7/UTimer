package ahtewlg7.utimer.entity.span;

import android.text.TextUtils;

import com.google.common.collect.Lists;

import java.util.List;

import ahtewlg7.utimer.util.AscII;

/**
 * Created by lw on 2019/7/8.
 */
public class SimpleMultiSpanTag extends ASpanTag {
    private List<String> tagList;

    public SimpleMultiSpanTag(){
        tagList = Lists.newArrayList();
    }

    public void appendTag(String tag){
        if(!TextUtils.isEmpty(tag))
            tagList.add(tag);
    }

    @Override
    public String getTagName() {
        StringBuilder tagBuilder = new StringBuilder();
        for(String tag : tagList){
            if(TextUtils.isEmpty(tagBuilder))
                tagBuilder.append(tag);
            else
                tagBuilder.append(AscII.Space()).append(tag);
        }
        return tagBuilder.toString();
    }
}
