package hello;

import java.util.*;
import java.text.*;
 
public class ScheduleTask {
   static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
   static Timer timer = new Timer();
   private static class MyTimeTask extends TimerTask {
	   
      public void run() {
         System.out.println("Running Task");
         System.out.println("Current Time: " + df.format( new Date()));
         //stimer.cancel();
      }
   }
 
   public static void main(String[] args) throws ParseException {
 
      System.out.println("Current Time: " + df.format( new Date()));
 
      //Date and time at which you want to execute
      Date date = df.parse("2018-01-02 22:19:50");
      Date dateObj = df.parse("2017-01-02 22:21:00");
      timer.schedule(new MyTimeTask(), date);
      timer.schedule(new MyTimeTask(), dateObj);
 System.out.println("test");
   }
}