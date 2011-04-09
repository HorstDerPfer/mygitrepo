package db.training.easy.util;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Eine HashMap, die mit weichen Referenzen auf die Werte arbeitet. D.h. Werte, auf die es keine
 * "richtige" (=harte) Referenz mehr gibt können vom Garbage-Collector entfernt werden und belegen
 * nicht ewig Speicher. Praktisch für die Verwendung als Cache.
 * 
 * @param <K>
 *                Typ der Schlüssel
 * @param <V>
 *                Typ der Werte
 * @see java.lang.ref.SoftReference
 * @author MarcBlume
 */
public class SoftHashMap<K, V> extends AbstractMap<K, V> {

    protected final Map<K, SoftReference<V>> hash = new HashMap<K, SoftReference<V>>();

    protected final Map<SoftReference<V>, K> reverseLookup = new HashMap<SoftReference<V>, K>();

    protected final ReferenceQueue<V> queue = new ReferenceQueue<V>();

    @Override
    public V get(Object key) {
	expungeStaleEntries();
	V result = null;
	SoftReference<V> soft_ref = this.hash.get(key);
	if (soft_ref != null) {
	    result = soft_ref.get();
	    if (result == null) {
		this.hash.remove(key);
		this.reverseLookup.remove(soft_ref);
	    }
	}
	return result;
    }

    protected void expungeStaleEntries() {
	Reference<? extends V> sv;
	while ((sv = this.queue.poll()) != null) {
	    this.hash.remove(this.reverseLookup.remove(sv.get()));
	}
    }

    @Override
    public V put(K key, V value) {
	expungeStaleEntries();
	SoftReference<V> soft_ref = new SoftReference<V>(value, this.queue);
	this.reverseLookup.put(soft_ref, key);
	SoftReference<V> result = this.hash.put(key, soft_ref);
	if (result == null)
	    return null;
	return result.get();
    }

    @Override
    public V remove(Object key) {
	expungeStaleEntries();
	SoftReference<V> result = this.hash.remove(key);
	if (result == null)
	    return null;
	return result.get();
    }

    @Override
    public void clear() {
	this.hash.clear();
	this.reverseLookup.clear();
    }

    @Override
    public int size() {
	expungeStaleEntries();
	return this.hash.size();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
	expungeStaleEntries();
	Set<Entry<K, V>> result = new LinkedHashSet<Entry<K, V>>();
	for (final Entry<K, SoftReference<V>> entry : this.hash.entrySet()) {
	    final V value = entry.getValue().get();
	    if (value != null) {
		result.add(new Entry<K, V>() {

		    public K getKey() {
			return entry.getKey();
		    }

		    public V getValue() {
			return value;
		    }

		    public V setValue(V v) {
			entry.setValue(new SoftReference<V>(v, SoftHashMap.this.queue));
			return value;
		    }
		});
	    }
	}
	return result;
    }
}
