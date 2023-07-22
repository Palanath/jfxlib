package pala.libs.jfxlib;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

public class DragAndDropHandler implements EventHandler<DragEvent> {

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

	private final DataFormat[] acceptedFormats;
	private final TransferMode[] transferModes;
	private final DragAcceptorBase dab;

	public DragAndDropHandler(DragAcceptorBase dragAcceptorBase, TransferMode[] acceptedTransferModes,
			DataFormat... acceptedDataFormats) {
		acceptedFormats = acceptedDataFormats;
		transferModes = acceptedTransferModes;
		dab = dragAcceptorBase;
	}

	@Override
	public void handle(DragEvent event) {
		if (event.getEventType() == DragEvent.DRAG_OVER) {
			for (DataFormat df : acceptedFormats)
				if (event.getDragboard().hasContent(df)) {
					event.acceptTransferModes(transferModes);
					break;
				}
			event.consume();
		} else if (event.getEventType() == DragEvent.DRAG_DROPPED) {
			boolean success;
			try {
				success = dab.accept(event);
			} catch (Exception e) {
				e.printStackTrace();
				success = false;
			}
			event.setDropCompleted(success);
			event.consume();
		}
	}

	public void attach(Node node) {
		node.setOnDragOver(this);
		node.setOnDragDropped(this);
	}

}
