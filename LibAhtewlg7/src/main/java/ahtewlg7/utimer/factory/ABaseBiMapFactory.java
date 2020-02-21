package ahtewlg7.utimer.factory;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.util.Set;

//the key(or value) of BiMap must be unique with other key(or value)
public abstract class ABaseBiMapFactory<K, V> {
    protected BiMap<K, V> baseMap;

    public abstract boolean ifKeyValid(K k);
    public abstract boolean ifValueValid(V v);

    protected ABaseBiMapFactory(){
        baseMap = HashBiMap.create();
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
    public K getKey(V v){
        return baseMap.inverse().get(v);
    }

    public void removeByKey(K k){
        baseMap.remove(k);
    }
    public void removeByValue(V v){
        baseMap.inverse().remove(v);
    }

    public Set<K> getAllKey(){
        return baseMap.keySet();
    }

    public void clearAll(){
        baseMap.clear();
    }
}
