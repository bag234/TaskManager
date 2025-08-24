package org.mrbag.test.TaskManager.Repository;

import java.util.Collection;
import java.util.List;

import org.mrbag.test.TaskManager.Entity.Task;
import org.mrbag.test.TaskManager.Entity.TaskPriority;
import org.mrbag.test.TaskManager.Entity.TaskStatus;
import org.mrbag.test.TaskManager.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRep extends JpaRepository<Task, Long> {

	@Query("SELECT t FROM Task t WHERE t.id = :id AND (t.author = :usr OR :root = true)")
	Task findOnebyIdAnUser(
			@Param("id") long id,
			@Param("usr") User usr,
			@Param("root") boolean canRoot
			);
	
	@Modifying
	@Query("UPDATE Task t SET "
			+ "t.title = COALESCE(:#{#nt.title}, t.title), "
			+ "t.description = COALESCE(:#{#nt.description}, t.description), "
			+ "t.status = COALESCE(:#{#nt.status}, t.status), "
			+ "t.priority = COALESCE(:#{#nt.priority}, t.priority) "
//			+ "t.assigner.id = COALESCE(:#{#nt.assigner.id}, t.assigner.id) "
			+ "WHERE t.id = :#{#nt.id} AND (t.author = :#{#nt.author} OR :root = true)")
	public int updateTask(
			@Param("nt") Task t,
			@Param("root") boolean canRoot
			);
	
	@Modifying
	@Query("UPDATE Task t SET "
			+ "t.assigner = (SELECT u FROM User u WHERE u.id = :idas) "
			+ "WHERE t.id = id")
	public int updateTaskAssigner(
			@Param("id") long id_task, 
			@Param("idas") long id_assigner);
	
	
	@Modifying
	@Query("DELETE Task t WHERE t.id = :id AND (t.author = :usr OR :root = true) ")
	public int dropTask(
			@Param("id") long id,
			@Param("usr") User usr,
			@Param("root") boolean canRoot
			);

	@Query("""
		SELECT t FROM Task t WHERE 
        (:root = true OR t.author = :usr) AND
        (:status IS NULL OR t.status IN :status) AND
        (:priority IS NULL OR t.priority IN :priority) AND
        (:assigneeIds IS NULL OR t.assigner.id IN :assigneeIds) AND
        (:authorIds IS NULL OR t.author.id IN :authorIds)
			""")
	public Collection<Task> getByFilter(
			@Param("status") List<TaskStatus> status,
	        @Param("priority") List<TaskPriority> priority,
	        @Param("assigneeIds") List<Long> assigneeIds,
	        @Param("authorIds") List<Long> authorIds,
	        @Param("usr") User usr,
	        @Param("root") boolean canRoot
			);
	
	
}
