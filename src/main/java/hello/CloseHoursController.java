package hello;

import java.util.List;
import java.util.Timer;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController    // This means that this class is a Controller
@RequestMapping(path="/schedule")
public class CloseHoursController {

	@Autowired
	CloseHoursSchedule scheduleService;
	
	@Autowired
	CloseHoursHistory closeHoursHistory;
	@Autowired
	MqttService mqttService;
	@RequestMapping(value="create",method=RequestMethod.POST)
	public String createSchedule(@RequestBody CloseHoursSchedularEntity requestBody)
	{/*CloseHoursSchedulerEntity entity = new CloseHoursSchedulerEntity();
	entity.setSchedularType("oneTime");
	entity.setInvokeTime(new DateTime());
	entity.setDayOfWeek(4);
	entity.setSleepTime((long)31001);
	
		*/
		requestBody.setStatus(false);
		scheduleService.saveAndFlush(requestBody);
		System.out.println("no records:"+scheduleService.findAll().size());
		CloseHoursSchedularEntity req=mqttService.getExecutionTask();
		System.out.println("reqt:"+req);
		//ExecuteTask.timer.purge();
		if(ExecuteTask.hasStarted==false)
		{
			
		ExecuteTask.timer.cancel();
		ExecuteTask.timer=new Timer();
		CloseHoursSchedularHistory pendingTask = closeHoursHistory.getPendingTask();
		if(pendingTask!=null)
		{ 
			Long seconds=(pendingTask.getWakeUpTime().getTime()-pendingTask.getInvokeTime().getTime())/1000;

			CloseHoursSchedularEntity entity=scheduleService.getOne(pendingTask.getId());
			Long secondsLeft=entity.getSleepTime()-seconds;
			entity.setSleepTime(secondsLeft);
			entity.setInvokeTime(pendingTask.getWakeUpTime());
			
			ExecuteTask.timer.schedule(new ExecuteTask(scheduleService,entity,closeHoursHistory,mqttService),entity.getInvokeTime());
					
		}
		else{
		ExecuteTask.timer.schedule(new ExecuteTask(scheduleService,req,closeHoursHistory,mqttService),req.getInvokeTime());
		}
		}
		List<CloseHoursSchedularEntity> lists=scheduleService.findAll();
		System.out.println(lists.size());
		return "success";
		
	}
	@RequestMapping(value="all",method=RequestMethod.GET)
	public String getSchedules(HttpServletRequest request,HttpServletResponse response)
	{/*CloseHoursSchedulerEntity entity = new CloseHoursSchedulerEntity();
	entity.setSchedularType("oneTime");
	entity.setInvokeTime(new DateTime());
	entity.setDayOfWeek(4);
	entity.setSleepTime((long)31001);
	
		
		scheduleService.saveAndFlush(entity);*/
	//	ExecuteTask task=new ExecuteTask(scheduleService);
	//	task.getExecutionTask();
		CloseHoursSchedularEntity lists=scheduleService.getOneTimeScheduler();
		System.out.println("size:"+lists.getInvokeTime());
//		System.out.println(lists.get(0).getInvokeTime());
		
		return lists.toString();
		
		//return "success";
	}
	/*@PostConstruct
	public void initialTask() throws InterruptedException
	{
		System.out.println("initial task");
		CloseHoursSchedulerEntity req=new ExecuteTask().getExecutionTask();
		ExecuteTask.timer.schedule(new ExecuteTask(scheduleService,req),req.getInvokeTime());
	}*/
	@PostConstruct
	public void initial()
	{System.out.println("initial task"+scheduleService);
	//ExecuteTask task=new ExecuteTask(scheduleService,closeHoursHistory);
	CloseHoursSchedularHistory pendingTask = closeHoursHistory.getPendingTask();
	if(pendingTask!=null)
	{ 
		Long seconds=(pendingTask.getWakeUpTime().getTime()-pendingTask.getInvokeTime().getTime())/1000;

		CloseHoursSchedularEntity entity=scheduleService.getOne(pendingTask.getId());
		Long secondsLeft=entity.getSleepTime()-seconds;
		entity.setSleepTime(secondsLeft);
		entity.setInvokeTime(pendingTask.getWakeUpTime());
		
		ExecuteTask.timer.schedule(new ExecuteTask(scheduleService,entity,closeHoursHistory,mqttService),entity.getInvokeTime());
		
		
	}
	else {
		CloseHoursSchedularEntity req=mqttService.getExecutionTask();
		System.out.println("req:"+req);
		if(req!=null)
		ExecuteTask.timer.schedule(new ExecuteTask(scheduleService,req,closeHoursHistory,mqttService),req.getInvokeTime());
	}
	}
}
