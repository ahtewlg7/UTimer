package ahtewlg7.utimer.enumtype;

import com.google.common.collect.Lists;

import java.util.List;

public enum ActState {
    MAYBE,
    TRASH,
    GTD,
    DO_NOW,
    TODO,
    NEXT,
    WAIT_FOR,
    DATEBOOK,
    DELEGATE,
    DONE;

    public static List<ActState> getActiveAll(){
        List<ActState> list = Lists.newArrayList();
        list.add(DO_NOW);
        list.add(TODO);
        list.add(GTD);
        list.add(NEXT);
        list.add(WAIT_FOR);
        list.add(DATEBOOK);
        list.add(DELEGATE);
        list.add(DONE);
        return list;
    }
}
