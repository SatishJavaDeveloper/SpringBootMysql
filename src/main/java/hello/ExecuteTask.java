package hello;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

public class ExecuteTask extends TimerTask{
	static Timer timer = new Timer();
	CloseHoursSchedule scheduleService;
	CloseHoursSchedulerEntity closeHours;
	public ExecuteTask(CloseHoursSchedule scheduleService)
	{ System.out.println("schedule service");
		this.scheduleService=scheduleService;
		
	}
	public ExecuteTask()
	{
		
	}
	public ExecuteTask(CloseHoursSchedule scheduleService,CloseHoursSchedulerEntity closeHours)
	{
		this.scheduleService=scheduleService;
		this.closeHours=closeHours;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("current task:"+closeHours.getInvokeTime());
		if(closeHours.getSchedularType().equalsIgnoreCase("oneTime"))
		{
			closeHours.setStatus(true);
			scheduleService.save(closeHours);
		}
		//CloseHoursSchedulerEntity entry=getExecutionTask();
		//timer.cancel();
		setNextTask();
		
	}
	// extends TimerTask
	/*
	 * public CloseHoursSchedulerEntity entity; public CloseHoursSchedule
	 * object;
	 * 
	 * public ExecuteTask(CloseHoursSchedulerEntity entity,CloseHoursSchedule
	 * object) { this.entity=entity; this.object=object;
	 * 
	 * } static Timer timer = new Timer();
	 */

	/*
	 * @Override public void run() {
	 * 
	 * System.out.println("executing:"+entity.getInvokeTime());
	 * 
	 * // TODO Auto-generated method stub
	 * 
	 * }
	 */
	public CloseHoursSchedulerEntity getExecutionTask() {
		Map<Long, Date> records = new HashMap<>();
System.out.println(scheduleService);
		CloseHoursSchedulerEntity oneTime = scheduleService.getOneTimeScheduler();
		List<CloseHoursSchedulerEntity> weekly = scheduleService.getWeeklySchedulers();
		System.out.println(weekly.size());
		for (CloseHoursSchedulerEntity entity : weekly) {
			Calendar c = Calendar.getInstance();
			Date temp=new Date();
			temp.setHours(entity.getInvokeTime().getHours());
			temp.setMinutes(entity.getInvokeTime().getMinutes());
			temp.setSeconds(entity.getInvokeTime().getSeconds());
			c.setTime(temp);
			c.set(Calendar.DAY_OF_WEEK, entity.getDayOfWeek());
			
			if(c.getTime().after(new Date()))
			{
			System.out.println(c.getTime());
			records.put(entity.getId(), c.getTime());
		}
		}
		
		Map<Long, Date> sortedMap = 
				records.entrySet().stream()
			    .sorted(Entry.comparingByValue())
			    .collect(Collectors.toMap(Entry::getKey, Entry::getValue,
			                              (e1, e2) -> e1, LinkedHashMap::new));

		Date weeklyDateObj=(Date) sortedMap.values().toArray()[0];
		if(oneTime.getInvokeTime().after(weeklyDateObj))
		{
			CloseHoursSchedulerEntity response=scheduleService.findOne((Long)sortedMap.keySet().toArray()[0]);
			response.setInvokeTime(weeklyDateObj);
			return response;
		}
		
		/*Iterator it = records.entrySet().iterator();
		System.out.println("before sort:");
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        System.out.println(pair.getKey() + " = " + pair.getValue());
	        it.remove(); // avoids a ConcurrentModificationException
	    }
	    
	    System.out.println("after sort:");
	    Iterator itr = sortedMap.entrySet().iterator();
	    while (itr.hasNext()) {
	        Map.Entry pair = (Map.Entry)itr.next();
	        System.out.println(pair.getKey() + " = " + pair.getValue());
	        itr.remove(); // avoids a ConcurrentModificationException
	    }*/
	    
		return oneTime;
    }
	
	public void setNextTask()
	{
		timer.schedule(new ExecuteTask(),getExecutionTask().getInvokeTime());
	}
	

}
