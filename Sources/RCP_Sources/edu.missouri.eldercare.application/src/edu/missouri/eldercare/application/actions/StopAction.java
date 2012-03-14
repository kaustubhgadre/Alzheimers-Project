package edu.missouri.eldercare.application.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import edu.missouri.eldercare.application.views.ElderCareView;

public class StopAction extends Action implements IWorkbenchAction {

	private static final String ID = "edu.missouri.eldercare.application.actions.StopAction";
	private ElderCareView elderCareView;

	public StopAction() {
		setId(ID);
	}

	public void dispose() {
		// TODO Auto-generated method stub

	}

	public void run() {
		elderCareView = (ElderCareView) PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().findView(
						ElderCareView.VIEW_ID);
		if (!Client.isClientNull()) {
			Client client = Client.getInstance();
			client.removeDataUpdateListener(elderCareView);
			client.logout();
		}

		if (!CSVFileThread.isCSVThreadNull()) {
			CSVFileThread csvTh = CSVFileThread.getInstance();
			csvTh.removeDataUpdateListener(elderCareView);
			csvTh.stopDisplay();
		}
	}
}
