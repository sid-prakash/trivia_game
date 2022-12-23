package coms309.Friends;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface FriendsRepository extends JpaRepository<Friends, Long> {

		Friends findById(FriendsKey id);
		
		boolean existsById(FriendsKey id);
		
		@Query(value = "Select * From friends f "
				+ "Where f.friend_id = :id and f.are_friends = 0",
				nativeQuery = true)
		List<Friends> findByFriendIdAndAreFriendsFalse(@Param("id") int id);
		
		@Query(value = "Select * From friends f "
				+ "Where (f.user_id = :id or f.friend_id = :id) and f.are_friends = 1", 
				nativeQuery = true)
		List<Friends> findByUserIdOrFriendIdAndAreFriendsTrue(@Param("id") int id);
		
		@Transactional
		void deleteById(FriendsKey id);
	
}
