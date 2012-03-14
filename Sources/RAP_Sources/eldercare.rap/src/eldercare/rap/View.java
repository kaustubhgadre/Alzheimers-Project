package eldercare.rap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.rwt.lifecycle.UICallBack;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.part.ViewPart;

import eldercare.data.collection.DataUpdateListener;
import eldercare.rap.interfaces.FileListener;
import eldercare.rap.utilities.CSVReader;
import eldercare.rap.utilities.FileMonitor;

public class View extends ViewPart implements FileListener, DataUpdateListener {
	public View() {
	}

	public static final String ID = "eldercare.rap.view";

	char[] row1 = new char[] { 'A', 'B', 'C', 'D' };
	char[] row2 = new char[] { '1', '2', '3', '4', '5', '6', '7', '8' };
	private Label title;
	private Composite areaA;
	private Composite areaB;
	private Composite areaC;
	private Composite areaD;
	private Composite cupboardArea;
	private Label[] allLabels = new Label[128];
	private ArrayList<char[]> alphabets;
	private static final Logger logger = Logger.getLogger(View.class.getName());
	private Display display;

	File file = new File("/carpet/data/temporary.csv");

	// File file = new File("C:\\temp\\temporary.csv");
	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {
		alphabets = new ArrayList<char[]>();
		alphabets.add(row1);
		alphabets.add(row2);
		initComponents(parent);
	}

	/**
	 * This method initiates all the components on the parent composite.
	 * 
	 * @param parent
	 */
	private void initComponents(Composite parent) {
		GridLayout parentLayout = new GridLayout(12, true);
		parentLayout.horizontalSpacing = 0;
		parentLayout.verticalSpacing = 0;
		parentLayout.marginBottom = 0;
		parentLayout.marginTop = 0;
		parentLayout.marginLeft = 0;
		parentLayout.marginRight = 0;
		parent.setLayout(parentLayout);
		this.display = Display.getCurrent();
		ArrayList<Composite> allComposites = new ArrayList<Composite>();
		title = new Label(parent, SWT.NULL | SWT.VIRTUAL);

		/*
		 * title.setFont(SWTResourceManager.getFont("Arial Black", 18, SWT.BOLD
		 * | SWT.CENTER));
		 */
		title.setFont(new Font(null, new FontData("Arial Black", 18, SWT.BOLD
				| SWT.CENTER)));
		/*
		 * title.setBackground(new Color(null, 236, 233, 216));
		 */
		title.setText("Smart Carpet Display");
		title.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true,
				12, 1));

		areaA = new Composite(parent, SWT.BORDER | SWT.VIRTUAL);
		areaA.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 4, 8));
		areaA.setBackground(new Color(null, 204, 204, 204));
		areaA.setData("name", "A");
		GridLayout aLayout = new GridLayout(4, true);
		aLayout.horizontalSpacing = 0;
		aLayout.verticalSpacing = 0;
		aLayout.marginBottom = 0;
		aLayout.marginTop = 0;
		aLayout.marginLeft = 0;
		aLayout.marginRight = 0;
		aLayout.marginHeight = 0;
		aLayout.marginWidth = 0;
		areaA.setLayout(aLayout);
		allComposites.add(areaA);

		areaB = new Composite(parent, SWT.BORDER | SWT.VIRTUAL);
		areaB.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 4, 8));
		areaB.setBackground(new Color(null, 204, 204, 255));
		areaB.setLayout(areaA.getLayout());
		areaB.setData("name", "B");
		allComposites.add(areaB);

		areaC = new Composite(parent, SWT.BORDER | SWT.VIRTUAL);
		areaC.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 4, 8));
		areaC.setBackground(new Color(null, 204, 255, 204));
		areaC.setLayout(areaA.getLayout());
		areaC.setData("name", "C");
		allComposites.add(areaC);

		areaD = new Composite(parent, SWT.BORDER | SWT.VIRTUAL);
		areaD.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 8, 4));
		areaD.setBackground(new Color(null, 255, 204, 204));
		areaD.setData("name", "D");
		GridLayout dLayout = new GridLayout(8, true);
		dLayout.horizontalSpacing = 0;
		dLayout.verticalSpacing = 0;
		dLayout.marginBottom = 0;
		dLayout.marginTop = 0;
		dLayout.marginLeft = 0;
		dLayout.marginRight = 0;
		dLayout.marginHeight = 0;
		dLayout.marginWidth = 0;
		areaD.setLayout(dLayout);

		cupboardArea = new Composite(parent, SWT.BORDER | SWT.VIRTUAL);
		cupboardArea.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
				4, 4));

		/*
		 * cupboardArea
		 * .setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		 */
		cupboardArea.setBackground(new Color(null, 255, 255, 255));
		Label cupboardLabel = new Label(cupboardArea, SWT.NULL | SWT.VIRTUAL);
		cupboardLabel.setText("Space for Cupboard");
		/*
		 * cupboardLabel.setFont(SWTResourceManager.getFont("Arial Black", 18,
		 * SWT.BOLD));
		 */
		cupboardLabel.setFont(new Font(null, "Arial Black", 18, SWT.BOLD));
		int compositeCount = 0;
		for (Iterator<Composite> iterator = allComposites.iterator(); iterator
				.hasNext();) {
			Composite composite = (Composite) iterator.next();
			for (int i = 0; i < row2.length; i++) {
				for (int j = 0; j < row1.length; j++) {
					Canvas canvas = new Canvas(composite, SWT.BORDER
							| SWT.VIRTUAL);
					canvas.setBackground(composite.getBackground());
					canvas.setData("greenBack", composite.getBackground());
					canvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
							true, 1, 1));
					canvas.setLayout(new GridLayout(1, false));

					Label label = new Label(canvas, SWT.NULL | SWT.VIRTUAL);
					label.setBackground(composite.getBackground());
					label.setData("greenBack", composite.getBackground());
					label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
							true, 1, 1));
					label.setAlignment(SWT.CENTER);
					/*
					 * label.setFont(SWTResourceManager.getFont("Arial Black",
					 * 18, SWT.NULL));
					 */
					label.setFont(new Font(null, new FontData("Arial Black",
							18, SWT.NULL)));
					label.setText("" + row1[j] + row2[i]);
					// label.setText("0");
					Menu menu = new Menu(label.getShell(), SWT.POP_UP
							| SWT.VIRTUAL);
					MenuItem item = new MenuItem(menu, SWT.PUSH | SWT.VIRTUAL);
					item.setText("Show Step Count");
					Image paw = new Image(Display.getDefault(), this.getClass()
							.getResourceAsStream("missouri-paw.jpg"));
					item.setImage(paw);
					item.setData("area", (String) composite.getData("name"));
					item.setData("label", label.getText());
					label.setMenu(menu);
					// item.addListener(SWT.Selection, listener);
					allLabels[32 * compositeCount + 8 * (j) + i] = label;
				}
			}
			compositeCount++;
		}

		for (int i = row1.length - 1; i >= 0; i--) {
			for (int j = 0; j < row2.length; j++) {
				Canvas canvas = new Canvas(areaD, SWT.BORDER | SWT.VIRTUAL);
				canvas.setBackground(areaD.getBackground());
				canvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
						true, 1, 1));
				canvas.setLayout(new GridLayout(1, false));
				canvas.setData("greenBack", areaD.getBackground());

				Label label = new Label(canvas, SWT.NULL | SWT.VIRTUAL);
				label.setBackground(areaD.getBackground());
				label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
						true, 1, 1));
				label.setAlignment(SWT.CENTER);
				/*
				 * label.setFont(SWTResourceManager.getFont("Arial Black", 18,
				 * SWT.NULL));
				 */
				label.setFont(new Font(null, new FontData("Arial Black", 18,
						SWT.NULL)));
				label.setText("" + row1[i] + row2[j]);
				// label.setText("0");
				label.setData("greenBack", areaD.getBackground());
				Menu menu = new Menu(label.getShell(), SWT.POP_UP | SWT.VIRTUAL);
				MenuItem item = new MenuItem(menu, SWT.PUSH | SWT.VIRTUAL);
				item.setText("Show Step Count");
				Image paw = new Image(Display.getDefault(), this.getClass()
						.getResourceAsStream("missouri-paw.jpg"));
				item.setImage(paw);
				item.setData("area", (String) areaD.getData("name"));
				item.setData("label", label.getText());
				label.setMenu(menu);
				// item.addListener(SWT.Selection, listener);
				allLabels[96 + i * 8 + j] = label;
			}
		}
		cupboardArea.setLayout(new GridLayout(4, true));
		cupboardArea.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
				4, 1));
		cupboardLabel.setBackground(cupboardArea.getBackground());
		cupboardLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true,
				true, 4, 1));
		// setStatusLine("Application is starting");
		updateLabel(file);

		// boolean added =
		// DataCollectionManager.getInstance().addDataListener(this);
		// logger.info("Eclipse Data Listener added : "+added);
		FileMonitor monitor = new FileMonitor(500);
		monitor.addFile(new File("/carpet/data/currentInstance.csv"));
		// monitor.addFile(new File("C:/temp/currentInstance.csv"));
		monitor.addListener(this);
		System.out.println("Added CSV File For listening");
	}

	private void updateLabel(File file) {
		// String userDir = CommonUtilities.getInstallationDirectory();
		// File file = new File("C://Data4.csv");

		CSVReader reader = null;
		int[] sum = new int[128];
		try {
			logger.log(Level.INFO, "Updating the label initial states");
			reader = new CSVReader(new FileReader(file));
			List<String[]> list = reader.readAll();
			for (Iterator<String[]> iterator = list.iterator(); iterator
					.hasNext();) {
				String[] strings = (String[]) iterator.next();
				for (int i = 0; i < strings.length - 1; i++) {
					String[] split = strings[i].split("\\(");
					String value = split[0];
					String coord = split[1].substring(0, split[1].length() - 1);
					String[] coordinates = coord.split(",");
					int row = Integer.parseInt(coordinates[0]);
					int column = Integer.parseInt(coordinates[1]);
					sum[row * 8 + column] = sum[row * 8 + column]
							+ Integer.parseInt(value);
				}
			}

		} catch (FileNotFoundException e) {
			logger.log(Level.SEVERE, e.getLocalizedMessage());
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.getLocalizedMessage());
		}
		drawLabels(sum);
	}

	private void drawLabels(int[] array) {
		for (int i = 0; i < array.length; i++) {
			allLabels[i].setText("" + array[i]);
		}
	}

	public Label[] getChildren() {
		return allLabels;
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {

	}

	public void redraw() {
		areaA.redraw();
		areaB.redraw();
		areaC.redraw();
		areaD.redraw();
		areaA.layout(true);
		areaB.layout(true);
		areaC.layout(true);
		areaD.layout(true);

		areaA.update();
		areaB.update();
		areaC.update();
		areaD.update();

		display.getActiveShell().layout(true);
		display.getActiveShell().update();
		display.update();
		// getViewSite().getActionBars().getStatusLineManager().update(true);
	}

	public void dataUpdated(String[] csvData) {
		logger.log(Level.INFO, "New DataupdateListener notification");
		int[] sum = new int[128];
		if (csvData != null && !csvData.equals("")) {
			for (int i = 0; i < csvData.length - 1; i++) {
				String[] split = csvData[i].split("\\(");
				String value = split[0];
				String coord = split[1].substring(0, split[1].length() - 1);
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
			paintLables(sum);
			// Thread.sleep(10);
		}
	}

	@Override
	public void fileChanged(File file) throws InterruptedException,
			FileNotFoundException, IOException {
		String[] lastLine = null;
		synchronized (file) {
			CSVReader reader = new CSVReader(new FileReader(file));
			try {
				// ra.seek(file.length() - 138);
		
				lastLine = reader.readNext();
				reader.close();
				// file.delete();
			} catch (Exception e) {
				logger.log(Level.SEVERE, e.getMessage());
			}
		}
		int[] sum = new int[128];
		if (lastLine != null && !lastLine.equals("")) {
			for (int i = 0; i < lastLine.length - 1; i++) {
				String[] split = lastLine[i].split("\\(");
				String value = split[0];
				String coord = split[1].substring(0, split[1].length() - 1);
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
			logger.info("Going to paint labels");
			paintLables(sum);
			// Thread.sleep(10);
		}

	}

	private void paintLables(final int[] sum) {
		final Runnable runnable = new Runnable() {
			public void run() {
				// /logger.info("Inside runnable");
				UICallBack.runNonUIThreadWithFakeContext(display,
						new Runnable() {

							@Override
							public void run() {
								// logger.info("Inside run ui non thread context");
								Label[] labels = getChildren();
								for (int i = 0; i < sum.length; i++) {
									if (sum[i] == 1) {
										labels[i].setBackground(new Color(null,
												237, 28, 36));
										labels[i].redraw();
										Composite parent = labels[i]
												.getParent();
										if (parent instanceof Canvas) {
											Canvas canvas = (Canvas) parent;
											canvas.setBackground(new Color(
													null, 237, 28, 36));
											int count = Integer
													.parseInt(labels[i]
															.getText()) + 1;
											labels[i].setText("" + count);
											canvas.redraw();
											canvas.layout(true);
											canvas.getParent().layout(true);
										}
									} else {
										Color backGroundColor = (Color) labels[i]
												.getData("greenBack");
										labels[i]
												.setBackground(backGroundColor);
										labels[i].redraw();
										Composite parent = labels[i]
												.getParent();
										if (parent instanceof Canvas) {
											Canvas canvas = (Canvas) parent;
											canvas.setBackground(backGroundColor);
											canvas.layout(true);
											canvas.getParent().layout(true);
										}
									}
								}
								// redraw();
							}
						});
			}
		};
		display.asyncExec(runnable);
		// new Thread(runnable).start();
	}

}