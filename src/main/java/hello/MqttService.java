package hello;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.junit.FixMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class MqttService {

	@Autowired
	CloseHoursSchedule scheduleService;

	@Autowired
	CloseHoursHistory closeHoursHistory;
@Scheduled(fixedDelay=10000, initialDelay=100)	
public void schedularTask()
{
	
	CloseHoursSchedularEntity entity=getExecutionTask(new Date());
	if(entity==null)
	{
		if(scheduleService.getWeeklySchedulers().size()>0)
		{
			Calendar cal= Calendar.getInstance();
			cal.setTime(new Date());
			cal.add(Calendar.DATE, 7);
			entity=getExecutionTask(cal.getTime());
			
		}
	}
	if(entity!=null) {
		if(new Date().after(entity.getInvokeTime()))
		{
//publish	
			System.out.println("publishing:"+entity.getSleepTime());
			if(entity.getSchedularType().equalsIgnoreCase("oneTime"))
			{
				entity.setStatus(true);
				scheduleService.save(entity);
				
			}
			CloseHoursSchedularHistory history=new CloseHoursSchedularHistory();
			history.setId(entity.getId());
			history.setPublishedTime(new Date());
			history.setInvokeTime(entity.getInvokeTime());
			Calendar calendar=Calendar.getInstance();
			calendar.setTime(entity.getInvokeTime());
			calendar.add(Calendar.SECOND,entity.getSleepTime().intValue());
			history.setWakeUpTime(calendar.getTime());
			if(entity.getSleepTime()>64800)
			{
				history.setStatus("inprogress");
			}
			else
			{
				history.setStatus("completed");
			}
			System.out.println("close hours history:"+closeHoursHistory);
			closeHoursHistory.save(history);
			
		}
		
	}
	
//System.out.println("records count:"+closeHoursHistory.count());	
}
	
	public CloseHoursSchedularEntity getExecutionTask(Date currentDate) {
		CloseHoursSchedularHistory pendingTask = closeHoursHistory.getPendingTask();
		if(pendingTask!=null)
		{ 
			Long seconds=(pendingTask.getWakeUpTime().getTime()-pendingTask.getInvokeTime().getTime())/1000;
	
			CloseHoursSchedularEntity entity=scheduleService.getOne(pendingTask.getId());
			Long secondsLeft=entity.getSleepTime()-seconds;
			entity.setSleepTime(secondsLeft);
			entity.setInvokeTime(pendingTask.getWakeUpTime());
			return entity;
			
			
		}
		Map<Long, Date> records = new HashMap<>();
		System.out.println(scheduleService);
		CloseHoursSchedularEntity oneTime = scheduleService.getOneTimeScheduler();
		List<CloseHoursSchedularEntity> weekly = scheduleService.getWeeklySchedulers();
		System.out.println(weekly.size());
		for (CloseHoursSchedularEntity entity : weekly) {
			Calendar c = Calendar.getInstance();
			Date temp =  currentDate; //new Date();
			temp.setHours(entity.getInvokeTime().getHours());
			temp.setMinutes(entity.getInvokeTime().getMinutes());
			temp.setSeconds(entity.getInvokeTime().getSeconds());
			c.setTime(temp);
			c.set(Calendar.DAY_OF_WEEK, entity.getDayOfWeek());

			// if(c.getTime().after(new Date()))
			// {
			CloseHoursSchedularHistory schedularHistory = closeHoursHistory.getWeeklySchedulers(entity.getId(),
					c.getTime());
			System.out.println(c.getTime());
			if (schedularHistory == null) {
				records.put(entity.getId(), c.getTime());
			}
			// }
		}

		Map<Long, Date> sortedMap = records.entrySet().stream().sorted(Entry.comparingByValue())
				.collect(Collectors.toMap(Entry::getKey, Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
		Date weeklyDateObj = null;
		System.out.println("size:" + sortedMap.size());
		if(oneTime!=null && sortedMap.size()==0)
		{
			return oneTime;
		}
		if(oneTime==null && sortedMap.size() > 0)
		{ weeklyDateObj = (Date) sortedMap.values().toArray()[0];
			CloseHoursSchedularEntity response = scheduleService.findOne((Long) sortedMap.keySet().toArray()[0]);
			response.setInvokeTime(weeklyDateObj);
			return response;
		}
		
		if (sortedMap.size() > 0 && oneTime != null && oneTime.getInvokeTime() != null) {
			weeklyDateObj = (Date) sortedMap.values().toArray()[0];
			if (oneTime.getInvokeTime().after(weeklyDateObj)) {
				CloseHoursSchedularEntity response = scheduleService.findOne((Long) sortedMap.keySet().toArray()[0]);
				response.setInvokeTime(weeklyDateObj);
				return response;
			}

			return oneTime;
		} else {
			return null;
		}

	}
}
