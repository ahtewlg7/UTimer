package ahtewlg7.utimer.util;

import com.google.common.collect.Table;

import java.util.Map;

import io.reactivex.Flowable;

/**
 * Created by lw on 2019/4/2.
 */
public class TableAction<R, C, V> {
    private Table<R, C, V> table;

    public TableAction(Table<R, C, V> table){
        this.table = table;
    }

    public int getNum(){
        return table.size();
    }

    public Flowable<Table.Cell<R, C, V>> getAllCell(){
        return Flowable.fromIterable(table.cellSet());
    }

    public Flowable<R> getRowKey(){
        return Flowable.fromIterable(table.rowKeySet());
    }

    public Flowable<C> getColumnKey(){
        return Flowable.fromIterable(table.columnKeySet());
    }

    public Flowable<V> getValue(){
        return Flowable.fromIterable(table.values());
    }

    public Flowable<Map.Entry<C,V>> getRow(R r){
        return Flowable.fromIterable(table.row(r).entrySet());
    }
    public Flowable<Map.Entry<R,V>> getColumn(C c){
        return Flowable.fromIterable(table.column(c).entrySet());
    }

    public Map<R,Map<C,V>> getAllRow(){
        return table.rowMap();
    }
    public Map<C, Map<R,V>> getAllColumn(){
        return table.columnMap();
    }

    public boolean contain(R r, C c){
        return table.contains(r, c);
    }

    public boolean containsColumn(C c){
        return table.containsColumn(c);
    }
    public boolean containsRow(R r){
        return table.containsRow(r);
    }

    public void putValue(R r, C c, V v){
        table.put(r, c , v);
    }

    public V getValue(R r, C c){
        return table.get(r, c);
    }

    public void remove(R r, C c){
        table.remove(r, c);
    }

    public void clearAll(){
        table.clear();
    }
}
