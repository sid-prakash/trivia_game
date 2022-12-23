package coms309.LobbyMembers;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LobbyMembersRepository extends JpaRepository<LobbyMembers, Long> {

    @Query(value = "Select * From lobby_members l Where l.lobby_id = :id", nativeQuery = true)
    List<LobbyMembers> findByLobbyId(@Param("id") int id);

    @Query(value = "Select * From lobby_members l Where l.lobby_id = :lid and l.member_id = :mid", nativeQuery = true)
    LobbyMembers findByLobbyIdAndMemberId(@Param("lid") int lid, @Param("mid") int mid);
}
