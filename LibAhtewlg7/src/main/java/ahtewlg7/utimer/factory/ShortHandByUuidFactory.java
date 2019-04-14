package ahtewlg7.utimer.factory;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ahtewlg7.utimer.comparator.GtdTimeComparator;
import ahtewlg7.utimer.entity.gtd.ShortHandEntity;
import ahtewlg7.utimer.enumtype.GtdLife;
import ahtewlg7.utimer.gtd.GtdLifeCycleAction;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

import static ahtewlg7.utimer.enumtype.GtdLife.MONTH;
import static ahtewlg7.utimer.enumtype.GtdLife.QUARTER;
import static ahtewlg7.utimer.enumtype.GtdLife.TODAY;
import static ahtewlg7.utimer.enumtype.GtdLife.TOMORROW;
import static ahtewlg7.utimer.enumtype.GtdLife.WEEK;
import static ahtewlg7.utimer.enumtype.GtdLife.YEAR;

/**
 * Created by lw on 2019/3/13.
 */
public class ShortHandByUuidFactory extends ABaseLruCacheFactory<String, ShortHandEntity> {
    private static ShortHandByUuidFactory instance;

    private BiMap<String, String> titleUuidMap;
    private Multimap<GtdLife, String> lifeUuidMultiMap;

    private GtdLifeCycleAction lifeCycleAction;

    protected ShortHandByUuidFactory(){
        super();
        titleUuidMap        = HashBiMap.create();
        lifeUuidMultiMap    = HashMultimap.create();
        lifeCycleAction     = new GtdLifeCycleAction();
    }

    public static ShortHandByUuidFactory getInstance() {
        if(instance == null)
            instance = new ShortHandByUuidFactory();
        return instance;
    }

    @Override
    protected boolean ifKeyValid(String uuid) {
        return !TextUtils.isEmpty(uuid);
    }

    @Override
    protected boolean ifValueValid(ShortHandEntity entity) {
        return entity != null && entity.ifValid();
    }

    @Override
    public boolean add(String s, ShortHandEntity entity) {
        boolean result = super.add(s, entity);
        if(result && entity.getAttachFile().getRPath().isPresent())
            titleUuidMap.put(entity.getAttachFile().getRPath().get(), entity.getUuid());
        return result;
    }

    @Override
    public ShortHandEntity remove(String s) {
        ShortHandEntity entity = super.remove(s);
        if(entity != null && entity.ifValid() && titleUuidMap.containsValue(entity.getUuid()))
            titleUuidMap.inverse().remove(entity.getUuid());
        return entity;
    }

    @Override
    public void clearAll() {
        super.clearAll();
        titleUuidMap.clear();
    }

    public Flowable<ShortHandEntity> getEntityByLife(@NonNull final GtdLife actLife){
        return Flowable.fromIterable(getEntityByUuid(new ArrayList<String>(lifeUuidMultiMap.get(actLife))))
                .doOnNext(new Consumer<ShortHandEntity>() {
                    @Override
                    public void accept(ShortHandEntity entity) throws Exception {
                        entity.setGtdLife(actLife);
                    }
                });
    }
    public Flowable<ShortHandEntity> getEntityByLife(){
        return Flowable.fromArray(TODAY,TOMORROW,WEEK,MONTH,QUARTER,YEAR).flatMap(new Function<GtdLife, Publisher<ShortHandEntity>>() {
            @Override
            public Publisher<ShortHandEntity> apply(GtdLife actLife) throws Exception {
                return getEntityByLife(actLife);
            }
        });
    }

    private List<ShortHandEntity> getEntityByUuid(@NonNull List<String> uuidList){
        List<ShortHandEntity> entityList = Lists.newArrayList();
        for(String uuid : uuidList){
            if(TextUtils.isEmpty(uuid))
                continue;
            ShortHandEntity entity = get(uuid);
            if(entity != null)
                entityList.add(entity);
        }
        Collections.sort(entityList, new GtdTimeComparator<ShortHandEntity>());
        return entityList;
    }
}
