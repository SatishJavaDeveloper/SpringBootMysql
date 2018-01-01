package hello;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CloseHoursSchedule extends JpaRepository<CloseHoursSchedularEntity,Long> {

	@Query(value="Select * From close_hours_schedular_entity where schedular_type='oneTime' and status=false ORDER BY invoke_time ASC LIMIT 1",nativeQuery = true)
	public CloseHoursSchedularEntity getOneTimeScheduler();
	@Query(value="Select * From close_hours_schedular_entity where schedular_type='weekly'",nativeQuery = true)	
	public List<CloseHoursSchedularEntity> getWeeklySchedulers();

}
