package pala.libs.jfxlib;

import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

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
	public static StackPane getCenteredText(String text, Color color, Font font) {
		Text t = new Text(text);
		t.setFill(color);
		t.setFont(font);
		return new StackPane(t);
	}

	public static void setVGrow(Priority priority, Node... nodes) {
		for (Node n : nodes)
			VBox.setVgrow(n, priority);
	}

	public static void setHGrow(Priority priority, Node... nodes) {
		for (Node n : nodes)
			HBox.setHgrow(n, priority);
	}
}
