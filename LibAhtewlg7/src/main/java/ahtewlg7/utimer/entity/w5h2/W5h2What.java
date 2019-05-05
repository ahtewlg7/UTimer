package ahtewlg7.utimer.entity.w5h2;

import android.text.TextUtils;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import java.util.List;

import ahtewlg7.utimer.entity.ITipsEntity;
import ahtewlg7.utimer.entity.context.DeedContext;

/**
 * Created by lw on 2019/1/16.
 */
public class W5h2What implements ITipsEntity {
    private String target;
    private List<DeedContext> deedContextList;

    public W5h2What(){
        deedContextList = Lists.newArrayList();
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public void addDeed(DeedContext deedContext){
        deedContextList.add(deedContext);
    }
    public void removeDeed(DeedContext deedContext){
        deedContextList.remove(deedContext);
    }
    public void clearDeed(){
        deedContextList.clear();
    }

    @Override
    public Optional<String> toTips() {
        StringBuilder builder   = new StringBuilder("What：");
        for(DeedContext deed : deedContextList)
            builder.append(deed.getName()).append(",");
        return Optional.of(builder.toString());
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("{");
        if(!TextUtils.isEmpty(target))
            builder.append(",").append(target);
        return builder.append("}").toString();
    }
}
