package hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
       // CloseHoursController app = new CloseHoursController();
       // app.initial();
       // ExecuteTask task=new ExecuteTask();
       // ExecuteTask.timer.schedule(new ExecuteTask(),task.getExecutionTask().getInvokeTime());
    }
}
