package edu.missouri.eldercare.application.views;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TimerTask;

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
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.part.ViewPart;

import edu.missouri.eldercare.application.utilities.DataUpdateListener;

/**
 * The application's main frame.
 */
public class ElderCareView extends ViewPart implements DataUpdateListener {
	public ElderCareView() {
	}

	public static final String VIEW_ID = "edu.missouri.eldercare.application.views.ElderCareView";
	char[] row1 = new char[] { 'A', 'B', 'C', 'D' };
	char[] row2 = new char[] { '1', '2', '3', '4', '5', '6', '7', '8' };
	private Label title;
	private int carpetColumns, carpetRows, columns, verticals, horizontals, gridSize;
	private Composite areaA;
	private Composite areaB;
	private Composite areaC;
	private Composite areaD;
	private Composite cupboardArea;
	private Label[] allLabels = new Label[128];
	private Listener listener = new Listener() {

		public void handleEvent(Event event) {
		}
	};

	/**
	 * This method initiates all the components on the parent composite.
	 * 
	 * @param parent
	 */
	private void initComponents(Composite parent) {
		initVariables();
		GridLayout parentLayout = new GridLayout(12, true);
		parentLayout.horizontalSpacing = 0;
		parentLayout.verticalSpacing = 0;
		parentLayout.marginBottom = 0;
		parentLayout.marginTop = 0;
		parentLayout.marginLeft = 0;
		parentLayout.marginRight = 0;
		parent.setLayout(parentLayout);

		ArrayList allComposites = new ArrayList();
		title = new Label(parent, SWT.NULL);

		/*
		 * title.setFont(SWTResourceManager.getFont("Arial Black", 18, SWT.BOLD
		 * | SWT.CENTER));
		 */
		title.setFont(new Font(null, new FontData("Arial Black", 18, SWT.BOLD
				| SWT.CENTER)));
		/*
		 * title.setBackground(new Color(null, 236, 233, 216));
		 */
		title.setText("Elder Care Technology");
		title.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true,
				12, 1));

		areaA = new Composite(parent, SWT.BORDER_SOLID);
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

		areaB = new Composite(parent, SWT.BORDER_SOLID);
		areaB.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 4, 8));
		areaB.setBackground(new Color(null, 204, 204, 255));
		areaB.setLayout(areaA.getLayout());
		areaB.setData("name", "B");
		allComposites.add(areaB);

		areaC = new Composite(parent, SWT.BORDER_SOLID);
		areaC.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 4, 8));
		areaC.setBackground(new Color(null, 204, 255, 204));
		areaC.setLayout(areaA.getLayout());
		areaC.setData("name", "C");
		allComposites.add(areaC);

		areaD = new Composite(parent, SWT.BORDER_SOLID);
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

		cupboardArea = new Composite(parent, SWT.BORDER_SOLID);
		cupboardArea.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
				4, 4));

		/*
		 * cupboardArea
		 * .setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		 */
		cupboardArea.setBackground(new Color(null, 255, 255, 255));
		Label cupboardLabel = new Label(cupboardArea, SWT.NULL);
		cupboardLabel.setText("Space for Cupboard");
		/*
		 * cupboardLabel.setFont(SWTResourceManager.getFont("Arial Black", 18,
		 * SWT.BOLD));
		 */
		cupboardLabel.setFont(new Font(null, "Arial Black", 18, SWT.BOLD));
		int compositeCount = 0;
		for (Iterator iterator = allComposites.iterator(); iterator.hasNext();) {
			Composite composite = (Composite) iterator.next();
			for (int i = 0; i < row2.length; i++) {
				for (int j = 0; j < row1.length; j++) {
					Canvas canvas = new Canvas(composite, SWT.BORDER);
					canvas.setBackground(composite.getBackground());
					canvas.setData("greenBack", composite.getBackground());
					canvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
							true, 1, 1));
					canvas.setLayout(new GridLayout(1, false));

					Label label = new Label(canvas, SWT.NULL);
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
					Menu menu = new Menu(label.getShell(), SWT.POP_UP);
					MenuItem item = new MenuItem(menu, SWT.PUSH);
					item.setText("Show Step Count");
					Image paw = new Image(Display.getDefault(), this.getClass()
							.getResourceAsStream("missouri-paw.jpg"));
					item.setImage(paw);
					item.setData("area", (String) composite.getData("name"));
					item.setData("label", label.getText());
					label.setMenu(menu);
					item.addListener(SWT.Selection, listener);
					allLabels[32 * compositeCount + 8 * (j) + i] = label;
				}
			}
			compositeCount++;
		}

		for (int i = row1.length - 1; i >= 0; i--) {
			for (int j = 0; j < row2.length; j++) {
				Canvas canvas = new Canvas(areaD, SWT.BORDER);
				canvas.setBackground(areaD.getBackground());
				canvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
						true, 1, 1));
				canvas.setLayout(new GridLayout(1, false));
				canvas.setData("greenBack", areaD.getBackground());

				Label label = new Label(canvas, SWT.NULL);
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
				Menu menu = new Menu(label.getShell(), SWT.POP_UP);
				MenuItem item = new MenuItem(menu, SWT.PUSH);
				item.setText("Show Step Count");
				Image paw = new Image(Display.getDefault(), this.getClass()
						.getResourceAsStream("missouri-paw.jpg"));
				item.setImage(paw);
				item.setData("area", (String) areaD.getData("name"));
				item.setData("label", label.getText());
				label.setMenu(menu);
				item.addListener(SWT.Selection, listener);
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
		// updateLabel();
	}

	private void initVariables() {
		carpetColumns = 4;
		carpetRows = 8;
		verticals = 2; 
		horizontals = 1;
		columns = 2;
		gridSize = carpetColumns*verticals;
	}

	/*
	 * private void updateLabel() { String userDir =
	 * CommonUtilities.getInstallationDirectory(); File file = new File(userDir
	 * + "/temp/temporary.csv"); CSVReader reader = null; int[] sum = new
	 * int[128]; int[][] matrix = new int[12][12];
	 * 
	 * try { reader = new CSVReader(new FileReader(file)); List list =
	 * reader.readAll(); for (Iterator iterator = list.iterator(); iterator
	 * .hasNext();) { String[] strings = (String[]) iterator.next(); for (int i
	 * = 0; i < strings.length - 1; i++) { String[] split =
	 * strings[i].split("\\("); String value = split[0]; String coord =
	 * split[1].substring(0,split[1].length()-1); String[] coordinates =
	 * coord.split(","); int row = Integer.parseInt(coordinates[0]); int column
	 * = Integer.parseInt(coordinates[1]); sum[row*8+column] = sum[row*8+column]
	 * + Integer.parseInt(value); } }
	 * 
	 * } catch (FileNotFoundException e) { e.printStackTrace(); } catch
	 * (IOException e) { e.printStackTrace(); } drawLabels(sum); }
	 */

	/*
	 * public void updateLabel() { String userDir =
	 * CommonUtilities.getInstallationDirectory(); File file = new File(userDir
	 * + "/temp/temporary.csv"); CSVReader reader = null; int[] sum = new
	 * int[128];
	 * 
	 * try { reader = new CSVReader(new FileReader(file)); List<String[]> list =
	 * reader.readAll(); for (Iterator<String[]> iterator = list.iterator();
	 * iterator .hasNext();) { String[] strings = (String[]) iterator.next();
	 * for (int i = 0; i < strings.length - 1; i++) { sum[i] = sum[i] +
	 * Integer.parseInt(strings[i]); } }
	 * 
	 * } catch (FileNotFoundException e) { e.printStackTrace(); } catch
	 * (IOException e) { e.printStackTrace(); } drawLabels(sum); }
	 */
	/*
	 * private void drawLabels(int[] array) { for (int i = 0; i < array.length;
	 * i++) { allLabels[i].setText("" + array[i]); } }
	 */
	private ArrayList alphabets;

	// @Override
	public void createPartControl(Composite parent) {
		alphabets = new ArrayList();
		alphabets.add(row1);
		alphabets.add(row2);

		parent.setSize(1024, 768);
		initComponents(parent);
		int msgTimeout = 5000;
		// Timer timer = new Timer();
		// timer.schedule(new RefreshStatus(), msgTimeout);
	}

	// @Override
	public void setFocus() {

	}

	public void setStatusLine(String message) {
		IActionBars bars = getViewSite().getActionBars();
		bars.getStatusLineManager().setMessage(message);
	}

	class RefreshStatus extends TimerTask {
		public void run() {
			setStatusLine("");
		}
	}

	public Label[] getChildren() {
		return allLabels;
	}

	public void redraw() {
		areaA.redraw();
		areaB.redraw();
		areaC.redraw();
		areaD.redraw();
		areaA.layout();
		areaA.layout();
		areaC.layout();
		areaD.layout();
		areaA.update();
		areaB.update();
		areaC.update();
		areaD.update();
	}

	public void refreshBlocks(final int[] sum) {
		Display.getDefault().syncExec(new Runnable() {

			//@Override
			public void run() {

				Label[] labels = getChildren();
				for (int i = 0; i < sum.length; i++) {
					if (sum[i] == 1) {
						labels[i].setBackground(new Color(null, 237, 28, 36));
						labels[i].redraw();
						Composite parent = labels[i].getParent();
						if (parent instanceof Canvas) {
							Canvas canvas = (Canvas) parent;
							canvas.setBackground(new Color(null, 237, 28, 36));
							// int count = Integer.parseInt(labels[i].getText())
							// + 1;
							// labels[i].setText("" + i);
							// canvas.redraw();
							// canvas.layout(true);
							// canvas.getParent().layout(true);
						}
					} else {
						Color backGroundColor = (Color) labels[i]
								.getData("greenBack");
						labels[i].setBackground(backGroundColor);
						labels[i].redraw();
						Composite parent = labels[i].getParent();
						if (parent instanceof Canvas) {
							Canvas canvas = (Canvas) parent;
							canvas.setBackground(backGroundColor);
							// canvas.layout(true);
							// canvas.getParent().layout(true);
						}
					}
				}
				redraw();

			}
		});
		/*
		 * try { Thread.sleep(1000); } catch (InterruptedException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 */
	}

	//@Override
	public void dataUpdated(int[] finalData) {
		refreshBlocks(finalData);		
	}
}
