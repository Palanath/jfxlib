package pala.libs.jfxlib;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

public interface DragAndDropHandler extends EventHandler<DragEvent> {

	public interface DragAcceptorBase {
		/**
		 * <p>
		 * Is expected to take and process the content from the drag event and return
		 * <code>true</code> on successful acquisition and processing, or to return
		 * <code>false</code>, or throw an {@link Exception}, on failure.
		 * </p>
		 * <p>
		 * This method should not call {@link DragEvent#setDropCompleted(boolean)} nor
		 * {@link DragEvent#consume()}; both are called by the caller.
		 * </p>
		 * 
		 * @param event The {@link DragEvent}.
		 * @return <code>true</code> on success and <code>false</code> on failure.
		 * @throws Exception If an exception occurs during processing.
		 */
		boolean accept(DragEvent event) throws Exception;
	}

	public interface DragAcceptor extends DragAcceptorBase {
		@Override
		default boolean accept(DragEvent event) throws Exception {
			return accept(event.getDragboard());
		}

		/**
		 * Accepts the content from the {@link Dragboard}, returning <code>true</code>
		 * on success or returning <code>false</code> or throwing an {@link Exception}
		 * on failure.
		 * 
		 * @param dragboard The {@link Dragboard} to acquire and process the content
		 *                  from.
		 * @return <code>true</code> on success, <code>false</code> on failure.
		 * @throws Exception If an {@link Exception} occurs while accepting or
		 *                   processing {@link Dragboard} content.
		 */
		boolean accept(Dragboard dragboard) throws Exception;
	}

	static DragAndDropHandler from(DragAcceptorBase draggedDataHandler, TransferMode[] acceptedTransferModes,
			DataFormat... acceptedFormats) {
		return event -> {
			if (event.getEventType() == DragEvent.DRAG_OVER) {
				for (DataFormat df : acceptedFormats)
					if (event.getDragboard().hasContent(df)) {
						event.acceptTransferModes(acceptedTransferModes);
						break;
					}
				event.consume();
			} else if (event.getEventType() == DragEvent.DRAG_DROPPED) {
				boolean success;
				try {
					success = draggedDataHandler.accept(event);
				} catch (Exception e) {
					e.printStackTrace();
					success = false;
				}
				event.setDropCompleted(success);
				event.consume();
			}
		};

	}

	static DragAndDropHandler from(DragAcceptorBase draggedDataHandler, TransferMode transferMode,
			DataFormat... acceptedDataFormats) {
		return from(draggedDataHandler, new TransferMode[] { transferMode }, acceptedDataFormats);
	}

	/**
	 * Creates a {@link DragAndDropHandler} handler which handles each of the
	 * provided {@link DragFunction}'s {@link DragFunction#dataFormat() data
	 * formats} in different ways (with {@link TransferMode}s specified by the
	 * respective {@link DragFunction} and with a handler specified by the
	 * {@link DragFunction}).
	 * 
	 * @param functions The {@link DragFunction}s to use.
	 * @return The new {@link DragAndDropHandler}.
	 */
	static DragAndDropHandler from(DragFunction<?>... functions) {
		return new DragAndDropHandler() {
			@SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
			public void handle(DragEvent event) {
				if (event.getEventType() == DragEvent.DRAG_OVER) {
					for (DragFunction<?> df : functions)
						if (event.getDragboard().hasContent(df.dataFormat())) {
							event.acceptTransferModes(df.getTransferModes());
							break;
						}
					event.consume();
				} else if (event.getEventType() == DragEvent.DRAG_DROPPED) {
					boolean success = false;
					try {
						for (DragFunction<?> df : functions)
							if (event.getDragboard().hasContent(df.dataFormat()))
								success = ((DragFunction) df).handle(event.getDragboard().getContent(df.dataFormat()));
					} catch (Exception e) {
						e.printStackTrace();
						success = false;
					}
					event.setDropCompleted(success);
					event.consume();
				}
			}
		};
	}

	interface DragFunction<D> {
		DataFormat dataFormat();

		TransferMode[] getTransferModes();

		boolean handle(D data) throws Exception;

		interface Handler<D> {
			boolean handle(D data) throws Exception;
		}

		static <D> DragFunction<D> from(DataFormat format, TransferMode[] transferModes, Handler<? super D> handler) {
			return new DragFunction<D>() {

				@Override
				public DataFormat dataFormat() {
					return format;
				}

				@Override
				public TransferMode[] getTransferModes() {
					return transferModes;
				}

				@Override
				public boolean handle(D data) throws Exception {
					return handler.handle(data);
				}
			};
		}
	}

	default void attach(Node node) {
		node.setOnDragOver(this);
		node.setOnDragDropped(this);
	}

}
