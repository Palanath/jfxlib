package pala.libs.jfxlib;

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
}
