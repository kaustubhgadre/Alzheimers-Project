package edu.missouri.eldercare.application;

import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import edu.missouri.eldercare.application.ftp.ConnectionInfo;
import edu.missouri.eldercare.application.utilities.ConnectionDialog;

public class ShowHistoryAction extends Action implements IWorkbenchAction {

	private static final String ID = "edu.missouri.eldercare.application.actions.ShowHistoryAction";
	private FTPClient ftp;

	public ShowHistoryAction() {
		setId(ID);
	}

	public void run() {
		ftp = new FTPClient();
		ConnectionDialog dialog = new ConnectionDialog(Display.getDefault()
				.getActiveShell());
		if (dialog.open() == Dialog.OK) {
			ConnectionInfo connectionInfo = dialog.getConnectionInfo();
			if (connectionInfo != null) {
				try {
					ftp.connect(connectionInfo.host, connectionInfo.port);
					if (!FTPReply.isPositiveCompletion(ftp.getReplyCode()))
						throw new RuntimeException(
								"FTP server refused connection.");
					boolean login = ftp.login(connectionInfo.username,
							connectionInfo.password);
					if (login) {
						FTPFile[] listFiles = ftp.listFiles("/carpet/data");
						for (int i = 0; i < listFiles.length; i++) {
							System.out.println(listFiles[i].getName());
						}
					}
				} catch (SocketException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public void dispose() {

	}

}
