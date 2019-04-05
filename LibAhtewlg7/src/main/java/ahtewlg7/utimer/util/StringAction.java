package ahtewlg7.utimer.util;

import android.text.TextUtils;

import com.google.common.collect.ImmutableList;
import com.google.common.io.CharSource;

import io.reactivex.Flowable;

/**
 * Created by lw on 2018/10/25.
 */
public class StringAction {
    public Flowable<String> readLines(String src){
        if(TextUtils.isEmpty(src))
            return Flowable.empty();

        ImmutableList<String> lineList = null;
        try{
            lineList = CharSource.wrap(src).readLines();
        }catch(Exception e){}

        if(lineList != null)
            return Flowable.fromIterable(lineList);
        return Flowable.empty();
    }
}
