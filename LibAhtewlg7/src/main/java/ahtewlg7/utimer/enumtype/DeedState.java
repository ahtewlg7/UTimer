package ahtewlg7.utimer.enumtype;

import com.google.common.collect.Lists;

import java.util.List;

public enum DeedState {
    MAYBE,
    TRASH,
    INBOX,
    DONE,

    DO_NOW,
    TODO,
    NEXT,
    WAIT_FOR,
    DATEBOOK,
    DELEGATE;

    public static List<DeedState> getActiveAll(){
        List<DeedState> list = Lists.newArrayList();
        list.add(INBOX);
        list.add(DO_NOW);
        list.add(TODO);
        list.add(NEXT);
        list.add(WAIT_FOR);
        list.add(DATEBOOK);
        list.add(DELEGATE);
        return list;
    }
}
