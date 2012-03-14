/**
 * 
 */
package edu.missouri.eldercare.application.handlers;


/**
 * @author Kaustubh
 * 
 */
public class ExcelHandler {/*

	private File file;
	private WorkbookSettings wbSettings;

	public ExcelHandler(WorkbookSettings wbSettings, File file) {
		this.wbSettings = wbSettings;
		this.file = file;
	}

	public void createExcel() {
		WritableWorkbook workbook = null;
		try {
			workbook = Workbook.createWorkbook(file, wbSettings);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		workbook.createSheet("Area A", 0);
		workbook.createSheet("Area B", 1);
		workbook.createSheet("Area C", 2);
		workbook.createSheet("Area D", 3);

		WritableSheet[] excelSheets = workbook.getSheets();
		for (int i = 0; i < excelSheets.length; i++) {
			try {
				createLabel(excelSheets[i]);
			} catch (WriteException e) {
				e.printStackTrace();
			}
		}

		try {
			workbook.write();
			workbook.close();
		} catch (WriteException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void removeSummation() throws BiffException, IOException,
			WriteException {
		Workbook wb = Workbook.getWorkbook(file);
		WritableWorkbook workbook = Workbook.createWorkbook(file, wb);
		WritableSheet[] sheets = workbook.getSheets();
		for (int i = 0; i < sheets.length; i++) {
			int rows = sheets[i].getRows();
			if (rows > 1)
				sheets[i].removeRow(rows - 1);
		}
		workbook.write();
		workbook.close();
	}

	private void createLabel(WritableSheet sheet) throws WriteException {
		// Define the cell format
		WritableCellFormat times = new WritableCellFormat(new WritableFont(
				WritableFont.TIMES, 10, WritableFont.BOLD));
		// Lets automatically wrap the cells
		// times.setWrap(true);
		times.setAlignment(Alignment.CENTRE);
		// Create create a bold font with unterlines
		WritableCellFormat timesBoldUnderline = new WritableCellFormat(
				new WritableFont(WritableFont.TIMES, 10, WritableFont.BOLD,
						false, UnderlineStyle.NO_UNDERLINE));
		// Lets automatically wrap the cells
		// timesBoldUnderline.setWrap(true);
		sheet.getSettings().setVerticalFreeze(1);
		CellView cv = new CellView();
		cv.setFormat(times);
		cv.setFormat(timesBoldUnderline);
		// cv.setAutosize(true);
		cv.setSize(25 * 8);
		// Write a few headers

		for (int i = 1; i <= 8; i++) {
			addLabel(sheet, i - 1, 0, "A" + i, times);
			addLabel(sheet, 7 + i, 0, "B" + i, times);
			addLabel(sheet, 15 + i, 0, "C" + i, times);
			addLabel(sheet, 23 + i, 0, "D" + i, times);
		}
		cv.setSize(10 * 200);
		addLabel(sheet, 32, 0, "Time", times);
	}

	private void addLabel(WritableSheet sheet, int column, int row, String s,
			WritableCellFormat format) throws WriteException,
			RowsExceededException {
		Label label;
		label = new Label(column, row, s, format);
		sheet.addCell(label);
	}

	public void addNumber(WritableSheet sheet, int column, int row,
			int integer, WritableCellFormat format) throws WriteException,
			RowsExceededException, IOException {
		Number number;
		number = new Number(column, row, integer, format);
		sheet.addCell(number);
	}

	public void addSummation() throws WriteException, BiffException,
			IOException {
		Workbook wb = Workbook.getWorkbook(file);
		WritableWorkbook workbook = Workbook.createWorkbook(file, wb);
		WritableCellFormat times = new WritableCellFormat(new WritableFont(
				WritableFont.TIMES, 10, WritableFont.BOLD));
		times.setAlignment(Alignment.CENTRE);
		CellView cv = new CellView();
		cv.setFormat(times);
		WritableSheet[] sheets = workbook.getSheets();

		for (int i = 0; i < sheets.length; i++) {
			WritableSheet writableSheet = sheets[i];
			int rows = writableSheet.getRows();
			for (int j = 0; j < 32; j++) {
				Cell[] column = writableSheet.getColumn(j);
				int sum = 0;
				for (int k = 1; k < column.length; k++) {
					String contents = column[k].getContents();
					sum = sum + Integer.parseInt(contents);
				}
				if (rows > 1)
					addNumber(writableSheet, j, rows + 1, sum, times);
			}

		}
		workbook.write();
		workbook.close();
	}

*/}
