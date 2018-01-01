package hello;

import java.util.List;

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
	
	@RequestMapping(value="create",method=RequestMethod.POST)
	public String createSchedule(@RequestBody CloseHoursSchedulerEntity requestBody)
	{/*CloseHoursSchedulerEntity entity = new CloseHoursSchedulerEntity();
	entity.setSchedularType("oneTime");
	entity.setInvokeTime(new DateTime());
	entity.setDayOfWeek(4);
	entity.setSleepTime((long)31001);
	
		*/
		requestBody.setStatus(false);
		scheduleService.saveAndFlush(requestBody);
		CloseHoursSchedulerEntity req=new ExecuteTask().getExecutionTask();
		ExecuteTask.timer.schedule(new ExecuteTask(scheduleService,req),req.getInvokeTime());
		List<CloseHoursSchedulerEntity> lists=scheduleService.findAll();
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
		CloseHoursSchedulerEntity lists=scheduleService.getOneTimeScheduler();
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
	ExecuteTask task=new ExecuteTask(scheduleService);
		CloseHoursSchedulerEntity req=task.getExecutionTask();
		System.out.println("time:"+req.getInvokeTime());
		ExecuteTask.timer.schedule(new ExecuteTask(scheduleService,req),req.getInvokeTime());
	}
}
