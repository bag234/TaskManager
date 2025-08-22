package org.mrbag.test.TaskManager.Entity.Duty;

import org.mrbag.test.TaskManager.Entity.Task;
import org.mrbag.test.TaskManager.Entity.TaskPriority;
import org.mrbag.test.TaskManager.Entity.TaskStatus;
import org.mrbag.test.TaskManager.Entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {

	String title;
	
	String description;
	
	TaskStatus status;
	
	TaskPriority priority;

	Long assigner;
	
	public Task toTask(User usr) {
		
		return Task
				.builder()
				.title(title)
				.description(description)
				.status(status == null ? TaskStatus.TODO : status)
				.priority(priority == null ? TaskPriority.LOW : priority)
				.author(usr)
				.assigner(assigner == null ? null : User.builder().id(assigner).build())
				.build();
	}
}
