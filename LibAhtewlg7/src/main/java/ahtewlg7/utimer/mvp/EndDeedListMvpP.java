package ahtewlg7.utimer.mvp;

import org.joda.time.DateTimeComparator;

import ahtewlg7.utimer.enumtype.DeedState;

public class EndDeedListMvpP extends BaseDeedListMvpP {
    public EndDeedListMvpP(IBaseDeedMvpV mvpV){
        super(mvpV);
    }

    @Override
    public void toLoadDeedByState(boolean ascOrder, DeedState... deedState){
        toLoad(mvpM.toLoad(ascOrder, deedState).sorted(DateTimeComparator.getInstance().reversed()));
    }
}
