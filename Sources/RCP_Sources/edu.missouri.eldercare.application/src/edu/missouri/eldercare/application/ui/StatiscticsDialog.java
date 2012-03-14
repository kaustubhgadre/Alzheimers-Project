package edu.missouri.eldercare.application.ui;

import java.io.File;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.ole.win32.OleClientSite;
import org.eclipse.swt.ole.win32.OleFrame;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

public class StatiscticsDialog extends Dialog {

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public StatiscticsDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(SWT.DIALOG_TRIM | SWT.MAX | SWT.PRIMARY_MODAL);
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	//@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(1, false));
		OleFrame frame = new OleFrame(container, SWT.NO_BACKGROUND);
		frame.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		frame.setLayout(new GridLayout(1, false));
		OleClientSite site = new OleClientSite(frame, SWT.NO_BACKGROUND,
				"Excel.Sheet", new File("C:/temp/temporary.xls"));
		site.setLayout(new GridLayout(1, false));
		site.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		site.setFocus();
		return container;
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	//@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	//@Override
	protected Point getInitialSize() {
		return new Point(800, 600);
	}

	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("Statistics");
	}
}
