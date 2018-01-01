package hello;


import java.util.Calendar;
import java.util.Date;

public class Day {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Calendar c = Calendar.getInstance();
		
		c.setTime(new Date());
		
		//c.set(Calendar.DAY_OF_WEEK,4);
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		Date date=c.getTime();
		System.out.println(dayOfWeek);
		System.out.println("time:"+date);
	}

}
