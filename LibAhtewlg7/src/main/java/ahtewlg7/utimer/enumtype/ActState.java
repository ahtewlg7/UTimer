package ahtewlg7.utimer.enumtype;

import com.google.common.collect.Lists;

import java.util.List;

public enum ActState {
    MAYBE,
    JUSTDOIT,
    TODO,
    NEXT,
    WAITFOR,
    DATEBOOK,
    DELEGATE,
    DONE;

    public static List<ActState> getActiveAll(){
        List<ActState> list = Lists.newArrayList();
        list.add(JUSTDOIT);
        list.add(TODO);
        list.add(NEXT);
        list.add(WAITFOR);
        list.add(DATEBOOK);
        list.add(DELEGATE);
        list.add(DONE);
        return list;
    }
}
