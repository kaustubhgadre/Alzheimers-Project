package edu.missouri.eldercare.application.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.rse.services.clientserver.messages.SystemMessageException;
import org.eclipse.rse.subsystems.files.core.subsystems.IRemoteFile;
import org.eclipse.rse.subsystems.files.core.subsystems.IRemoteFileSubSystem;
import org.eclipse.rse.subsystems.files.core.subsystems.RemoteFile;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import edu.missouri.eldercare.application.views.ElderCareView;

public class DisplayHistoryAction implements IObjectActionDelegate {

	private ElderCareView elderCareView;
	private static Thread displayThread;

	public DisplayHistoryAction() {
	}

	// @Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		// TODO Auto-generated method stub

	}

	// @Override
	public void run(IAction action) {
		elderCareView = (ElderCareView) PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().findView(
						ElderCareView.VIEW_ID);
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
					CSVFileThread csvth = CSVFileThread.getInstance();
					csvth.setPath(path);
					csvth.addDataUpdateListener(elderCareView);
					displayThread = new Thread(csvth);
					displayThread.start();
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

	// @Override
	public void selectionChanged(IAction action, ISelection selection) {
	}

}
