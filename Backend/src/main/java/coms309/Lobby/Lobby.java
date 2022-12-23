package coms309.Lobby;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import coms309.LobbyMembers.LobbyMembers;

@Entity
@JsonIgnoreProperties( {"lobbyOf"} )
public class Lobby {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int lobbyId;

	private int hostId;

	private String lobbyName;
	
	@OneToMany(mappedBy = "lobby")
	public List<LobbyMembers> lobbyOf = new ArrayList<>();

	@ElementCollection(targetClass=Integer.class)
	private List<Integer> questionIds;

	public Lobby(int hostId, String lobbyName) {
		this.hostId = hostId;
		this.lobbyName = lobbyName;
		this.questionIds = new ArrayList<>();
	}
	
	public Lobby() {

	}

	public int getLobbyId() {
		return lobbyId;
	}

	public int getHostId() {
		return hostId;
	}
	
	public String getLobbyName() {
		return lobbyName;
	}

	public List<Integer> getQuestionIds() { return questionIds; }

	public void setQuestionIds(List<Integer> questionIds) {
		this.questionIds.addAll(questionIds);
	}
}
