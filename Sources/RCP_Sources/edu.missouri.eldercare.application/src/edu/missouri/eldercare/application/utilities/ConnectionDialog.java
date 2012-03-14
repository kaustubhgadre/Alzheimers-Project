package edu.missouri.eldercare.application.utilities;

import java.io.File;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.DialogSettings;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import edu.missouri.eldercare.application.ftp.ConnectionInfo;

public class ConnectionDialog extends Dialog {
	private static final String DIALOG_SETTING_FILE = "ftp.connection.xml";

	private static final String KEY_HOST = "HOST";

	private static final String KEY_PORT = "PORT";

	private static final String KEY_USERNAME = "USER";

	private static final String KEY_PASSWORD = "PASSWORD";

	Text textHost;

	Text textPort;

	Text textUsername;

	Text textPassword;

	DialogSettings dialogSettings;

	ConnectionInfo connectionInfo;

	public ConnectionDialog(Shell window) {
		super(window.getShell());
		connectionInfo = null;

		dialogSettings = new DialogSettings("FTP");
		try {
			//dialogSettings.load(DIALOG_SETTING_FILE);
		} catch (Exception e) {
			e.printStackTrace();
			// ignore.
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets
	 * .Composite)
	 */
	protected Control createDialogArea(Composite parent) {
		getShell().setText("Connection Settings");

		Composite composite = (Composite) super.createDialogArea(parent);
		composite.setLayout(new GridLayout(2, false));

		new Label(composite, SWT.NULL).setText("Host: ");
		textHost = new Text(composite, SWT.BORDER);
		textHost.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		new Label(composite, SWT.NULL).setText("Port: ");
		textPort = new Text(composite, SWT.BORDER);
		textPort.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		new Label(composite, SWT.NULL).setText("Username: ");
		textUsername = new Text(composite, SWT.BORDER);
		textUsername.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		new Label(composite, SWT.NULL).setText("Password: ");
		textPassword = new Text(composite, SWT.PASSWORD | SWT.BORDER);
		textPassword.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// sets initial values.
		try {
			textHost.setText(dialogSettings.get(KEY_HOST));
			textPort.setText(dialogSettings.getInt(KEY_PORT) + "");
			textUsername.setText(dialogSettings.get(KEY_USERNAME));
			textPassword.setText(dialogSettings.get(KEY_PASSWORD));
		} catch (Exception e) {
			// ignore.
		}

		return composite;
	}

	/**
	 * Returns a ConnectionInfo object containing connection information.
	 * 
	 * @return
	 */
	public ConnectionInfo getConnectionInfo() {
		return connectionInfo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	protected void okPressed() {
		try {
			if (!new File(DIALOG_SETTING_FILE).exists()) {
				new File(DIALOG_SETTING_FILE).createNewFile();
			}
			dialogSettings.put(KEY_HOST, textHost.getText());
			dialogSettings.put(KEY_PORT, Integer.parseInt(textPort.getText()
					.trim()));
			dialogSettings.put(KEY_USERNAME, textUsername.getText());
			dialogSettings.put(KEY_PASSWORD, textPassword.getText());
			dialogSettings.save(DIALOG_SETTING_FILE);
		} catch (Exception e) {
			e.printStackTrace();
			// ignore
		}

		connectionInfo = new ConnectionInfo();
		connectionInfo.host = textHost.getText();
		connectionInfo.port = Integer.parseInt(textPort.getText().trim());
		connectionInfo.username = textUsername.getText();
		connectionInfo.password = textPassword.getText();

		super.okPressed();
	}
}