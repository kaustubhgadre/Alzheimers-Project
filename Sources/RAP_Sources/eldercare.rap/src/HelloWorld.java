

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.TooManyListenersException;

import au.com.bytecode.opencsv.CSVWriter;

public class HelloWorld implements Runnable, SerialPortEventListener {

	private static SerialPort serialPort;
	private static InputStream inputStream;
	private String display_data = "";
	private boolean data_ended = false;
	private boolean data_started = false;
	private int[] previousData = new int[128];
	private int[][] sumMatrix = new int[12][12];
	File file = new File("/carpet/data/raw.csv");
	File rawFile = new File("/carpet/data/raw.txt");
	private File currentInstance = new File("/carpet/data/currentInstance.csv");
	private int delayCount = 0;

	public static void main(String[] args) {
		Enumeration portList = CommPortIdentifier.getPortIdentifiers();
		String portName = "/dev/ttyUSB0";
		while (portList.hasMoreElements()) {

			CommPortIdentifier portId = (CommPortIdentifier) portList
					.nextElement();
			if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				if (portId.getName().equals(portName)) {
					try {
						serialPort = (SerialPort) portId.open("Elder Care",
								2000);
						inputStream = serialPort.getInputStream();
						serialPort.addEventListener(new HelloWorld());
						serialPort.notifyOnDataAvailable(true);
						//serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
						serialPort.setSerialPortParams(19200,
								SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
								SerialPort.PARITY_NONE);
					} catch (PortInUseException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (TooManyListenersException e) {
						e.printStackTrace();
					} catch (UnsupportedCommOperationException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}


	@Override
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
			byte[] readBuffer = new byte[8];
			int numBytes = 0;
			try {
				while (inputStream.available() > 0) {
					numBytes = inputStream.read(readBuffer);
					String frameData = new String(readBuffer);
					// System.out.println(frameData);
					processFrameData(frameData, numBytes);
				}
			} catch (IOException e) {
				System.out.println(e);
			}
			/*try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			break;
		}
	}

	@Override
	public void run() {

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
					//storeRawData(display_data);
					//System.out.println(display_data);
					processDisplayData(display_data);
					display_data = "";
				}
			}

		}
	}

	private void storeRawData(String rawData) {
		BufferedWriter writer = null;
		if (!rawData.equals("SABCDE")) {
			try {
				writer = new BufferedWriter(new FileWriter(rawFile, true));
				writer.write(rawData);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private void processDisplayData(String displayData) {
		int[] finalData = new int[128];
		if (!displayData.equals("SABCDE")) {
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
		} else {

		}
		/*boolean dataChanged = false;
		for (int i = 0; i < finalData.length; i++) {
			if (finalData[i] != previousData[i])
				dataChanged = true;
		}
*/
		//if (dataChanged) {
			// showData(finalData);
			delayCount++;
		    if(delayCount==5){
		    	int[][] matrix = transformToMatrix(finalData);
			// measureCount(matrix);
		    	writeObjectCSV(matrix, getFile());
		        delayCount = 0;
		    }
			// writeIntoCSV(finalData, file);
		/*}

		for (int i = 0; i < finalData.length; i++) {
			previousData[i] = finalData[i];
		}

*/		// ((ElderCareView)elderCareView).updateLabel();
	}

	private File getFile() {
		Calendar calendar = new GregorianCalendar();
		String am_pm;
		int month = calendar.get(Calendar.MONTH);
		String monthName = DateFormatSymbols.getInstance().getMonths()[month];
		int year = calendar.get(Calendar.YEAR);
		int date = calendar.get(Calendar.DATE);
		int hour = calendar.get(Calendar.HOUR);
		if (calendar.get(Calendar.AM_PM) == 0)
			am_pm = "AM";
		else
			am_pm = "PM";
		return new File("/carpet/data/"+monthName+"_"+date+"_"+year+"_"+hour+am_pm+".csv");
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
		/*
		 * System.out.println("------------------New Frame------------------");
		 * for (int r = 0; r < sumMatrix.length; r++) { String row = ""; for
		 * (int c = 0; c < sumMatrix[r].length; c++) { row = row + "  " +
		 * sumMatrix[r][c]; } System.out.println(row); }
		 */
		return matrix;
	}

	private void writeObjectCSV(int[][] matrix, File file) {
		// String[] finalString = new String[];
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
	}

	private void writeIntoCSV(Object[] finalData, int[][] matrix, File file) {
		String[] finalString = new String[finalData.length + 1];
		CSVWriter csvW = null;
		PrintWriter fw = null;
		synchronized (file) {
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
				 * System.out.println(
				 * "------------------New Frame------------------" ); for (int r
				 * = 0; r < matrix.length; r++) { String row = ""; for (int c =
				 * 0; c < matrix[r].length; c++) { row = row + "  "
				 * +matrix[r][c]; } System.out.println(row); }
				 */
				csvW.writeNext(finalString);
				
				//DataCollectionManager.getInstance().dataUpdated(finalString);
				
				try {
					synchronized (currentInstance) {
						CSVWriter currentCSVW = new CSVWriter(new PrintWriter(
								new FileWriter(currentInstance, false)));
						currentCSVW.writeNext(finalString);
						currentCSVW.close();
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				// System.out.println(finalString);
				try {
					csvW.close();
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}

	class FileListFilter implements FilenameFilter {
		private String name;

		private String extension;

		public FileListFilter(String name, String extension) {
			this.name = name;
			this.extension = extension;
		}

		public boolean accept(File directory, String filename) {
			boolean fileOK = true;

			if (name != null) {
				fileOK &= filename.startsWith(name);
			}

			if (extension != null) {
				fileOK &= filename.endsWith('.' + extension);
			}
			return fileOK;
		}
	}

}
