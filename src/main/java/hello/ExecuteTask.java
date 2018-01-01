package hello;

import java.util.Calendar;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ExecuteTask extends TimerTask{
	static Timer timer = new Timer();
	CloseHoursSchedule scheduleService;
	CloseHoursSchedularEntity closeHours;
	CloseHoursHistory closeHoursHistory;
	MqttService mqttService;
	public static boolean hasStarted = false;
	public ExecuteTask(CloseHoursSchedule scheduleService,CloseHoursSchedularEntity closeHours,CloseHoursHistory closeHoursHistory,MqttService mqttService)
	{
		this.scheduleService=scheduleService;
		this.closeHours=closeHours;
		this.closeHoursHistory=closeHoursHistory;
		this.mqttService=mqttService;
		
	}
	@Override
	public void run() {
		hasStarted=true;
		// TODO Auto-generated method stub
		List<CloseHoursSchedularHistory> closeHoursEntity=closeHoursHistory.isTagAvailable(closeHours.getInvokeTime());
		if(closeHoursEntity==null)
		{	
		System.out.println("id:"+closeHours.getId());
		System.out.println("current task:"+closeHours.getInvokeTime());
		if(closeHours.getSchedularType().equalsIgnoreCase("oneTime"))
		{
			closeHours.setStatus(true);
			scheduleService.save(closeHours);
			
		}
		CloseHoursSchedularHistory history=new CloseHoursSchedularHistory();
		history.setId(closeHours.getId());
		history.setPublishedTime(new Date());
		history.setInvokeTime(closeHours.getInvokeTime());
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(closeHours.getInvokeTime());
		calendar.add(Calendar.SECOND,closeHours.getSleepTime().intValue());
		history.setWakeUpTime(calendar.getTime());
		if(closeHours.getSleepTime()>64800)
		{
			history.setStatus("inprogress");
		}
		else
		{
			history.setStatus("completed");
		}
		System.out.println("close hours history:"+closeHoursHistory);
		closeHoursHistory.save(history);
		//CloseHoursSchedulerEntity entry=getExecutionTask();
		//timer.cancel();
		setNextTask();
		}
		else
		{
			System.out.println("tag not available");
		}
		hasStarted=false;
		
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
	/*public CloseHoursSchedularEntity getExecutionTask() {
		Map<Long, Date> records = new HashMap<>();
System.out.println(scheduleService);
		CloseHoursSchedularEntity oneTime = scheduleService.getOneTimeScheduler();
		List<CloseHoursSchedularEntity> weekly = scheduleService.getWeeklySchedulers();
		System.out.println(weekly.size());
		for (CloseHoursSchedularEntity entity : weekly) {
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
		Date weeklyDateObj = null;
		System.out.println("size:"+sortedMap.size());
		if(sortedMap.size()>0)
		{
		weeklyDateObj=(Date) sortedMap.values().toArray()[0];
		}
		
		if(oneTime!=null && oneTime.getInvokeTime()!=null)
		{
			if(weeklyDateObj!=null && oneTime.getInvokeTime().after(weeklyDateObj))
		{
			CloseHoursSchedularEntity response=scheduleService.findOne((Long)sortedMap.keySet().toArray()[0]);
			response.setInvokeTime(weeklyDateObj);
			return response;
		}
		
	
		
	     
		return oneTime;
    }
		return null;
	}*/
	
	public void setNextTask()
	{
		System.out.println("next task");
	// do 18 hours logic
	
	
		
		
		//
		CloseHoursSchedularEntity entity=mqttService.getExecutionTask();
		//System.out.println(entity.toString());
		if(entity!=null)
		timer.schedule(new ExecuteTask(scheduleService,entity,closeHoursHistory,mqttService),entity.getInvokeTime());
		
	}
	

}
