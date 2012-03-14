import java.text.DateFormatSymbols;
import java.util.*;

public class CurrentTime {
	public static void main(String[] args) {
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
		System.out.println("File name :"+monthName+"_"+date+"_"+year+"_"+hour+am_pm);
	}
}
