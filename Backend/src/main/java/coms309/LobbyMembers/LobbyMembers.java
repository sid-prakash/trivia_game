package coms309.LobbyMembers;

import coms309.Lobby.Lobby;
import coms309.Profile.Profile;

import javax.persistence.*;

@Entity
public class LobbyMembers {

    @EmbeddedId
    private LobbyMembersKey id;

    @ManyToOne
    @MapsId("memberId")
    @JoinColumn(name = "member_id")
    private Profile member;

    @ManyToOne
    @MapsId("lobbyId")
    @JoinColumn(name = "lobby_id")
    private Lobby lobby;

    int score;

    int maxScore;

    public LobbyMembers(Profile member, Lobby lobby) {
        this.id = new LobbyMembersKey(lobby.getLobbyId(), member.getId());
        this.member = member;
        this.lobby = lobby;
        this.score = 0;
        this.maxScore = 0;
    }

    public LobbyMembers() {

    }

    public Profile getMember() {
        return member;
    }

    public Lobby getLobby() {
        return lobby;
    }

    public int getScore() {
        return score;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }
}
