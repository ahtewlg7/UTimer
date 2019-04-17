package ahtewlg7.utimer.factory;

import com.google.common.collect.Maps;

import java.util.Map;


public abstract class ABaseMapFactory<K, V> {
    protected Map<K, V> baseMap;

    public abstract boolean ifKeyValid(K k);
    public abstract boolean ifValueValid(V v);

    protected ABaseMapFactory(){
        baseMap = Maps.newHashMap();
    }

    public int getSize(){
        return baseMap.size();
    }

    public void put(K k, V v){
        if(ifKeyValid(k) && ifValueValid(v) && !containsKey(k))
            baseMap.put(k, v);
    }

    public boolean containsKey(K k) {
        return baseMap.containsKey(k);
    }
    public boolean containsValue(V v) {
        return baseMap.containsValue(v);
    }

    public V getValue(K k){
        return baseMap.get(k);
    }

    public void remove(K k){
        baseMap.remove(k);
    }

    public void clearAll(){
        baseMap.clear();
    }
}
