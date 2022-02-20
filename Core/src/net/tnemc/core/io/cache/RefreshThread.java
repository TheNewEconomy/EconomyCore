package net.tnemc.core.io.cache;

import net.tnemc.core.io.Datable;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class RefreshThread<K, V extends Datable<K, V>> extends Thread {

  private final RefreshConcurrentMap<K, V> map;

  public RefreshThread(@NotNull RefreshConcurrentMap<K, V> map) {
    this.map = map;
  }

  /**
   * If this thread was constructed using a separate
   * {@code Runnable} run object, then that
   * {@code Runnable} object's {@code run} method is called;
   * otherwise, this method does nothing and returns.
   * <p>
   * Subclasses of {@code Thread} should override this method.
   *
   * @see #start()
   * @see #stop()
   */
  @Override
  public void run() {
    final long time = new Date().getTime();

    for(K key : map.getKeyExpiration().keySet()) {

      if(time >= map.getKeyExpiration().get(key) - map.getExpiration()) {
        map.refresh(key);
      }
    }
  }
}