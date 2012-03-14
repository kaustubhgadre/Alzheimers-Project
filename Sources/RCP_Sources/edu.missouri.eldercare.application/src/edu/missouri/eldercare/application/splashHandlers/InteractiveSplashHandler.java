package edu.missouri.eldercare.application.splashHandlers;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.splash.AbstractSplashHandler;
import com.swtdesigner.SWTResourceManager;

/**
 * @since 3.3
 * 
 */
public class InteractiveSplashHandler extends AbstractSplashHandler {

	private Composite fCompositeLogin;

	private Text fTextUsername;

	private Text fTextPassword;

	private Button fButtonOK;

	private Button fButtonCancel;

	private boolean fAuthenticated;

	// UserManager updateXML;

	private Shell splash;

	private Composite composite;

	private Composite composite_1;

	/**
                *
                 */
	public InteractiveSplashHandler() {
		fCompositeLogin = null;
		composite = null;
		composite_1 = null;
		fTextUsername = null;
		fTextPassword = null;
		fButtonOK = null;
		fButtonCancel = null;
		fAuthenticated = false;
		// updateXML = new UserManager();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.splash.AbstractSplashHandler#init(org.eclipse.swt.widgets
	 * .Shell)
	 */
	public void init(final Shell splash) {
		replaceShell(splash);
		// Store the shell
		super.init(splash);
		// Configure the shell layout
		configureUISplash();
		// Create UI
		createUI();
		// Create UI listeners
		createUIListeners();
		// Force the splash screen to layout
		getSplash().layout(true);
		// splash.layout(true);
		// Keep the splash screen visible and prevent the RCP application from
		// loading until the close button is clicked.
		doEventLoop();
	}

	private void replaceShell(Shell splash) {
		Shell newSplash = new Shell(Display.getCurrent(), SWT.NO_TRIM);
		newSplash.setBackgroundImage(splash.getBackgroundImage());
		newSplash.setBounds(splash.getBounds());
		newSplash.setFont(splash.getFont());
		newSplash.setText("Smart Carpet Display");
		try {
			Image img = new Image(Display.getDefault(), getClass()
					.getClassLoader().getResourceAsStream(
							"icons/MU_Stacked_Logo32x32.gif"));
			newSplash.setImage(img);
			// newSplash.setImage(splash.getBackgroundImage());
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		newSplash.setVisible(true);
		// newSplash.forceActive();
		setSplash(newSplash);
	}

	public Shell getSplash() {
		return splash;
	}

	public void setSplash(Shell splash) {
		this.splash = splash;
	}

	/**
                *
                 */
	private void doEventLoop() {
		Shell splash = getSplash();
		while (fAuthenticated == false) {
			if (splash.getDisplay().readAndDispatch() == false) {
				splash.getDisplay().sleep();
			}
		}
	}

	/**
                *
                 */
	private void createUIListeners() {
		// Create the OK button listeners
		createUIListenersButtonOK();
		// Create the cancel button listeners
		createUIListenersButtonCancel();
	}

	/**
                *
                 */
	private void createUIListenersButtonCancel() {
		fButtonCancel.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleButtonCancelWidgetSelected();
			}
		});
	}

	/**
                *
                 */
	private void handleButtonCancelWidgetSelected() {
		// Abort the loading of the RCP application
		getSplash().getDisplay().close();
		System.exit(0);
	}

	/**
                *
                 */
	private void createUIListenersButtonOK() {
		fButtonOK.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleButtonOKWidgetSelected();
			}
		});
	}

	/**
                *
                 */
	private void handleButtonOKWidgetSelected() {
		String username = fTextUsername.getText();
		String password = fTextPassword.getText();
		// Aunthentication is successful if a user provides any username and
		// any password
		if ((username.length() > 0) && (password.length() > 0)) {
			fAuthenticated = true;

			/*
			 * if (updateXML.verifyPassword(username, password)) {
			 * updateXML.setCurrentUser(username);
			 * fTextUsername.setEditable(false);
			 * fTextPassword.setEditable(false); splash.dispose(); } else {
			 * MessageDialog.openError(Display.getDefault().getActiveShell(),
			 * "Platform Builder", // NON-NLS-1
			 * "Login Id and password do not match"); fTextPassword.setText("");
			 * fTextPassword.setFocus(); }
			 */} else {
			MessageDialog.openError(Display.getDefault().getActiveShell(),
					"Smart Carpet Display", // NON-NLS-1
					"A username and password must be specified to login."); // NON-NLS-1
		}
		getSplash().close();
	}

	/**
                *
                 */
	private void createUI() {
		// Create the login panel
		createUICompositeLogin();
		// Create the blank spanner
		// Create the user name label
		createUILabelUserName();
		// Create the user name text widget
		createUITextUserName();
		// Create the password label
		createUILabelPassword();
		// Create the password text widget
		createUITextPassword();
		// Create the blank label
		createUILabelBlank();
		// Create the OK button
		createUIButtonOK();
		// Create the cancel button
		createUIButtonCancel();
	}

	/**
                *
                 */
	private void createUIButtonCancel() {
		// Create the button
		fButtonCancel = new Button(composite_1, SWT.PUSH);
		fButtonCancel.setText("Cancel"); // NON-NLS-1
		final GridData gd_cancelButton = new GridData(SWT.FILL, SWT.TOP, false,
				false);
		gd_cancelButton.widthHint = 73;
		fButtonCancel.setLayoutData(gd_cancelButton);

		// Configure layout data
		/*
		 * GridData data = new GridData(SWT.NONE, SWT.NONE, false, false);
		 * data.widthHint = F_BUTTON_WIDTH_HINT; data.verticalIndent = 10;
		 * fButtonCancel.setLayoutData(data);
		 */
	}

	/**
                *
                 */
	private void createUIButtonOK() {
		// Create the button
		fButtonOK = new Button(composite_1, SWT.PUSH);
		fButtonOK.setText("OK"); // NON-NLS-1
		// Configure layout data
		final GridData gd_okButton = new GridData(SWT.CENTER, SWT.TOP, false,
				false);
		gd_okButton.widthHint = 73;
		fButtonOK.setLayoutData(gd_okButton);
		// added by kaustubhg to make OK button default selected+
		getSplash().setDefaultButton(fButtonOK);
		// added by kaustubhg to make OK button default selected-
	}

	/**
                *
                 */
	private void createUILabelBlank() {
		/*
		 * Label label = new Label(fCompositeLogin, SWT.NONE);
		 * label.setVisible(false);
		 */
	}

	/**
                *
                 */
	private void createUITextPassword() {
		// Create the text widget
		int style = SWT.PASSWORD | SWT.BORDER;
		fTextPassword = new Text(composite, style);
		// Configure layout data
		final GridData gd_text_1 = new GridData(SWT.FILL, SWT.CENTER, false,
				false);
		gd_text_1.widthHint = 96;
		fTextPassword.setLayoutData(gd_text_1);
	}

	/**
                *
                 */
	private void createUILabelPassword() {
		// Create the label
		Label label = new Label(composite, SWT.NONE);
		label.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		label.setText("&Password:"); // NON-NLS-1
		// Configure layout data
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
	}

	/**
                *
                 */
	private void createUITextUserName() {
		// Create the text widget
		fTextUsername = new Text(composite, SWT.BORDER);
		fTextUsername.setFocus();
		fTextUsername.setLayoutData(new GridData(107, SWT.DEFAULT));

	}

	/**
                *
                 */
	private void createUILabelUserName() {
		// Create the label
		Label label = new Label(composite, SWT.NONE);
		label.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		label.setText("&User Name:"); // NON-NLS-1
		// Configure layout data
		/*
		 * GridData data = new GridData(); data.horizontalIndent =
		 * F_LABEL_HORIZONTAL_INDENT; label.setLayoutData(data);
		 */
	}

	/**
                *
                 */
	private void createUICompositeBlank() {
		// Composite spanner = new Composite(fCompositeLogin, SWT.BOTTOM);
		/*
		 * GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		 * data.horizontalSpan = 3; spanner.setLayoutData(data);
		 */
	}

	/**
                *
                 */
	private void createUICompositeLogin() {
		// Create the composite
		fCompositeLogin = new Composite(getSplash(), SWT.NONE);
		fCompositeLogin.setBackgroundMode(SWT.INHERIT_DEFAULT);
		fCompositeLogin.setLayout(new FormLayout());
		fCompositeLogin.setBackgroundImage(getSplash().getBackgroundImage());

		createUICompositeBlank();

		composite = new Composite(fCompositeLogin, SWT.INHERIT_DEFAULT);
		composite.setBackgroundMode(SWT.INHERIT_DEFAULT);
		final FormData fd_composite = new FormData();
		fd_composite.right = new FormAttachment(100, -14);
		fd_composite.top = new FormAttachment(0, 179);
		fd_composite.left = new FormAttachment(100, -202);
		composite.setLayoutData(fd_composite);
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		composite.setLayout(gridLayout);
		composite_1 = new Composite(fCompositeLogin, SWT.NONE);
		fd_composite.bottom = new FormAttachment(composite_1, -6);
		composite_1.setBackgroundMode(SWT.INHERIT_DEFAULT);
		final FormData fd_composite_1 = new FormData();
		fd_composite_1.bottom = new FormAttachment(100, -6);
		fd_composite_1.right = new FormAttachment(100, -2);
		fd_composite_1.top = new FormAttachment(0, 242);
		fd_composite_1.left = new FormAttachment(0, 331);
		composite_1.setLayoutData(fd_composite_1);
		final GridLayout gridLayout_1 = new GridLayout();
		gridLayout_1.numColumns = 2;
		composite_1.setLayout(gridLayout_1);
	}

	/**
                *
                 */
	private void configureUISplash() {
		// Configure layout
		FillLayout layout = new FillLayout();
		getSplash().setLayout(layout);
		// Force shell to inherit the splash background
		getSplash().setBackgroundMode(SWT.INHERIT_DEFAULT);
	}
}