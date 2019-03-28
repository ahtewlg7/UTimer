package ahtewlg7.utimer.factory;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;


public abstract class ABaseMapFactory<K, V> {
    protected BiMap<K, V> baseMap;

    public abstract boolean ifKeyValid(K k);
    public abstract boolean ifValueValid(V v);

    protected ABaseMapFactory(){
        baseMap = HashBiMap.create();
    }

    public int getSize(){
        return baseMap.size();
    }

    public void add(K k, V v){
        if(ifKeyValid(k) && ifValueValid(v))
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
    public K getKey(V v){
        return baseMap.inverse().get(v);
    }

    public void removeByKey(K k){
        baseMap.remove(k);
    }
    public void removeByValue(V v){
        baseMap.inverse().remove(v);
    }

    public void clearAll(){
        baseMap.clear();
    }
}
