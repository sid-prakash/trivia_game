package coms309.Friends;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class FriendsKey implements Serializable {

	@Column(name = "user_id")
	int userId;
	
	@Column(name = "friend_id")
	int friendId;
	
	public FriendsKey(int userId, int friendId) {
		this.userId = userId;
		this.friendId = friendId;
	}
	
	public FriendsKey() {
		
	}
	
	public FriendsKey reverse() {
		int temp = this.userId;
		this.userId = this.friendId;
		this.friendId = temp;
		
		return this;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		
		if (obj.getClass() != this.getClass()) {
			return false;
		}
		
		final FriendsKey other = (FriendsKey) obj;
		if (this.userId != other.userId) {
			return false;
		}
		
		if (this.friendId != other.friendId) {
			return false;
		}
		
		return true;
	}
}
