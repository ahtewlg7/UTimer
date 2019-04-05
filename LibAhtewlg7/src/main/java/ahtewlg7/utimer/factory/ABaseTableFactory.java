package ahtewlg7.utimer.factory;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import ahtewlg7.utimer.entity.material.AAttachFile;
import ahtewlg7.utimer.util.TableAction;
import io.reactivex.Flowable;


public abstract class ABaseTableFactory<K, M, V> {
    protected Table<K, M, V> baseTable;
    protected TableAction<K, M, V> tableAction;

    public abstract boolean ifPriKeyValid(K k);
    public abstract boolean ifSecKeyValid(M m);
    public abstract boolean ifValueValid(V v);
    public abstract V createBean();
    public abstract V createBean(AAttachFile attachFile);

    protected ABaseTableFactory(){
        baseTable   = HashBasedTable.create();
        tableAction = new TableAction<K, M, V>(baseTable);
    }

    public int getNum(){
        return tableAction.getNum();
    }

    public Flowable<Table.Cell<K, M , V>> getAllCell(){
        return tableAction.getAllCell();
    }

    public Flowable<K> getRowKey(){
        return tableAction.getRowKey();
    }

    public Flowable<M> getColumnKey(){
        return tableAction.getColumnKey();
    }

    public Flowable<V> getValue(){
        return tableAction.getValue();
    }

    public V getValue(K k, M m){
        return tableAction.getValue(k, m);
    }

    public void addValue(K k, M m, V v){
        if(ifPriKeyValid(k) && ifSecKeyValid(m) && ifValueValid(v) && !baseTable.contains(k, m))
            tableAction.putValue(k, m , v);
    }

    public void remove(K k, M m){
        tableAction.remove(k, m);
    }

    public void clearAll(){
        tableAction.clearAll();
    }
}
