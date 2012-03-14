package edu.missouri.eldercare.application.ui;

import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.swtchart.Chart;
import org.swtchart.ILineSeries;
import org.swtchart.IAxis.Position;
import org.swtchart.ISeries.SeriesType;

import edu.missouri.eldercare.application.utilities.CSVReader;

public class PathDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	private String path;
	private double[] xSeries, ySeries;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public PathDialog(Shell parent, String path, int style) {
		super(parent, style);
		this.path = path;
		setText("Track Path");
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
		CSVReader csvR;
		csvR = new CSVReader(new FileReader(path));
		List list = csvR.readAll();
		xSeries = new double[300];
		ySeries = new double[300];

		int index = 0;
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			String[] strings = (String[]) iterator.next();
			for (int i = 0; i < strings.length - 1; i++) {
				String[] split = strings[i].split("\\(");
				String value = split[0];
				String coord = split[1].substring(0, split[1].length() - 1);
				String[] coordinates = coord.split(",");
				int row = Integer.parseInt(coordinates[0]);
				int column = Integer.parseInt(coordinates[1]);
				/*
				 * totalSum[row * 8 + column] = totalSum[row * 8 + column] +
				 * Double.parseDouble(value);
				 */
				ySeries[index] = column;
				xSeries[index] = row;
				index++;
			}
		}
		csvR.close();

		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.MAX
				| SWT.APPLICATION_MODAL);
		shell.setSize(573, 440);
		shell.setText(getText());
		shell.setLayout(new FillLayout(SWT.HORIZONTAL));

		// double[] numSeries = { 1, 2, 2, 4, 5, 6, 6, 8, 9, 10, 11, 12 };

		Chart chart = new Chart(shell, SWT.BORDER);
		chart.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		chart.getAxisSet().getXAxis(0).getTitle().setText("Columns");
		chart.getAxisSet().getYAxis(0).getTitle().setText("Rows");
		chart.getAxisSet().getXAxis(0).enableCategory(true);
		chart.getAxisSet().getXAxis(0).setPosition(Position.Secondary);

		final ILineSeries barSeries = (ILineSeries) chart.getSeriesSet()
				.createSeries(SeriesType.LINE, "Track");
		barSeries.setLineColor(new Color(Display.getDefault(), 237, 28, 36));
		barSeries.setYSeries(ySeries);
		barSeries.setXSeries(xSeries);
		chart.getAxisSet().adjustRange();
		chart.getTitle().setText("Carpet path track");
	}
}
