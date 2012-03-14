/**
 * 
 */
package eldercare.rap.actions;

import eldercare.rap.View;
import eldercare.rap.utilities.CSVWriter;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TooManyListenersException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IViewPart;


/**
 * @author Kaustubh
 * 
 */
public class SerialDataRead implements Runnable, SerialPortEventListener {

	private CommPortIdentifier portId;
	private SerialPort serialPort;
	private InputStream inputStream;
	private File file;
	private IViewPart elderCareView;
	private String display_data = "";
	private boolean data_ended = false;
	private boolean data_started = false;
	private int[] previousData = new int[128];
	private int[][] sumMatrix = new int[12][12];
	private static final Logger logger = Logger.getLogger(SerialDataRead.class
			.getName());

	public SerialDataRead(IViewPart elderCareView, File file,
			CommPortIdentifier portId) throws PortInUseException, IOException,
			TooManyListenersException, UnsupportedCommOperationException {
		this.portId = portId;
		this.elderCareView = elderCareView;
		this.file = file;
		initParams();
		new Thread(this).start();
		logger.info("In Serial Data Read Constructor");
	}

	/**
	 * @param portId
	 * @throws PortInUseException
	 * @throws IOException
	 * @throws TooManyListenersException
	 * @throws UnsupportedCommOperationException
	 */
	private void initParams() throws PortInUseException, IOException,
			TooManyListenersException, UnsupportedCommOperationException {
		logger.log(Level.INFO, "Init serial port params");
		serialPort = (SerialPort) portId.open("Elder Care", 2000);
		inputStream = serialPort.getInputStream();
		serialPort.addEventListener(this);
		serialPort.notifyOnDataAvailable(true);
		serialPort.setSerialPortParams(19200, SerialPort.DATABITS_8,
				SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
		logger.info("Serial Data Read Params have been initialized");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		while (true) {
			try {
				Thread.sleep(2000);
				// System.out.println("Thread slept");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gnu.io.SerialPortEventListener#serialEvent(gnu.io.SerialPortEvent)
	 */
	public void serialEvent(SerialPortEvent event) {
		switch (event.getEventType()) {
		case SerialPortEvent.BI:
		case SerialPortEvent.OE:
		case SerialPortEvent.FE:
		case SerialPortEvent.PE:
		case SerialPortEvent.CD:
		case SerialPortEvent.CTS:
		case SerialPortEvent.DSR:
		case SerialPortEvent.RI:
			break;

		case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
			// No data recieved
			break;
		case SerialPortEvent.DATA_AVAILABLE:
			logger.log(Level.INFO, "New Data Received");
			byte[] readBuffer = new byte[8];
			int numBytes = 0;
			try {
				while (inputStream.available() > 0) {
					numBytes = inputStream.read(readBuffer);
					String frameData = new String(readBuffer);
					//logger.log(Level.INFO, frameData);
					processFrameData(frameData, numBytes);
				}
			} catch (IOException e) {
				System.out.println(e);
			}
			break;
		}
	}

	private void processFrameData(String frameData, int numBytes) {
		for (int n = 0; n < numBytes; n++) {
			char current_data = (char) frameData.charAt(n);
			if (!data_started) {
				data_ended = false;
				if (display_data.endsWith("SA")) {
					data_started = true;
					display_data = "SA";
				} else

					display_data = display_data + current_data;
			}

			if (data_started) {
				display_data = display_data + current_data;

				if (data_started && !data_ended && display_data.endsWith("E")) {
					data_started = false;
					data_ended = true;
					processDisplayData(display_data);
					display_data = "";
				}
			}

		}
	}

	private void processDisplayData(String displayData) {
		int[] finalData = new int[128];
		if (!displayData.equals("SABCDE")) {
			logger.info("Processing the display data");
			int n = 0;
			for (int k = 1; k < displayData.length(); k++) {
				if (displayData.charAt(k) == 'A'
						|| displayData.charAt(k) == 'B'
						|| displayData.charAt(k) == 'C'
						|| displayData.charAt(k) == 'D') {
					continue;

				}
				if (displayData.charAt(k) == 'E') {
					k = 100;
					break;
				}
				String str1 = Integer.toBinaryString(displayData.charAt(k));
				String str2 = str1.substring(2);
				for (int i = 0; i < 4; i++) {
					if (n * 4 + i > 127)
						break;
					switch (str2.charAt(i)) {

					case '1':
						finalData[n * 4 + i] = 1;
						break;
					case '0':
						finalData[n * 4 + i] = 0;
						break;
					default:
						// finalData[n * 4 + i] = 0;
						break;
					}

				}
				n++;
			}
		}
		try {
			boolean dataChanged = false;
			for (int i = 0; i < finalData.length; i++) {
				if (finalData[i] != previousData[i])
					dataChanged = true;
			}
			if (dataChanged) {
				int[][] matrix = transformToMatrix(finalData);
				logger.log(Level.INFO, "Data is converted into martix");
				measureCount(matrix);
				writeObjectCSV(matrix, file);
				showData(finalData);
				// writeIntoCSV(finalData, file);
			}

			for (int i = 0; i < finalData.length; i++) {
				previousData[i] = finalData[i];
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// ((ElderCareView)elderCareView).updateLabel();
	}

	private void showData(final int[] finalData) throws IOException {
		logger.log(Level.INFO, "Showing data on labels");
		Display.getDefault().asyncExec(new Runnable() {

			public void run() {
				if (elderCareView instanceof View) {
					Label[] labels = ((View) elderCareView).getChildren();
					for (int i = 0; i < finalData.length; i++) {
						String value = Integer.toString(finalData[i]);
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
								// writeIntoExcel(workbook, labels[i], i,
								// value);
							}
						} else {
							Color backGroundColor = (Color) labels[i]
									.getData("greenBack");
							labels[i].setBackground(backGroundColor);
							Composite parent = labels[i].getParent();
							if (parent instanceof Canvas) {
								Canvas canvas = (Canvas) parent;
								canvas.setBackground(backGroundColor);
								// writeIntoExcel(workbook, labels[i], i,
								// value);
							}
						}
					}
					((View) elderCareView).redraw();
				}

			}
		});
		/*
		 * Workbook wb = Workbook.getWorkbook(file); WritableWorkbook workbook =
		 * Workbook.createWorkbook(file, wb);
		 */
		/*
		 * workbook.write(); workbook.close();
		 */
	}

	private void measureCount(int[][] matrix) {
		int sum = 0;
		boolean flag = false;
		for (int r = 0; r < matrix.length; r++) {
			for (int c = 0; c < matrix[r].length; c++) {
				if (matrix[r][c] == 1) {
					if (c < matrix[r].length - 3)
						if (matrix[r][c + 1] == 1 && matrix[r][c + 2] == 1)
							flag = true;
					sum = sum + matrix[r][c];
				}
			}
		}
		for (int c = 0; c < matrix[0].length; c++) {
			for (int r = 0; r < matrix.length; r++) {
				if (matrix[r][c] == 1) {
					if (r < matrix.length - 3)
						if (matrix[r + 1][c] == 1 && matrix[r + 2][c] == 1)
							flag = true;
				}
			}
		}
		/*
		 * if (sum >= 6 && flag) { Display.getDefault().beep();
		 * 
		 * sum = 0; flag = false; }
		 */
	}

	private int[][] transformToMatrix(int[] finalData) {
		int[][] matrix = new int[12][12];
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
		logger.log(Level.INFO, "------------------New Frame------------------");
		for (int r = 0; r < sumMatrix.length; r++) {
			String row = "";
			for (int c = 0; c < sumMatrix[r].length; c++) {
				row = row + "  " + sumMatrix[r][c];
			}
			logger.log(Level.INFO, row);
		}
		return matrix;
	}

	private void writeIntoCSV(Object[] finalData, int[][] matrix, File file) {
		String[] finalString = new String[finalData.length + 1];
		CSVWriter csvW = null;
		PrintWriter fw = null;
		try {
			fw = new PrintWriter(new FileWriter(file, true));
			csvW = new CSVWriter(fw);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		for (int i = 0; i < finalData.length; i++) {
			finalString[i] = "" + finalData[i];
		}
		finalString[finalData.length] = Calendar.getInstance().getTime()
				.toLocaleString();
		if (finalString != null && finalString.length > 1) {

			/*
			 * System.out.println("------------------New Frame------------------"
			 * ); for (int r = 0; r < matrix.length; r++) { String row = ""; for
			 * (int c = 0; c < matrix[r].length; c++) { row = row + "  "
			 * +matrix[r][c]; } System.out.println(row); }
			 */
			csvW.writeNext(finalString);
			try {
				csvW.close();
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void writeObjectCSV(int[][] matrix, File file) {
		// String[] finalString = new String[];
		logger.log(Level.INFO, "Writing into CSV");
		ArrayList valueCoordinates = new ArrayList();

		for (int r = 0; r < matrix.length; r++) {
			String row = "";
			for (int c = 0; c < matrix[r].length; c++) {
				if (matrix[r][c] == 1) {
					valueCoordinates
							.add(matrix[r][c] + "(" + r + "," + c + ")");
				}
				// row = row + " " +matrix[r][c];
			}
			// System.out.println(row);
		}
		valueCoordinates.trimToSize();
		Object[] finalData = valueCoordinates.toArray();
		writeIntoCSV(finalData, matrix, file);
		/*
		 * String[] finalString = new String[finalData.length + 1]; CSVWriter
		 * csvW = null; PrintWriter fw = null; try { fw = new PrintWriter(new
		 * FileWriter(file, true)); csvW = new CSVWriter(fw); } catch
		 * (IOException e1) { e1.printStackTrace(); } for (int i = 0; i <
		 * finalData.length; i++) { finalString[i] = "" + finalData[i]; }
		 * finalString[finalData.length] = Calendar.getInstance().getTime()
		 * .toLocaleString(); if (finalString != null) {
		 * csvW.writeNext(finalString); try { csvW.close(); fw.close(); } catch
		 * (IOException e) { e.printStackTrace(); } }
		 */

	}
}
