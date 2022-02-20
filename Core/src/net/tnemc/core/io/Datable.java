package net.tnemc.core.io;

/**
 * Represents an object that is associated with information in a database.
 *
 * @since 0.1.1.17
 * @author creatorfromhell
 */
public interface Datable<K, V> {

  /**
   * Used to get the {@link Queryable data part} of this object. This is what houses all the
   * IO logic.
   *
   * @return The {@link Queryable data part} of this object. This is what houses all the
   *         IO logic.
   */
  Queryable<K, V> getData();

  /**
   * Sets the {@link Queryable data part} of this object. This is what houses all the
   * IO logic.
   *
   * @param dataObject The data object to set to.
   */
  void setData(Queryable<K, V> dataObject);
}