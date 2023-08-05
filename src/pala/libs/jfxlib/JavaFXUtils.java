package pala.libs.jfxlib;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class JavaFXUtils {
	public static StackPane getCenteredText(String text, Color color, Font font) {
		Text t = new Text(text);
		t.setFill(color);
		t.setFont(font);
		return new StackPane(t);
	}
}
