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

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.math.plot.Plot3DPanel;

import edu.missouri.eldercare.application.utilities.CSVReader;

public class Histogram3DDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	private String path;
	private double[] totalSum;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public Histogram3DDialog(Shell parent, String path, int style) {
		super(parent, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.APPLICATION_MODAL);
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
		double[] numSeries = { 1, 2, 2, 4, 5, 6, 6, 8, 9, 10, 11, 12 };

		CSVReader csvR;
		csvR = new CSVReader(new FileReader(path));
		List list = csvR.readAll();
		totalSum = new double[128];
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			String[] strings = (String[]) iterator.next();
			for (int i = 0; i < strings.length - 1; i++) {
				String[] split = strings[i].split("\\(");
				String value = split[0];
				String coord = split[1].substring(0, split[1].length() - 1);
				String[] coordinates = coord.split(",");
				int row = Integer.parseInt(coordinates[0]);
				int column = Integer.parseInt(coordinates[1]);
				if (row == 1 && column == 7) {

				} else {
					totalSum[row * 8 + column] = totalSum[row * 8 + column]
							+ Double.parseDouble(value);
				}
			}
		}
		csvR.close();
		double[][] sumMatrix = transformToMatrix(totalSum);

		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.MAX
				| SWT.APPLICATION_MODAL);
		shell.setSize(573, 440);
		shell.setText(getText());
		shell.setLayout(new FillLayout(SWT.HORIZONTAL));

		Plot3DPanel plot = new Plot3DPanel();
		plot.addHistogramPlot("3D room", sumMatrix, numSeries, numSeries);

		Composite composite = new Composite(shell, SWT.EMBEDDED);

		Frame frame = SWT_AWT.new_Frame(composite);
		frame.setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);

		Panel panel = new Panel();
		frame.add(panel);
		panel.setLayout(new BorderLayout(0, 0));

		JRootPane rootPane = new JRootPane();
		panel.add(rootPane);

		rootPane.getContentPane().add(plot);
	}

	private double[][] transformToMatrix(double[] finalData) {
		double[][] matrix = new double[12][12];
		double[][] sumMatrix = new double[12][12];
		int i = 0;
		for (int c = 0; c < matrix[0].length; c++) {
			for (int r = 0; r < matrix.length - 4; r++) {
				matrix[r][c] = finalData[i++];
				sumMatrix[r][c] = sumMatrix[r][c] + matrix[r][c];
			}
		}

		for (int r = matrix.length - 1; r >= matrix.length - 4; r--) {
			for (int c = 0; c < matrix[r].length - 4; c++) {
				matrix[r][c] = finalData[i++];
				sumMatrix[r][c] = sumMatrix[r][c] + matrix[r][c];
			}
		}
		/*
		 * System.out.println("------------------New Frame------------------");
		 * for (int r = 0; r < sumMatrix.length; r++) { String row = ""; for
		 * (int c = 0; c < sumMatrix[r].length; c++) { row = row + "  " +
		 * sumMatrix[r][c]; } System.out.println(row); }
		 */
		return sumMatrix;
	}

}
