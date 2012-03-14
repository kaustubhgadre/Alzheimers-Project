package edu.missouri.eldercare.application.ui;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.swtchart.Chart;
import org.swtchart.IBarSeries;
import org.swtchart.ISeries.SeriesType;

import edu.missouri.eldercare.application.utilities.CSVReader;

public class HistogramDialog extends Dialog {

	protected Object result;
	protected Shell shlStatistics;
	char[] row1 = new char[] { 'A', 'B', 'C', 'D' };
	char[] row2 = new char[] { '1', '2', '3', '4', '5', '6', '7', '8' };
	String[] categories;
	private String path;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public HistogramDialog(Shell parent, String path, int style) {
		super(parent, style);
		this.path = path;
		setText("SWT Dialog");
	}

	/**
	 * Open the dialog.
	 * 
	 * @return the result
	 * @throws IOException
	 */
	public Object open() {
		try {
			createContents();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		shlStatistics.setMaximized(true);
		shlStatistics.open();
		shlStatistics.layout();
		Display display = getParent().getDisplay();
		while (!shlStatistics.isDisposed()) {
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
	 * @throws IOException
	 */
	private void createContents() throws IOException {
		CSVReader csvR;

		csvR = new CSVReader(new FileReader(path));
		List list = csvR.readAll();
		final double[] totalSum = new double[128];
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			String[] strings = (String[]) iterator.next();
			for (int i = 0; i < strings.length - 1; i++) {
				String[] split = strings[i].split("\\(");
				String value = split[0];
				String coord = split[1].substring(0, split[1].length() - 1);
				String[] coordinates = coord.split(",");
				int row = Integer.parseInt(coordinates[0]);
				int column = Integer.parseInt(coordinates[1]);
				totalSum[row * 8 + column] = totalSum[row * 8 + column]
						+ Double.parseDouble(value);
			}
		}
		csvR.close();

		shlStatistics = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.MAX
				| SWT.APPLICATION_MODAL);
		shlStatistics.setSize(450, 300);
		shlStatistics.setText("Statistics");
		shlStatistics.setLayout(new GridLayout(2, false));

		CLabel lblPleaseSelectThe = new CLabel(shlStatistics, SWT.NONE);
		lblPleaseSelectThe.setText("Please Select the area of the carpet");

		CCombo combo = new CCombo(shlStatistics, SWT.BORDER);
		combo.setEditable(false);
		combo.setVisibleItemCount(4);
		combo.setBackground(new Color(null, 255, 255, 255));
		combo.setListVisible(true);
		combo.setItems(new String[] { "A", "B", "C", "D" });
		combo.select(0);
		categories = new String[32];
		final double[] sum = new double[32];
		int k = 0;
		for (int i = 0; i < row1.length; i++) {
			for (int j = 0; j < row2.length; j++) {
				categories[k++] = "" + row1[i] + row2[j];
			}
		}

		Chart chart = new Chart(shlStatistics, SWT.BORDER);
		chart.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		chart.getAxisSet().getXAxis(0).getTitle().setText("Carpet Blocks");
		chart.getAxisSet().getYAxis(0).getTitle().setText("Step Count");
		chart.getAxisSet().getXAxis(0).enableCategory(true);
		chart.getAxisSet().getXAxis(0).setCategorySeries(categories);

		for (int i = combo.getSelectionIndex() * 32; i < combo
				.getSelectionIndex() * 32 + 32; i++) {
			sum[i - combo.getSelectionIndex() * 32] = sum[i
					- combo.getSelectionIndex() * 32]
					+ totalSum[i];
		}

		final IBarSeries barSeries = (IBarSeries) chart.getSeriesSet()
				.createSeries(SeriesType.BAR, "Step counts");
		barSeries.setYSeries(sum);
		barSeries.setBarColor(new Color(Display.getDefault(), 237, 28, 36));
		chart.getAxisSet().adjustRange();
		chart.getTitle().setText("Carpet Blocks Vs Step Count");
		combo.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				CCombo ccombo = (CCombo) e.getSource();
				Arrays.fill(sum, 0d);
				for (int i = ccombo.getSelectionIndex() * 32; i < ccombo
						.getSelectionIndex() * 32 + 32; i++) {
					sum[i - ccombo.getSelectionIndex() * 32] = sum[i
							- ccombo.getSelectionIndex() * 32]
							+ totalSum[i];
				}
				barSeries.setYSeries(sum);
				shlStatistics.redraw();
				shlStatistics.layout();
				shlStatistics.update();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
	}
}
