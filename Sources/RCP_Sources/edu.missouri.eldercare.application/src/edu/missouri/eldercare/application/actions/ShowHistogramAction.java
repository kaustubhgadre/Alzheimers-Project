/**
 * 
 */
package edu.missouri.eldercare.application.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.rse.services.clientserver.messages.SystemMessageException;
import org.eclipse.rse.subsystems.files.core.subsystems.IRemoteFile;
import org.eclipse.rse.subsystems.files.core.subsystems.IRemoteFileSubSystem;
import org.eclipse.rse.subsystems.files.core.subsystems.RemoteFile;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import edu.missouri.eldercare.application.ui.Histogram3DDialog;

/**
 * @author Kaustubh
 * 
 */
public class ShowHistogramAction extends Action implements IWorkbenchAction,
		IObjectActionDelegate {

	private static final String ID = "edu.missouri.eldercare.application.actions.ShowHistogramAction";

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

	// @Override
	public void run() {/*
						 * HistogramDialog dialog = new
						 * HistogramDialog(Display.getDefault()
						 * .getActiveShell(), SWT.NULL); dialog.open();
						 */
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

	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		// TODO Auto-generated method stub

	}

	public void run(IAction action) {
		ISelection selection = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getSelectionService()
				.getSelection();
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection isselection = (IStructuredSelection) selection;
			Object firstElement = isselection.getFirstElement();
			if (firstElement instanceof IRemoteFile) {
				RemoteFile rf = (RemoteFile) firstElement;
				IRemoteFileSubSystem rfss = rf.getParentRemoteFileSubSystem();
				String home = System.getProperty("user.home");
				String path = home + "//temp.csv";
				try {
					rfss.download(rf, path, "Cp1252", null);
					/*
					 * HistogramDialog dialog = new HistogramDialog(Display
					 * .getDefault().getActiveShell(), path, SWT.NULL);
					 * dialog.open();
					 */
					Histogram3DDialog hs3d = new Histogram3DDialog(Display
							.getDefault().getActiveShell(), path, SWT.NULL);
					hs3d.open();
					/*
					 * PlotDialog pdialog = new
					 * PlotDialog(Display.getDefault().getActiveShell(), path,
					 * SWT.NULL); pdialog.open();
					 */
					/*
					 * PathDialog pathDialog = new
					 * PathDialog(Display.getDefault().getActiveShell(), path,
					 * SWT.NULL); pathDialog.open();
					 */
				} catch (SystemMessageException e) {
					e.printStackTrace();
				}
			}
		}
		/*
		 * MessageDialog.openInformation(Display.getDefault().getActiveShell(),
		 * PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell()
		 * .getText(), "Under Construction");
		 */
	}

	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub

	}
}
