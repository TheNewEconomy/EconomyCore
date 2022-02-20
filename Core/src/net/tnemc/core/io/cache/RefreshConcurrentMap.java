package net.tnemc.core.io.cache;

import net.tnemc.core.EconomyManager;
import net.tnemc.core.io.Datable;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.Spliterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Represents a {@link ConcurrentHashMap} that is able to
 * refresh data and provides a listener for when data is added and removed.
 *
 * This map provides the ability to automatically refresh data over a given period of
 * time using a thread to refresh the data.
 *
 * @see ConcurrentHashMap
 * @since 0.1.1.17
 * @author creatorfromhell
 */
public class RefreshConcurrentMap<K, V extends Datable<K, V>> extends ConcurrentHashMap<K, V> {

  private ConcurrentHashMap<K, Long> keyExpiration = new ConcurrentHashMap<>();

  /**
   * The expiration time for keys in seconds.
   */
  private int expiration;
  private boolean purge;
  private boolean refresh;
  private boolean cache;
  private RefreshMapListener<K, V> listener;
  private RefreshThread<K, V> thread;

  /**
   * Creates a new, empty map with the default initial table size (16).
   */
  public RefreshConcurrentMap(boolean refresh, int expiration, RefreshMapListener<K, V> listener) {
    this.cache = EconomyManager.instance().canCache();
    this.expiration = expiration;
    this.refresh = refresh;
    this.purge = false;
    this.listener = listener;
    this.thread = new RefreshThread<>(this);
  }

  public void initialize() {
    if(refresh) {
      thread.start();
    }
  }

  private boolean checkDB(CheckLevel level) {
    return !cache || level.equals(CheckLevel.DB_ONLY);
  }

  /**
   * Returns the value to which the specified key is mapped,
   * or {@code null} if this map contains no mapping for the key.
   *
   * <p>More formally, if this map contains a mapping from a key
   * {@code k} to a value {@code v} such that {@code key.equals(k)},
   * then this method returns {@code v}; otherwise it returns
   * {@code null}.  (There can be at most one such mapping.)
   *
   * @param key
   *
   * @throws NullPointerException if the specified key is null
   */
  @Override
  public V get(Object key) {
    return get(key, CheckLevel.MAP_FIRST);
  }

  /**
   * Used to get a value from the map or directly from the database.
   *
   * @param key The key to search for.
   * @param level The check level to use.
   * @return
   */
  public V get(Object key, CheckLevel level) {
    if(checkDB(level) || !containsKey(key, CheckLevel.MAP_ONLY)) {
      return listener.get(key);
    }

    V value = super.get(key);
    if(value == null && level.equals(CheckLevel.MAP_FIRST)) {
      return listener.get(key);
    }
    return value;
  }

  /**
   * Tests if the specified object is a key in this table.
   *
   * @param key possible key
   *
   * @return {@code true} if and only if the specified object
   * is a key in this table, as determined by the
   * {@code equals} method; {@code false} otherwise
   * @throws NullPointerException if the specified key is null
   */
  @Override
  public boolean containsKey(Object key) {
    return containsKey(key, CheckLevel.MAP_FIRST);
  }

  /**
   * Tests if the specified object is a key in this table.
   *
   * @param key possible key
   * @param level The check level to use/
   *
   * @return {@code true} if and only if the specified object
   * is a key in this table, as determined by the
   * {@code equals} method; {@code false} otherwise
   * @throws NullPointerException if the specified key is null
   */
  public boolean containsKey(Object key, CheckLevel level) {

    final boolean mapContains = super.containsKey(key);

    if(checkDB(level) || !mapContains && level.equals(CheckLevel.MAP_FIRST)) {
      return listener.containsKey(key);
    }
    return mapContains;
  }

  /**
   * Returns {@code true} if this map maps one or more keys to the
   * specified value. Note: This method may require a full traversal
   * of the map, and is much slower than method {@code containsKey}.
   *
   * @param value value whose presence in this map is to be tested
   *
   * @return {@code true} if this map maps one or more keys to the
   * specified value
   * @throws NullPointerException if the specified value is null
   */
  @Override
  public boolean containsValue(Object value) {
    return containsValue(value, CheckLevel.MAP_FIRST);
  }

  /**
   * Returns {@code true} if this map maps one or more keys to the
   * specified value. Note: This method may require a full traversal
   * of the map, and is much slower than method {@code containsKey}.
   *
   * @param value value whose presence in this map is to be tested
   *
   * @return {@code true} if this map maps one or more keys to the
   * specified value
   * @throws NullPointerException if the specified value is null
   */
  public boolean containsValue(Object value, CheckLevel level) {

    final boolean mapContains = super.containsValue(value);

    if(checkDB(level) || !mapContains && level.equals(CheckLevel.MAP_FIRST)) {
      return listener.containsValue(value);
    }
    return mapContains;
  }

  /**
   * Maps the specified key to the specified value in this table.
   * Neither the key nor the value can be null.
   *
   * <p>The value can be retrieved by calling the {@code get} method
   * with a key that is equal to the original key.
   *
   * @param key   key with which the specified value is to be associated
   * @param value value to be associated with the specified key
   *
   * @return the previous value associated with {@code key}, or
   * {@code null} if there was no mapping for {@code key}
   * @throws NullPointerException if the specified key or value is null
   */
  @Override
  public V put(@NotNull K key, @NotNull V value) {
    return put(key, value, true);
  }

  /**
   * Maps the specified key to the specified value in this table.
   * Neither the key nor the value can be null.
   *
   * <p>The value can be retrieved by calling the {@code get} method
   * with a key that is equal to the original key.
   *
   * @param key   key with which the specified value is to be associated
   * @param value value to be associated with the specified key
   * @param database True if the key value pair should be added to the database too.
   *
   * @return the previous value associated with {@code key}, or
   * {@code null} if there was no mapping for {@code key}
   * @throws NullPointerException if the specified key or value is null
   */
  public V put(@NotNull K key, @NotNull V value, boolean database) {
    if(database) {
      listener.put(key, value);
    }

    if(cache) {
      return super.put(key, value);
    }
    return value;
  }

  /**
   * Copies all of the mappings from the specified map to this one.
   * These mappings replace any mappings that this map had for any of the
   * keys currently in the specified map.
   *
   * @param m mappings to be stored in this map
   */
  @Override
  public void putAll(Map<? extends K, ? extends V> m) {
    putAll(m, true);
  }
  /**
   * Copies all of the mappings from the specified map to this one.
   * These mappings replace any mappings that this map had for any of the
   * keys currently in the specified map.
   *
   * @param m mappings to be stored in this map
   * @param database True if the values should be added to the database too.
   */
  public void putAll(Map<? extends K, ? extends V> m, boolean database) {
    for(Entry<? extends K, ? extends V> entry : m.entrySet()) {
      put(entry.getKey(), entry.getValue(), database);
    }
  }

  /**
   * Removes the key (and its corresponding value) from this map.
   * This method does nothing if the key is not in the map.
   *
   * @param key the key that needs to be removed
   *
   * @return the previous value associated with {@code key}, or
   * {@code null} if there was no mapping for {@code key}
   * @throws NullPointerException if the specified key is null
   */
  @Override
  public V remove(@NotNull Object key) {
    return remove(key, false);
  }

  /**
   * Removes the key (and its corresponding value) from this map.
   * This method does nothing if the key is not in the map.
   *
   * @param key the key that needs to be removed
   * @param database True if the value in the database should be removed too.
   *
   * @return the previous value associated with {@code key}, or
   * {@code null} if there was no mapping for {@code key}
   * @throws NullPointerException if the specified key is null
   */
  public V remove(@NotNull Object key, boolean database) {

    final V value = get(key);
    if(database) {
      listener.remove(key);
    }

    if(cache) {
      return super.remove(key);
    }
    return value;
  }

  /**
   * Removes all of the mappings from this map.
   */
  @Override
  public void clear() {
    clear(false);
  }
  /**
   * Removes all of the mappings from this map.
   *
   * @param database True if the database values should be cleared too.
   */
  public void clear(boolean database) {
    super.clear();

    if(database) {
      listener.clear();
    }
  }

  /**
   * Returns a {@link Set} view of the keys contained in this map.
   * The set is backed by the map, so changes to the map are
   * reflected in the set, and vice-versa. The set supports element
   * removal, which removes the corresponding mapping from this map,
   * via the {@code Iterator.remove}, {@code Set.remove},
   * {@code removeAll}, {@code retainAll}, and {@code clear}
   * operations.  It does not support the {@code add} or
   * {@code addAll} operations.
   *
   * <p>The view's iterators and spliterators are
   * <a href="package-summary.html#Weakly"><i>weakly consistent</i></a>.
   *
   * <p>The view's {@code spliterator} reports {@link Spliterator#CONCURRENT},
   * {@link Spliterator#DISTINCT}, and {@link Spliterator#NONNULL}.
   *
   * @return the set view
   */
  @Override
  public KeySetView<K, V> keySet() {
    return super.keySet();
  }

  /**
   * Returns a {@link Collection} view of the values contained in this map.
   * The collection is backed by the map, so changes to the map are
   * reflected in the collection, and vice-versa.  The collection
   * supports element removal, which removes the corresponding
   * mapping from this map, via the {@code Iterator.remove},
   * {@code Collection.remove}, {@code removeAll},
   * {@code retainAll}, and {@code clear} operations.  It does not
   * support the {@code add} or {@code addAll} operations.
   *
   * <p>The view's iterators and spliterators are
   * <a href="package-summary.html#Weakly"><i>weakly consistent</i></a>.
   *
   * <p>The view's {@code spliterator} reports {@link Spliterator#CONCURRENT}
   * and {@link Spliterator#NONNULL}.
   *
   * @return the collection view
   */
  @Override
  public Collection<V> values() {
    return super.values();
  }

  /**
   * Returns a {@link Set} view of the mappings contained in this map.
   * The set is backed by the map, so changes to the map are
   * reflected in the set, and vice-versa.  The set supports element
   * removal, which removes the corresponding mapping from the map,
   * via the {@code Iterator.remove}, {@code Set.remove},
   * {@code removeAll}, {@code retainAll}, and {@code clear}
   * operations.
   *
   * <p>The view's iterators and spliterators are
   * <a href="package-summary.html#Weakly"><i>weakly consistent</i></a>.
   *
   * <p>The view's {@code spliterator} reports {@link Spliterator#CONCURRENT},
   * {@link Spliterator#DISTINCT}, and {@link Spliterator#NONNULL}.
   *
   * @return the set view
   */
  @Override
  public Set<Entry<K, V>> entrySet() {
    return entrySet(false);
  }

  /**
   * Returns a {@link Set} view of the mappings contained in this map.
   * The set is backed by the map, so changes to the map are
   * reflected in the set, and vice-versa.  The set supports element
   * removal, which removes the corresponding mapping from the map,
   * via the {@code Iterator.remove}, {@code Set.remove},
   * {@code removeAll}, {@code retainAll}, and {@code clear}
   * operations.
   *
   * <p>The view's iterators and spliterators are
   * <a href="package-summary.html#Weakly"><i>weakly consistent</i></a>.
   *
   * <p>The view's {@code spliterator} reports {@link Spliterator#CONCURRENT},
   * {@link Spliterator#DISTINCT}, and {@link Spliterator#NONNULL}.
   *
   *
   * @param database True if the entryset should be taken directly from the database.
   * @return the set view
   */
  public Set<Entry<K, V>> entrySet(boolean database) {

    if(database || !cache) {
      return listener.entrySet();
    }
    return super.entrySet();
  }

  /**
   * {@inheritDoc}
   *
   * @param key
   * @param value
   *
   * @return the previous value associated with the specified key,
   * or {@code null} if there was no mapping for the key
   * @throws NullPointerException if the specified key or value is null
   */
  @Override
  public V putIfAbsent(K key, V value) {
    return super.putIfAbsent(key, value);
  }

  /**
   * {@inheritDoc}
   *
   * @param key
   * @param value
   *
   * @throws NullPointerException if the specified key is null
   */
  @Override
  public boolean remove(Object key, Object value) {
    return super.remove(key, value);
  }

  /**
   * {@inheritDoc}
   *
   * @param key
   * @param oldValue
   * @param newValue
   *
   * @throws NullPointerException if any of the arguments are null
   */
  @Override
  public boolean replace(K key, V oldValue, V newValue) {
    return super.replace(key, oldValue, newValue);
  }

  /**
   * {@inheritDoc}
   *
   * @param key
   * @param value
   *
   * @return the previous value associated with the specified key,
   * or {@code null} if there was no mapping for the key
   * @throws NullPointerException if the specified key or value is null
   */
  @Override
  public V replace(K key, V value) {
    return super.replace(key, value);
  }

  /**
   * Returns the value to which the specified key is mapped, or the
   * given default value if this map contains no mapping for the
   * key.
   *
   * @param key          the key whose associated value is to be returned
   * @param defaultValue the value to return if this map contains
   *                     no mapping for the given key
   *
   * @return the mapping for the key, if present; else the default value
   * @throws NullPointerException if the specified key is null
   */
  @Override
  public V getOrDefault(Object key, V defaultValue) {
    return super.getOrDefault(key, defaultValue);
  }

  @Override
  public void forEach(BiConsumer<? super K, ? super V> action) {
    super.forEach(action);
  }

  @Override
  public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
    super.replaceAll(function);
  }

  /**
   * If the specified key is not already associated with a value,
   * attempts to compute its value using the given mapping function
   * and enters it into this map unless {@code null}.  The entire
   * method invocation is performed atomically, so the function is
   * applied at most once per key.  Some attempted update operations
   * on this map by other threads may be blocked while computation
   * is in progress, so the computation should be short and simple,
   * and must not attempt to update any other mappings of this map.
   *
   * @param key             key with which the specified value is to be associated
   * @param mappingFunction the function to compute a value
   *
   * @return the current (existing or computed) value associated with
   * the specified key, or null if the computed value is null
   * @throws NullPointerException  if the specified key or mappingFunction
   *                               is null
   * @throws IllegalStateException if the computation detectably
   *                               attempts a recursive update to this map that would
   *                               otherwise never complete
   * @throws RuntimeException      or Error if the mappingFunction does so,
   *                               in which case the mapping is left unestablished
   */
  @Override
  public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
    return super.computeIfAbsent(key, mappingFunction);
  }

  /**
   * If the value for the specified key is present, attempts to
   * compute a new mapping given the key and its current mapped
   * value.  The entire method invocation is performed atomically.
   * Some attempted update operations on this map by other threads
   * may be blocked while computation is in progress, so the
   * computation should be short and simple, and must not attempt to
   * update any other mappings of this map.
   *
   * @param key               key with which a value may be associated
   * @param remappingFunction the function to compute a value
   *
   * @return the new value associated with the specified key, or null if none
   * @throws NullPointerException  if the specified key or remappingFunction
   *                               is null
   * @throws IllegalStateException if the computation detectably
   *                               attempts a recursive update to this map that would
   *                               otherwise never complete
   * @throws RuntimeException      or Error if the remappingFunction does so,
   *                               in which case the mapping is unchanged
   */
  @Override
  public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
    return super.computeIfPresent(key, remappingFunction);
  }

  /**
   * Tests if some key maps into the specified value in this table.
   *
   * <p>Note that this method is identical in functionality to
   * {@link #containsValue(Object)}, and exists solely to ensure
   * full compatibility with class {@link Hashtable},
   * which supported this method prior to introduction of the
   * Java Collections Framework.
   *
   * @param value a value to search for
   *
   * @return {@code true} if and only if some key maps to the
   * {@code value} argument in this table as
   * determined by the {@code equals} method;
   * {@code false} otherwise
   * @throws NullPointerException if the specified value is null
   */
  @Override
  public boolean contains(Object value) {
    return containsValue(value, CheckLevel.MAP_FIRST);
  }

  /**
   * Tests if some key maps into the specified value in this table.
   *
   * <p>Note that this method is identical in functionality to
   * {@link #containsValue(Object)}, and exists solely to ensure
   * full compatibility with class {@link Hashtable},
   * which supported this method prior to introduction of the
   * Java Collections Framework.
   *
   * @param value a value to search for
   *
   * @return {@code true} if and only if some key maps to the
   * {@code value} argument in this table as
   * determined by the {@code equals} method;
   * {@code false} otherwise
   * @throws NullPointerException if the specified value is null
   */
  public boolean contains(Object value, CheckLevel level) {
    return containsValue(value, level);
  }

  /**
   * Returns an enumeration of the keys in this table.
   *
   * @return an enumeration of the keys in this table
   * @see #keySet()
   */
  @Override
  public Enumeration<K> keys() {
    return keys(false);
  }


  /**
   * Returns an enumeration of the values in this table.
   *
   * @param database True if the keys should be pulled directly from the database.
   *
   * @return an enumeration of the values in this table
   * @see #values()
   */
  public Enumeration<K> keys(boolean database) {

    if(!cache || database) {
      return listener.keys();
    }
    return super.keys();
  }

  /**
   * Returns an enumeration of the values in this table.
   *
   * @return an enumeration of the values in this table
   * @see #values()
   */
  @Override
  public Enumeration<V> elements() {
    return elements(false);
  }


  /**
   * Returns an enumeration of the values in this table.
   *
   * @param database True if the keys should be pulled directly from the database.
   * @return an enumeration of the values in this table
   * @see #values()
   */
  public Enumeration<V> elements(boolean database) {

    if(!cache || database) {
      return listener.elements();
    }
    return super.elements();
  }

  /**
   * DOES NOT SUPPORT DATABASE FUNCTIONALITY.
   *
   * Returns a {@link Set} view of the keys in this map, using the
   * given common mapped value for any additions (i.e., {@link
   * Collection#add} and {@link Collection#addAll(Collection)}).
   * This is of course only appropriate if it is acceptable to use
   * the same value for all additions from this view.
   *
   * @param mappedValue the mapped value to use for any additions
   *
   * @return the set view
   * @throws NullPointerException if the mappedValue is null
   */
  @Override
  public KeySetView<K, V> keySet(V mappedValue) {
    return super.keySet(mappedValue);
  }

  /**
   * Used to refresh the value associated with the key from the database. If the refresh is not
   * successful then the old value will be removed if purge is true.
   *
   * @param key The key associated with the object.
   */
  public void refresh(K key) {

    if(cache) {

      final V value = get(key);
      if(value != null) {

        listener.preRefresh(key, value);

        value.getData().refresh(key, value, (refreshed, successful)->{

          if(successful) {
            put(key, refreshed);
          } else {

            if(purge) {
              remove(key);
            }
          }
        });
      }
    }
  }

  public ConcurrentHashMap<K, Long> getKeyExpiration() {
    return keyExpiration;
  }

  public int getExpiration() {
    return expiration;
  }

  public boolean canRefresh() {
    return refresh;
  }

  public void setRefresh(boolean refresh) {
    this.refresh = refresh;
  }

  public void setCache(boolean cache) {
    this.cache = cache;
  }
}