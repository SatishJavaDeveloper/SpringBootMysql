package hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestApp {
	@Autowired
	CloseHoursSchedule scheduleService;
	
	/*public void initial()
	{System.out.println("initial task"+scheduleService);
	ExecuteTask task=new ExecuteTask(scheduleService);
		CloseHoursSchedulerEntity req=task.getExecutionTask();
		//ExecuteTask.timer.schedule(new ExecuteTask(scheduleService,req),req.getInvokeTime());
	}*/
}
