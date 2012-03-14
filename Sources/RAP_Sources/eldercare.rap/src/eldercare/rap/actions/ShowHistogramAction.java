/**
 * 
 */
package eldercare.rap.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import eldercare.rap.ui.HistogramDialog;

/**
 * @author Kaustubh
 * 
 */
public class ShowHistogramAction extends Action implements IWorkbenchAction {

	private static final String ID = "eldercare.rap.actions.ShowHistogramAction";

	/**
	 * 
	 */
	public ShowHistogramAction() {
		setId(ID);
	}

	/**
	 * @param text
	 */
	public ShowHistogramAction(String text) {
		super(text);
		setId(ID);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param text
	 * @param image
	 */
	public ShowHistogramAction(String text, ImageDescriptor image) {
		super(text, image);
		// TODO Auto-generated constructor stub
	}

	//@Override
	public void run() {
		HistogramDialog dialog = new HistogramDialog(Display.getDefault()
				.getActiveShell(), SWT.NULL);
		dialog.open();

	}

	public ShowHistogramAction(String text, String id) {
		setText(text);
		setId(id);
		// setAccelerator(SWT.CTRL | 'R');
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.actions.ActionFactory.IWorkbenchAction#dispose()
	 */
	public void dispose() {
		// TODO Auto-generated method stub

	}
}
