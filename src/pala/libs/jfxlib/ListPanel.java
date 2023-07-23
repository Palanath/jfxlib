package pala.libs.jfxlib;

import java.util.function.Function;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import pala.libs.generic.javafx.bindings.BindingTools;
import pala.libs.generic.javafx.bindings.BindingTools.Unbindable;

public abstract class ListPanel<E> extends ScrollPane implements Function<E, Node> {

	private final BooleanProperty horizontal = new SimpleBooleanProperty();
	private final ReadOnlyObjectWrapper<Pane> itemContainer = new ReadOnlyObjectWrapper<>(new VBox());
	private Unbindable binding;
	private final ObjectProperty<ObservableList<? extends E>> items = new SimpleObjectProperty<>();

	{

		items.addListener(new ChangeListener<ObservableList<? extends E>>() {

			@Override
			public void changed(ObservableValue<? extends ObservableList<? extends E>> observable,
					ObservableList<? extends E> oldValue, ObservableList<? extends E> newValue) {
				if (binding != null)
					binding.unbind();
				itemContainer.get().getChildren().clear();
				if (newValue != null)
					binding = BindingTools.bind(newValue, itemContainer.get().getChildren(), ListPanel.this);
			}
		});
		horizontal.addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (binding != null)
					binding.unbind();
				itemContainer.set(newValue ? new HBox() : new VBox());
				if (items.get() != null)
					binding = BindingTools.bind(items.get(), itemContainer.get().getChildren(), ListPanel.this);
			}
		});
	}

	public ListPanel(ObservableList<? extends E> items) {
		this.items.set(items);
	}

	public ListPanel() {
	}

	protected abstract Node toNode(E item);

	public final BooleanProperty horizontalProperty() {
		return this.horizontal;
	}

	public final boolean isHorizontal() {
		return this.horizontalProperty().get();
	}

	public final void setHorizontal(final boolean horizontal) {
		this.horizontalProperty().set(horizontal);
	}

	public final ObjectProperty<ObservableList<? extends E>> itemsProperty() {
		return this.items;
	}

	public final ObservableList<? extends E> getItems() {
		return this.itemsProperty().get();
	}

	public final void setItems(final ObservableList<? extends E> items) {
		this.itemsProperty().set(items);
	}

	public final ReadOnlyObjectProperty<Pane> itemContainerProperty() {
		return this.itemContainer.getReadOnlyProperty();
	}

	/**
	 * Gets the container that stores the items in this {@link ListPanel}. Its
	 * children should not be modified other than through this class. Its layout can
	 * be modified or it can be styled. If {@link #isHorizontal()} is
	 * <code>true</code>, the returned {@link Pane} is an {@link HBox}, otherwise it
	 * is a {@link VBox}.
	 * 
	 * @return The {@link HBox} or {@link VBox} containing the items in this
	 *         {@link ListPanel}.
	 */
	public final Pane getItemContainer() {
		return this.itemContainerProperty().get();
	}

}
