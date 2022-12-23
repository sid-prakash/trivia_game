package coms309.Friends;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import coms309.Profile.Profile;

@Entity
public class Friends {
	
	@EmbeddedId
	private FriendsKey id;
	
	@ManyToOne
	@MapsId("userId")
	@JoinColumn(name = "user_id")
	private Profile user;
	
	@ManyToOne
	@MapsId("friendId")
	@JoinColumn(name = "friend_id")
	private Profile friend;
	
	private boolean areFriends;
	
	public Friends(Profile user, Profile friend) {
		this.id = new FriendsKey(user.getId(), friend.getId());
		this.user = user;
		this.friend = friend;
	}
	
	public Friends() {
		
	}
	
	public Profile getUnMatchingProfile(int toMatch) {
		if (user.getId() == toMatch) {
			return this.friend;
		} else
			return this.user;
	}
	
	public void setAreFriends() {
		this.areFriends = true;
	}
	
	public boolean areFriends() {
		return this.areFriends;
	}
	
	public Profile getFriend() {
		return this.friend;
	}
	
	public Profile getUser() {
		return this.user;
	}
}
