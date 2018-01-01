package hello;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;
@Entity
public class CloseHoursSchedularHistory {
	@Id
	 private Long id;
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss",timezone="Asia/Kolkata")
		@Temporal(TemporalType.TIMESTAMP)
		private Date invokeTime;
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss",timezone="Asia/Kolkata")
		@Temporal(TemporalType.TIMESTAMP)
		private Date wakeUpTime;
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss",timezone="Asia/Kolkata")
		@Temporal(TemporalType.TIMESTAMP)
		private Date publishedTime;
		private String status;
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public Date getInvokeTime() {
			return invokeTime;
		}
		public void setInvokeTime(Date invokeTime) {
			this.invokeTime = invokeTime;
		}
		public Date getWakeUpTime() {
			return wakeUpTime;
		}
		public void setWakeUpTime(Date wakeUpTime) {
			this.wakeUpTime = wakeUpTime;
		}
		public Date getPublishedTime() {
			return publishedTime;
		}
		public void setPublishedTime(Date publishedTime) {
			this.publishedTime = publishedTime;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		@Override
		public String toString() {
			return "CloseHoursSchedularHistory [id=" + id + ", invokeTime=" + invokeTime + ", wakeUpTime=" + wakeUpTime
					+ ", publishedTime=" + publishedTime + ", status=" + status + "]";
		}
		


}

