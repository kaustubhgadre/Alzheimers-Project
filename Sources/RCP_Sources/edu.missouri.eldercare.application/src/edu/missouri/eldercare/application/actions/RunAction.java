/**
 * 
 */
package edu.missouri.eldercare.application.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import edu.missouri.eldercare.application.utilities.DataUpdateListener;
import edu.missouri.eldercare.application.views.ElderCareView;

/**
 * @author Kaustubh
 * 
 */
public class RunAction extends Action implements IWorkbenchAction,
		DataUpdateListener {

	private static final String ID = "edu.missouri.eldercare.application.actions.RunAction";
	private ElderCareView elderCareView;

	/**
	 * 
	 */
	public RunAction() {
		setId(ID);
	}

	/**
	 * @param text
	 */
	public RunAction(String text) {
		super(text);
		setId(ID);
	}

	/**
	 * @param text
	 * @param image
	 */
	public RunAction(String text, ImageDescriptor image) {
		super(text, image);
	}

	public void run() {
		try {
			elderCareView = (ElderCareView) PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getActivePage().showView(
							ElderCareView.VIEW_ID);
		} catch (PartInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Client client = Client.getInstance();
		client.addDataUpdateListener(elderCareView);
		new Thread(client).start();
	}

	public RunAction(String text, String id) {
		setText(text);
		setId(id);
		setAccelerator(SWT.CTRL | 'R');
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.actions.ActionFactory.IWorkbenchAction#dispose()
	 */
	public void dispose() {
		// TODO Auto-generated method stub

	}

	// @Override
	public void dataUpdated(int[] finalData) {
		((ElderCareView) elderCareView).refreshBlocks(finalData);
	}
}
