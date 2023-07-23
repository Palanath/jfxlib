package pala.libs.jfxlib.theming;

import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import pala.libs.generic.javafx.FXTools;

public class GrayTheme {
	public static final Border BORDER = FXTools.getBorderFromColor(Color.gray(.75));
	public static final Background BACKGROUND_LIGHT = FXTools.getBackgroundFromColor(Color.gray(.9)),
			BACKGROUND_DARK = FXTools.getBackgroundFromColor(Color.gray(.85));

	public static void styleContainersLight(Region... containers) {
		for (Region r : containers) {
			r.setBorder(BORDER);
			r.setBackground(BACKGROUND_LIGHT);
		}
	}

	public static void styleContainersDark(Region... containers) {
		for (Region r : containers) {
			r.setBorder(BORDER);
			r.setBackground(BACKGROUND_DARK);
		}
	}
}
