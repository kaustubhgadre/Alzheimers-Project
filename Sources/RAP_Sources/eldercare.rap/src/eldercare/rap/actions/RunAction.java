/**
 * 
 */
package eldercare.rap.actions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.rwt.lifecycle.UICallBack;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import au.com.bytecode.opencsv.CSVReader;
import eldercare.rap.View;
import eldercare.rap.interfaces.FileListener;
import eldercare.rap.utilities.FileMonitor;

/**
 * @author Kaustubh
 * 
 */
public class RunAction extends Action implements IWorkbenchAction, FileListener {

	private static final String ID = "eldercare.rap.actions.RunAction";
	private IViewPart elderCareView;
	private static final Logger logger = Logger.getLogger(RunAction.class
			.getName());
	// File file = new File("/var/lib/tomcat6/temporary.csv");
	File file = new File("C:/temp/temporary.csv");

	/**
	 * 
	 */
	public RunAction() {
		setId(ID);
		// new Thread(this).start();
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
		logger.log(Level.INFO, "Inside RUN before file monitor");
		FileMonitor monitor = new FileMonitor(1000);

		// Add some files to listen for
		// monitor.addFile(new File("/carpet/data/currentInstance.csv"));
		monitor.addFile(new File("C:/temp/currentInstance.csv"));
		// monitor.addFile(file);
		logger.log(Level.INFO, "Added CSV file for listening");

		// Add a dummy listener
		monitor.addListener(this);

		elderCareView = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getActivePage().findView(View.ID);
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

	private void showData(final int[] finalData) throws IOException {
		UICallBack.runNonUIThreadWithFakeContext(PlatformUI.createDisplay(), new Runnable() {

			@Override
			public void run() {
				if (elderCareView instanceof View) {
					Label[] labels = ((View) elderCareView).getChildren();
					for (int i = 0; i < finalData.length; i++) {
						if (finalData[i] == 1) {
							labels[i]
									.setBackground(new Color(null, 237, 28, 36));
							Composite parent = labels[i].getParent();
							if (parent instanceof Canvas) {
								Canvas canvas = (Canvas) parent;
								canvas.setBackground(new Color(null, 237, 28,
										36));
								int count = Integer.parseInt(labels[i]
										.getText()) + 1;
								labels[i].setText("" + count);

							}
						} else {
							Color backGroundColor = (Color) labels[i]
									.getData("greenBack");
							labels[i].setBackground(backGroundColor);
							Composite parent = labels[i].getParent();
							if (parent instanceof Canvas) {
								Canvas canvas = (Canvas) parent;
								canvas.setBackground(backGroundColor);
							}
						}
					}
					((View) elderCareView).redraw();
				}

			}
		});
	}

	@Override
	public void fileChanged(File file) throws InterruptedException,
			FileNotFoundException, IOException {
		synchronized (file) {
			CSVReader reader = new CSVReader(new FileReader(file));
			try {
				// ra.seek(file.length() - 138);
				int[] sum = new int[128];
				String[] lastLine = reader.readNext();
				if (lastLine != null && !lastLine.equals("")) {
					for (int i = 0; i < lastLine.length - 1; i++) {
						String[] split = lastLine[i].split("\\(");
						String value = split[0];
						String coord = split[1].substring(0,
								split[1].length() - 1);
						String[] coordinates = coord.split(",");
						int row = Integer.parseInt(coordinates[0]);
						int column = Integer.parseInt(coordinates[1]);
						// logger.info("Value : " + value + " Row : " + row +
						// " Column : "+column);
						sum[row * 8 + column] = sum[row * 8 + column]
								+ Integer.parseInt(value);
						// logger.log(Level.INFO, value + " at "
						// +coordinates[0]+","+coordinates[1]);
					}
					// drawLabels(sum);
					showData(sum);
					// Thread.sleep(10);
				}
				reader.close();
				// file.delete();
			} catch (Exception e) {
				logger.log(Level.SEVERE, e.getMessage());
			}
		}

	}
}
