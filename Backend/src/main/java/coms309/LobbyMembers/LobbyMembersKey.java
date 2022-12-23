package coms309.LobbyMembers;

import coms309.Lobby.Lobby;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class LobbyMembersKey implements Serializable {
    @Column(name = "lobby_id")
    int lobbyId;

    @Column(name = "member_id")
    int memberId;

    public LobbyMembersKey(int lobbyId, int profileId) {
        this.lobbyId = lobbyId;
        this.memberId = profileId;
    }

    public LobbyMembersKey() {

    }


}
