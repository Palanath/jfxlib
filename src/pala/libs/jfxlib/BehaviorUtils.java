package pala.libs.jfxlib;

import java.util.List;
import java.util.function.Function;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import pala.libs.generic.javafx.bindings.BindingTools;
import pala.libs.generic.javafx.bindings.BindingTools.Unbindable;

public class BehaviorUtils {
	public static <E> Unbindable bind(ObservableList<? extends E> sourceList, List<? super Node> containerChildList,
			Function<? super E, ? extends Node> nodeMaker) {
		return BindingTools.bind(sourceList, containerChildList, nodeMaker);
	}

	public static <E> Unbindable bind(ObservableList<? extends E> sourceList, Pane nodeContainer,
			Function<? super E, ? extends Node> nodeMaker) {
		return bind(sourceList, nodeContainer.getChildren(), nodeMaker);
	}
}
