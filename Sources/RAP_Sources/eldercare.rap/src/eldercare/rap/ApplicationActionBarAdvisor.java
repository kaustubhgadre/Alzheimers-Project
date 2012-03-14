package eldercare.rap;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

import eldercare.rap.actions.RunAction;
import eldercare.rap.actions.ShowHistogramAction;

/**
 * Creates, adds and disposes actions for the menus and action bars of each
 * workbench window.
 */
public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

	public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
		super(configurer);
	}

	// Actions - important to allocate these only in makeActions, and then use
	// them in the fill methods. This ensures that the actions aren't recreated
	// in the fill methods.
	private IWorkbenchAction exitAction;
	private RunAction runProgramAction;
	private ShowHistogramAction showHistogramAction;

	protected void makeActions(IWorkbenchWindow window) {
		// Creates the actions and registers them. Registering also
		// provides automatic disposal of the actions when the window is closed.
		exitAction = ActionFactory.QUIT.create(window);
		register(exitAction);

		runProgramAction = new RunAction();
		runProgramAction.setText("Run");
		runProgramAction.setAccelerator(SWT.CTRL | 'R');
		register(runProgramAction);

		showHistogramAction = new ShowHistogramAction();
		showHistogramAction.setAccelerator(SWT.CTRL | 'H');
		showHistogramAction.setText("Show Histogram");
		register(showHistogramAction);
	}

	protected void fillMenuBar(IMenuManager menuBar) {
		MenuManager fileMenu = new MenuManager("&File",
				IWorkbenchActionConstants.M_FILE);
		menuBar.add(fileMenu);
		fileMenu.add(runProgramAction);
		fileMenu.add(showHistogramAction);
		fileMenu.add(exitAction);
	}

}
