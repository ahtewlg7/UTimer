package ahtewlg7.utimer.entity.gtd;

import ahtewlg7.utimer.enumtype.GtdType;

/**
 * Created by lw on 2017/9/27.
 */

public class GtdOnlineEntity extends AGtdTaskEntity {
    public static final String TAG = GtdOnlineEntity.class.getSimpleName();

    private boolean watched;
    private boolean stared;
    private boolean forked;

    protected String url;

    public boolean incomable(){
        return watched || stared || forked;
    }

    public void setWatched(boolean watched) {
        this.watched = watched;
    }

    public boolean isWatched(){
        return watched;
    }

    public void setStared(boolean stared) {
        this.stared = stared;
    }

    public boolean isStared(){
        return stared;
    }

    public void setForked(boolean forked) {
        this.forked = forked;
    }

    public boolean isForked(){
        return forked;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public GtdType getPreTaskType() {
        return GtdType.NEW;
    }

    @Override
    public GtdType getTaskType() {
        return GtdType.ONLINE;
    }

    @Override
    public String toString() {
        return super.toString() + ", watched = " + watched + ", stared = " + stared + ", forked = " + forked
                + ", url = " + url;
    }
}
