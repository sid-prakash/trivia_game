package coms309.Message;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface MessageRepository extends JpaRepository<Message, Long> { 
	
	@Query(value = "SELECT * FROM message m WHERE m.receiver_name = :username or m.sender_name = :username ORDER BY m.sent", nativeQuery = true)
	List<Message> getMessageHistory(@Param("username") String username);
	
	@Modifying(clearAutomatically = true)
	@Transactional
	@Query(value = "DELETE FROM message WHERE receiver_name = :part1 and sender_name = :part2 or receiver_name = :part2 and sender_name = :part1", nativeQuery = true)
	void deleteAllActivity(@Param("part1") String participant1, @Param("part2") String participant2);
	
	boolean existsById(Long id);
	
	@Transactional
	void deleteById(Long id);
}
