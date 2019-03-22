package ahtewlg7.utimer.factory;

import android.util.LruCache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by lw on 2019/3/13.
 */
public abstract class ABaseLruCacheFactory<K,V> {
    protected int maxSize;
    protected LruCache<K,V> cache;

    protected abstract boolean ifKeyValid(K k);
    protected abstract boolean ifValueValid(V v);

    protected ABaseLruCacheFactory(){
        long maxMemory = Runtime.getRuntime().maxMemory();
        maxSize = (int) (maxMemory / 8);
        cache   = new LruCache<K,V>(maxSize);
    }
    protected ABaseLruCacheFactory(int maxSize){
        this.maxSize = maxSize;
        cache = new LruCache<K,V>(maxSize);
    }

    public boolean add(K k, V v){
        if(!ifKeyValid(k) || !ifValueValid(v))
            return false;
        cache.put(k, v);
        return true;
    }

    public V get(K k){
        return cache.get(k);
    }

    public List<V> getAll(){
        return new ArrayList<V>(cache.snapshot().values());
    }

    public V remove(K k){
        return cache.remove(k);
    }

    public Map<K, V> snapshot(){
        return cache.snapshot();
    }

    public void clearAll(){
        cache.evictAll();
    }
}
