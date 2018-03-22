package ahtewlg7.utimer.mvp;

/**
 * Created by lw on 2018/3/23.
 */

public interface IGtdRecyclerViewMvpV<T> extends IRecyclerViewMvpV<T> {
    public void toStartNoteActivity(String gtdEntityId, String noteEntityId);
}
