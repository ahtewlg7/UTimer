package ahtewlg7.utimer.factory;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import ahtewlg7.utimer.entity.material.AAttachFile;
import io.reactivex.Flowable;


public abstract class ABaseTableFactory<K, M, V> {
    public static final String TAG = ABaseTableFactory.class.getSimpleName();

    protected Table<K, M, V> baseTable;

    public abstract boolean ifPriKeyValid(K k);
    public abstract boolean ifSecKeyValid(M m);
    public abstract boolean ifValueValid(V v);
    public abstract V createBean();
    public abstract V createBean(AAttachFile attachFile);

    protected ABaseTableFactory(){
        baseTable = HashBasedTable.create();
    }

    public int getNum(){
        return baseTable.size();
    }

    public Flowable<Table.Cell<K, M , V>> getAllCell(){
        return Flowable.fromIterable(baseTable.cellSet());
    }

    public Flowable<K> getPriKey(){
        return Flowable.fromIterable(baseTable.rowKeySet());
    }

    public Flowable<M> getSecKey(){
        return Flowable.fromIterable(baseTable.columnKeySet());
    }

    public Flowable<V> getValue(){
        return Flowable.fromIterable(baseTable.values());
    }

    public V getValue(K k, M m){
        return baseTable.get(k, m);
    }

    public void addValue(K k, M m, V v){
        if(ifPriKeyValid(k) && ifSecKeyValid(m) && ifValueValid(v) && !baseTable.contains(k, m))
            baseTable.put(k, m , v);
    }

    public void removeByKey(K k, M m){
        baseTable.remove(k, m);
    }

    public void clearAll(){
        baseTable.clear();
    }
}
