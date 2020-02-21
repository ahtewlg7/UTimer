package ahtewlg7.utimer.util;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import java.util.Collection;
import java.util.Map;
import java.util.Set;


public abstract class ABaseTable<R, C, V> {
    protected Table<R, C, V> table;

    public abstract boolean ifPriReyValid(R r);
    public abstract boolean ifSecKeyValid(C c);
    public abstract boolean ifValueValid(V v);

    protected ABaseTable(){
        table = HashBasedTable.create();
    }

    public int getNum(){
        return table.size();
    }

    public Set<Table.Cell<R, C, V>> getAllCell(){
        return table.cellSet();
    }

    public Set<R> getRowKey(){
        return table.rowKeySet();
    }

    public Set<C> getColumnKey(){
        return table.columnKeySet();
    }

    public Collection<V> getValue(){
        return table.values();
    }

    public Set<Map.Entry<C, V>> getRowValues(R r){
        return table.row(r).entrySet();
    }
    public Set<Map.Entry<R, V>> getColumnValues(C c){
        return table.column(c).entrySet();
    }

    public Map<C, V> getRow(R r){
        return table.row(r);
    }
    public Map<R, V> getColumn(C c){
        return table.column(c);
    }

    public Map<R, Map<C,V>> getAllRow(){
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
    public boolean containsValue(V v){
        return table.containsValue(v);
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
