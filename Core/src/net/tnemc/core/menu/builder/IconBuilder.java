package net.tnemc.core.menu.builder;

import net.tnemc.core.menu.callbacks.icon.IconClickCallback;
import net.tnemc.core.menu.icon.Icon;
import net.tnemc.core.menu.icon.IconAction;
import net.tnemc.core.utils.constraints.Constraint;
import net.tnemc.item.AbstractItemStack;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class IconBuilder {

  private final Map<String, String> constraints = new HashMap<>();

  private final List<IconAction> actions = new LinkedList<>();

  private int slot;
  private AbstractItemStack<?> item;

  //Callbacks
  protected Consumer<IconClickCallback> click;

  public static IconBuilder of(AbstractItemStack<?> item) {
    return new IconBuilder(item);
  }

  public IconBuilder(AbstractItemStack<?> item) {
    this.item = item;
  }

  public IconBuilder withSlot(int slot) {
    this.slot = slot;
    return this;
  }

  public IconBuilder withItem(AbstractItemStack<?> item) {
    this.item = item;
    return this;
  }

  public IconBuilder click(Consumer<IconClickCallback> callback) {
    this.click = callback;
    return this;
  }

  public <TYPE> IconBuilder withConstraint(Constraint<TYPE> constraint, TYPE value) {
    this.constraints.put(constraint.identifier(), constraint.asString(value));
    return this;
  }

  public IconBuilder withAction(IconAction action) {
    this.actions.add(action);
    return this;
  }

  public Icon create() {
    Icon icon = new Icon(slot, item);
    icon.constraints().putAll(constraints);
    icon.getActions().addAll(actions);
    icon.setClick(click);
    return icon;
  }
}