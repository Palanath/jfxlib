package pala.libs.jfxlib;

import java.util.function.Function;

import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class JavaFXUtils {
	/**
	 * Creates and returns a {@link StackPane} whose only child node is a
	 * {@link Text} with the specified {@link Color} and {@link Font}. Any parameter
	 * may be <code>null</code>. The parameters are passed directly to the
	 * {@link Text} object's methods.
	 * 
	 * @param text  The {@link String} value that the {@link Text} will render.
	 * @param color The {@link Color} of the {@link Text}.
	 * @param font  The {@link Font} used to render the {@link Text}.
	 * @return A new {@link StackPane} whose only child is the created {@link Text}.
	 */
	public static StackPane makeCenteredText(String text, Color color, Font font) {
		return new StackPane(makeText(text, color, font));
	}

	public static Text makeText(String text, Color color, Font font) {
		Text t = new Text(text);
		t.setFill(color);
		t.setFont(font);
		return t;
	}

	public static void setVGrow(Priority priority, Node... nodes) {
		for (Node n : nodes)
			VBox.setVgrow(n, priority);
	}

	public static void setHGrow(Priority priority, Node... nodes) {
		for (Node n : nodes)
			HBox.setHgrow(n, priority);
	}

	public static void setSizeConstraints(Stage stage, double minWidth, double minHeight, double maxWidth,
			double maxHeight) {
		stage.setMinWidth(minWidth);
		stage.setMinHeight(minHeight);
		stage.setMaxWidth(maxWidth);
		stage.setMaxHeight(maxHeight);
	}

	public static void setSize(Stage stage, double width, double height) {
		stage.setWidth(width);
		stage.setHeight(height);
	}

	/**
	 * <p>
	 * Returns an object binding which is considered to have been invalidated when
	 * the provided {@link ObservableValue} is invalidated. The value of the binding
	 * is computed from the provided <code>valueFunction</code>, which is provided
	 * the current value of the {@link ObservableValue} upon execution.
	 * </p>
	 * 
	 * @param <P>       The type of the {@link ObservableValue}'s value.
	 * @param <R>       The type of value returned by the function.
	 * @param property  The parent property, for example, an instance of
	 *                  {@link StringProperty}.
	 * @param valueFunc A {@link Function} used to compute a new value from the
	 *                  provided {@link ObservableValue}.
	 * @return The new object binding.
	 */
	public static <P, R> NewObjectBinding<R> property(ObservableValue<? extends P> property,
			Function<? super P, ? extends R> valueFunc) {
		return new NewObjectBinding<R>() {
			{
				bind(property);
			}

			@Override
			public void dispose() {
				unbind(property);
			}

			@Override
			protected R computeValue() {
				return valueFunc.apply(property.getValue());
			}
		};
	}

	/**
	 * <p>
	 * Returns an object binding that is represents the nested property, returned
	 * from the provided <code>propertyAccessor</code>, that is contained within the
	 * provided parent property. When this nested property changes value, the
	 * returned object binding is updated, and when the parent property provided
	 * changes value, the returned property is updated to reflect the new parent's
	 * nested property's value.
	 * </p>
	 * <p>
	 * This is similar to the {@link #property(ObservableValue, Function)} method,
	 * but this method is for nested observables rather than nested constant values.
	 * </p>
	 * 
	 * @param <P>              The type of the parent property's value.
	 * @param <R>              The type of the child property's value.
	 * @param parentProperty   The parent property. For example, an instance of
	 *                         <code>ObjectProperty&lt;VBox&gt;</code>.
	 * @param propertyAccessor A {@link Function} used to access the child property
	 *                         within the parent property. Often of the form,
	 *                         <code>ParentValueType::someFieldProperty</code>, for
	 *                         example, <code>VBox::backgroundProperty</code>.
	 * @return The created {@link ObjectBinding}.
	 */
	public static <P, R> NewObjectBinding<R> nested(ObservableValue<? extends P> parentProperty,
			Function<? super P, ? extends ObservableValue<? extends R>> propertyAccessor) {
		class O extends NewObjectBinding<R> implements ChangeListener<P> {
			{
				bind(parentProperty);
				if (parentProperty.getValue() != null)
					bind(propertyAccessor.apply(parentProperty.getValue()));
			}

			@Override
			public void changed(ObservableValue<? extends P> observable, P oldValue, P newValue) {
				if (oldValue != null)
					unbind(propertyAccessor.apply(oldValue));
				if (newValue != null)
					bind(propertyAccessor.apply(newValue));
			}

			@Override
			public void dispose() {
				if (parentProperty.getValue() != null)
					unbind(propertyAccessor.apply(parentProperty.getValue()));
				unbind(parentProperty);
			}

			@Override
			protected R computeValue() {
				P p = parentProperty.getValue();
				return p == null ? null : propertyAccessor.apply(p).getValue();
			}

		}
		return new O();
	}
}
