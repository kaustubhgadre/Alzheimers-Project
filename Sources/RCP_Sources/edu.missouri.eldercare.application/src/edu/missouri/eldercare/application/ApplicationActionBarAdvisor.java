package edu.missouri.eldercare.application;

import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ContributionItemFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

import edu.missouri.eldercare.application.actions.RunAction;
import edu.missouri.eldercare.application.actions.ShowHistogramAction;
import edu.missouri.eldercare.application.actions.StopAction;

public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

	private RunAction runProgramAction;
	private ShowHistogramAction showHistogramAction;
	private ShowHistoryAction showHistoryAction;
	private IWorkbenchAction introAction, exitAction, helpContentsAction,
			aboutAction, resetPerspectiveAction;
	private IContributionItem viewList, perspectiveList;
	private StopAction stopAction;

	public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
		super(configurer);
	}

	protected void makeActions(IWorkbenchWindow window) {
		exitAction = ActionFactory.QUIT.create(window);
		register(exitAction);

		runProgramAction = new RunAction();
		runProgramAction.setText("Start");
		runProgramAction.setAccelerator(SWT.CTRL | 'R');
		register(runProgramAction);

		stopAction = new StopAction();
		stopAction.setText("Stop");
		register(stopAction);

		/*
		 * showStatisticsAction = new ShowStatisticsAction();
		 * showStatisticsAction.setText("Show Statistics");
		 * register(showStatisticsAction);
		 */

		/*
		 * showHistogramAction = new ShowHistogramAction();
		 * showHistogramAction.setAccelerator(SWT.CTRL | 'H');
		 * showHistogramAction.setText("Show Histogram");
		 * register(showHistogramAction);
		 * 
		 * showHistoryAction = new ShowHistoryAction();
		 * showHistoryAction.setText("Show History");
		 * register(showHistoryAction);
		 */

		perspectiveList = ContributionItemFactory.PERSPECTIVES_SHORTLIST
				.create(window);
		viewList = ContributionItemFactory.VIEWS_SHORTLIST.create(window);
		resetPerspectiveAction = ActionFactory.RESET_PERSPECTIVE.create(window);
		register(resetPerspectiveAction);

		helpContentsAction = ActionFactory.HELP_CONTENTS.create(window);
		register(helpContentsAction);

		aboutAction = ActionFactory.ABOUT.create(window);
		register(aboutAction);

		/*
		 * introAction = ActionFactory.INTRO.create(window);
		 * register(introAction);
		 */
	}

	protected void fillCoolBar(ICoolBarManager coolBar) {
		// TODO Auto-generated method stub
		super.fillCoolBar(coolBar);
		IToolBarManager toolbar = new ToolBarManager(SWT.FLAT | SWT.RIGHT);
		coolBar.add(new ToolBarContributionItem(toolbar, "main"));
		toolbar.add(runProgramAction);
		toolbar.add(stopAction);
	}

	protected void fillMenuBar(IMenuManager menuBar) {
		MenuManager fileMenu = new MenuManager(
				"&File", IWorkbenchActionConstants.M_FILE); //$NON-NLS-1$
		MenuManager helpMenu = new MenuManager(
				"&Help", IWorkbenchActionConstants.M_HELP); //$NON-NLS-1$
		MenuManager windowMenu = new MenuManager("&Window",
				IWorkbenchActionConstants.M_WINDOW);
		MenuManager perspectiveMenu = new MenuManager("&Open Perspective",
				"openPerspective");
		MenuManager showViewMenu = new MenuManager("&Show View", "showView");

		menuBar.add(fileMenu);
		menuBar.add(windowMenu);
		menuBar.add(helpMenu);

		GroupMarker fileStart = new GroupMarker("fileStart");
		fileMenu.add(fileStart);
		fileMenu.add(runProgramAction);
		fileMenu.add(stopAction);
		/*
		 * fileMenu.add(showStatisticsAction);
		 * fileMenu.add(showHistogramAction); fileMenu.add(showHistoryAction);
		 */
		Separator exitStart = new Separator();
		fileMenu.add(exitStart);
		fileMenu.add(exitAction);

		perspectiveMenu.add(perspectiveList);
		showViewMenu.add(viewList);

		windowMenu.add(resetPerspectiveAction);
		windowMenu.add(perspectiveMenu);
		windowMenu.add(showViewMenu);

		// helpMenu.add(introAction);
		helpMenu.add(aboutAction);
	}
}
