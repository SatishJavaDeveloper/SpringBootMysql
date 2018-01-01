package hello;

import java.util.Date;

import javax.persistence.Entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.couchbase.client.deps.com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ibm.icu.util.TimeZone;

@Entity
public class CloseHoursSchedulerEntity {

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
	private String schedularType;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss",timezone="Asia/Kolkata")
	@Temporal(TemporalType.TIMESTAMP)
	private Date invokeTime;
	private Long sleepTime;
	private int dayOfWeek;
	
	private boolean status;
	
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getSchedularType() {
		return schedularType;
	}
	public void setSchedularType(String schedularType) {
		this.schedularType = schedularType;
	}
	public Date getInvokeTime() {
		return invokeTime;
	}
	public void setInvokeTime(Date invokeTime) {
		this.invokeTime = invokeTime;
	}
	public Long getSleepTime() {
		return sleepTime;
	}
	public void setSleepTime(Long sleepTime) {
		this.sleepTime = sleepTime;
	}
	public int getDayOfWeek() {
		return dayOfWeek;
	}
	public void setDayOfWeek(int dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}
	@Override
	public String toString() {
		return "CloseHoursSchedulerEntity [id=" + id + ", schedularType=" + schedularType + ", invokeTime=" + invokeTime
				+ ", sleepTime=" + sleepTime + ", dayOfWeek=" + dayOfWeek + "]";
	}
	
}
