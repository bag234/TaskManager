package org.mrbag.test.TaskManager.Repository;

import org.mrbag.test.TaskManager.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRep extends JpaRepository<User, Long> {
	
	/***
	 * Было выбрано не использовать для реализация JPQL, а доверить ядру spring-data-jpa генерацию запросов на основе контекста наименования метода 
	 * @param email - unique count, that means result was single 
	 * @return {@link User} or Null if not found
	 */
	public User findOneByEmail(String email);
	
	public User findOneByEmailAndPassword(String email, String password);
	
	public User findOneById(long id);
	
}
