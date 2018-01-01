package hello;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MqttService {

	@Autowired
	CloseHoursSchedule scheduleService;

	@Autowired
	CloseHoursHistory closeHoursHistory;

	public CloseHoursSchedularEntity getExecutionTask() {
		Map<Long, Date> records = new HashMap<>();
		System.out.println(scheduleService);
		CloseHoursSchedularEntity oneTime = scheduleService.getOneTimeScheduler();
		List<CloseHoursSchedularEntity> weekly = scheduleService.getWeeklySchedulers();
		System.out.println(weekly.size());
		for (CloseHoursSchedularEntity entity : weekly) {
			Calendar c = Calendar.getInstance();
			Date temp = new Date();
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
