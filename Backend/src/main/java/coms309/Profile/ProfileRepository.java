package coms309.Profile;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
	Profile findById(int id);
	
	List<Profile> findByIsActiveTrue();
	
	boolean existsByUsername(String username);
	
	boolean existsById(int id);
	
	@Transactional
	void deleteById(int id);
	
	Profile findByUsername(String username);
}
