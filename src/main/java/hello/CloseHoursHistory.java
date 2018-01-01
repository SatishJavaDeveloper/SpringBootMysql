package hello;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CloseHoursHistory extends JpaRepository<CloseHoursSchedularHistory, Long> {

	@Query(value="Select * From close_hours_schedular_history where id= :id and invoke_time=:date",nativeQuery = true)	
	public CloseHoursSchedularHistory getWeeklySchedulers(@Param("id") Long id,@Param("date") Date invoke);	
	
	@Query(value="Select * From close_hours_schedular_history where published_time<= :invokeTime and wake_up_time>=:invokeTime",nativeQuery = true)	
	public List<CloseHoursSchedularHistory> isTagAvailable(@Param("invokeTime") Date invoke);
	
	@Query(value="Select * From close_hours_schedular_history where status='inprogress'")
	public CloseHoursSchedularHistory getPendingTask();
	
}
