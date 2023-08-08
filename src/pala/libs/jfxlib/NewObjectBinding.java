package pala.libs.jfxlib;

import java.util.function.Function;

import javafx.beans.binding.ObjectBinding;
import javafx.beans.value.ObservableValue;

public abstract class NewObjectBinding<T> extends ObjectBinding<T> {
	public <R> NewObjectBinding<R> then(Function<? super T, ? extends R> function) {
		return JavaFXUtils.property(this, function);
	}

	public <R> NewObjectBinding<R> nested(Function<? super T, ? extends ObservableValue<? extends R>> function) {
		return JavaFXUtils.nested(this, function);
	}
}
