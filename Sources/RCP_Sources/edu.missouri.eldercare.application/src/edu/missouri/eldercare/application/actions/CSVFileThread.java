package edu.missouri.eldercare.application.actions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import edu.missouri.eldercare.application.utilities.CSVReader;
import edu.missouri.eldercare.application.utilities.DataUpdateListener;

public class CSVFileThread implements Runnable {
	private ArrayList listeners;
	private String path;
	private static CSVFileThread instance;

	public CSVFileThread() {
		listeners = new ArrayList();
	}

	public static CSVFileThread getInstance() {
		if (instance != null) {
			return instance;
		} else
			return instance = new CSVFileThread();
	}

	public void setPath(String path) {
		this.path = path;
	}

	// @Override
	public void run() {
		try {
			CSVReader csvReader = new CSVReader(new FileReader(new File(path)));
			String[] nextLine = null;
			final int[] sum = new int[128];
			while ((nextLine = csvReader.readNext()) != null) {
				for (int i = 0; i < nextLine.length - 1; i++) {
					String[] split = nextLine[i].split("\\(");
					String value = split[0];
					String coord = split[1].substring(0, split[1].length() - 1);
					String[] coordinates = coord.split(",");
					int row = Integer.parseInt(coordinates[0]);
					int column = Integer.parseInt(coordinates[1]);
					sum[row * 8 + column] = sum[row * 8 + column]
							+ Integer.parseInt(value);
				}
				dataUpdated(sum);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void dataUpdated(int[] finalData) {
		for (Iterator iterator = listeners.iterator(); iterator.hasNext();) {
			DataUpdateListener listener = (DataUpdateListener) iterator.next();
			listener.dataUpdated(finalData);
		}
	}

	public void addDataUpdateListener(DataUpdateListener listener) {
		listeners.add(listener);
	}

	public void removeDataUpdateListener(DataUpdateListener listener) {
		if (listeners.contains(listener))
			listeners.remove(listener);
	}

	public static boolean isCSVThreadNull() {
		if (instance == null)
			return true;
		return false;
	}

	public void stopDisplay() {
		instance = null;
	}

}
