package ahtewlg7.utimer.entity.gtd.un;


import androidx.annotation.NonNull;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import java.util.Comparator;
import java.util.List;

import ahtewlg7.utimer.comparator.DegreeLevelComparator;
import ahtewlg7.utimer.enumtype.GtdType;
import io.reactivex.Observable;

public class GtdTaskEntity extends AGtdEntity {
    public static final String TAG = GtdTaskEntity.class.getSimpleName();

    protected boolean isActive;
    protected DegreeLevelComparator degreeLevelComparator;
    protected List<GtdTaskEntity> subTaskActList      = Lists.newArrayList();
    protected List<GtdActionEntity> doneActList       = Lists.newArrayList();
    protected List<GtdActionEntity> toDoActList       = Lists.newArrayList();

    public GtdTaskEntity() {
        degreeLevelComparator  = new DegreeLevelComparator();
    }

    @Override
    public @NonNull String toJson() {
        // TODO: PropertyFilter,2018/9/8
        return "";
    }

    @Override
    public GtdType getGtdType() {
        return null;
    }

    // TODO: 2018/8/29
    @Override
    public boolean isDone() {
        return isActive();
    }

    // TODO: 2018/8/29
    @Override
    public Optional<String> toTips() {
        return Optional.absent();
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getToDoActNum(){
        return toDoActList.size();
    }

    public Observable<GtdActionEntity> getDoneActList(){
        return Observable.fromIterable(doneActList);
    }

    public Observable<GtdActionEntity> getToDoActlist(){
        return Observable.fromIterable(toDoActList);
    }

    public Observable<GtdActionEntity> getAllActList(){
        return Observable.merge(getToDoActlist(),getDoneActList());
    }

    public Observable<GtdActionEntity> toOrderByImport(@NonNull Observable<GtdActionEntity> sourceActObservable){
        return sourceActObservable.sorted(new Comparator<GtdActionEntity>() {
            @Override
            public int compare(GtdActionEntity o1, GtdActionEntity o2) {
                return degreeLevelComparator.compare(o1.getW5h2Entity().getHowMuch().getImportLevel(),
                        o2.getW5h2Entity().getHowMuch().getImportLevel());
            }
        });
    }
}
