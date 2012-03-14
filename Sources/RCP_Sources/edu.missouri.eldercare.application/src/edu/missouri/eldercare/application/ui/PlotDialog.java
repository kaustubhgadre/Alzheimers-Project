package edu.missouri.eldercare.application.ui;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.Dialog.ModalExclusionType;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.swing.JRootPane;
import javax.swing.UIManager;

import net.ericaro.surfaceplotter.AbstractSurfaceModel;
import net.ericaro.surfaceplotter.AbstractSurfaceModel.Plotter;
import net.ericaro.surfaceplotter.surface.JSurface;
import net.ericaro.surfaceplotter.surface.SurfaceModel.PlotColor;
import net.ericaro.surfaceplotter.surface.SurfaceModel.PlotType;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import edu.missouri.eldercare.application.utilities.CSVReader;

public class PlotDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	private String path;
	private int[] totalSum;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public PlotDialog(Shell parent, String path, int style) {
		super(parent, style);
		this.path = path;
		setText("Histogram");
	}

	/**
	 * Open the dialog.
	 * 
	 * @return the result
	 */
	public Object open() {
		try {
			createContents();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @throws IOException
	 */
	private void createContents() throws IOException {
		final AbstractSurfaceModel sm = new AbstractSurfaceModel();

		CSVReader csvR;
		csvR = new CSVReader(new FileReader(path));
		List list = csvR.readAll();
		totalSum = new int[128];
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			String[] strings = (String[]) iterator.next();
			for (int i = 0; i < strings.length - 1; i++) {
				String[] split = strings[i].split("\\(");
				String value = split[0];
				String coord = split[1].substring(0, split[1].length() - 1);
				String[] coordinates = coord.split(",");
				int row = Integer.parseInt(coordinates[0]);
				int column = Integer.parseInt(coordinates[1]);
				if(row == 1 && column == 7){
					
				}else{
					totalSum[row * 8 + column] = totalSum[row * 8 + column]
					              						+ Integer.parseInt(value);	
				}				
			}
		}
		csvR.close();

		sm.setPlotFunction2(true);

		sm.setCalcDivisions(12);
		sm.setDispDivisions(12);
		sm.setContourLines(12);

		sm.setXMin(0);
		sm.setXMax(12);
		sm.setYMin(0);
		sm.setYMax(12);

		sm.setBoxed(true);
		sm.setDisplayXY(true);
		// sm.setExpectDelay(true);
		sm.setAutoScaleZ(true);
		sm.setDisplayZ(true);
		sm.setMesh(true);
		sm.autoScale();
		sm.setPlotType(PlotType.WIREFRAME);

		sm.setPlotColor(PlotColor.SPECTRUM);

		JSurface canvas = new JSurface(sm);
		canvas.setYLabel("Columns");
		canvas.setXLabel("Rows");
		canvas.setBorder(UIManager.getBorder("TableHeader.cellBorder"));

		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.MAX
				| SWT.APPLICATION_MODAL);
		shell.setSize(573, 440);
		shell.setText(getText());
		shell.setLayout(new FillLayout(SWT.HORIZONTAL));

		Composite composite = new Composite(shell, SWT.EMBEDDED);

		Frame frame = SWT_AWT.new_Frame(composite);
		frame.setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);

		Panel panel = new Panel();
		frame.add(panel);
		panel.setLayout(new BorderLayout(0, 0));

		JRootPane rootPane = new JRootPane();
		panel.add(rootPane);

		new Thread(new Runnable() {
			public void run() {
				Plotter p = sm.newPlotter(sm.getCalcDivisions());
				int im = p.getWidth();
				int jm = p.getHeight();
				for (int i = 0; i < im; i++)
					for (int j = 0; j < jm; j++) {
						p.setValue(i, j, totalSum[i * 8 + j], 0);						
					}
			}
		}).start();

		rootPane.getContentPane().add(canvas);
	}
}
