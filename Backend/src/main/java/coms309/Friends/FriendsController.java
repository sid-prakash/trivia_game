package coms309.Friends;

import java.util.List;
import java.util.stream.Collectors;

import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import coms309.Profile.Profile;
import coms309.Profile.ProfileRepository;
import org.springframework.web.server.ResponseStatusException;

@Api(value = "FriendsController", description = "REST APIs related to Friends Entity")
@RestController
public class FriendsController {
	
	@Autowired
	FriendsRepository friendsRepository;
	
	@Autowired
	ProfileRepository profileRepository;

	@ApiOperation(value = "Create Friend request or creates Friendship if reciprocated")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success|OK"),
			@ApiResponse(code = 401, message = "not authorized"),
			@ApiResponse(code = 403, message = "forbidden"),
			@ApiResponse(code = 404, message = "not found") })
	@PostMapping("/friends/{userId}/{friendUsername}")
	public void createFriends(@ApiParam(value = "id of User") @PathVariable String userId,
								@ApiParam(value = "Name of potential Friend")@PathVariable String friendUsername) {
		Profile user = profileRepository.findById(Integer.parseInt(userId));
		Profile friend = profileRepository.findByUsername(friendUsername);

		if (user.getId() == friend.getId()) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Cannot Friend Self!");
		}
		
		FriendsKey id = new FriendsKey(friend.getId(), user.getId());
		
		if (friendsRepository.existsById(id)) {
			Friends friends = friendsRepository.findById(id);
			friends.setAreFriends();
			friendsRepository.save(friends);
		} else if (!friendsRepository.existsById(id.reverse())) {
			friendsRepository.save(new Friends(user, friend));
		}
	}

	@ApiOperation(value = "Get list of all Friends for specific User")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success|OK"),
			@ApiResponse(code = 401, message = "not authorized"),
			@ApiResponse(code = 403, message = "forbidden"),
			@ApiResponse(code = 404, message = "not found") })
	@GetMapping("/friends/{userId}")
	public List<Profile> getFriends(@ApiParam(value = "id of User") @PathVariable String userId) {
		int id = Integer.parseInt(userId);
		
		return friendsRepository.findByUserIdOrFriendIdAndAreFriendsTrue(id)
			.stream().map(f -> f.getUnMatchingProfile(id))
			.collect(Collectors.toList());
	}

	@ApiOperation(value = "Get list of Friend requests for specific User")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success|OK"),
			@ApiResponse(code = 401, message = "not authorized"),
			@ApiResponse(code = 403, message = "forbidden"),
			@ApiResponse(code = 404, message = "not found") })
	@GetMapping("/friends/requests/{userId}")
	public List<Profile> getFriendsRequests(@ApiParam("id of User") @PathVariable String userId) {
		int id = Integer.parseInt(userId);
		
		return friendsRepository.findByFriendIdAndAreFriendsFalse(id)
			.stream().map(f-> f.getUser())
			.collect(Collectors.toList());
	}

	@ApiOperation(value = "Delete Friendship between two Users")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success|OK"),
			@ApiResponse(code = 401, message = "not authorized"),
			@ApiResponse(code = 403, message = "forbidden"),
			@ApiResponse(code = 404, message = "not found") })
	@DeleteMapping("/friends/{userId}/{friendId}")
	public void deleteFriends(@ApiParam(value = "id of first User") @PathVariable String userId,
								@ApiParam(value = "id of second User") @PathVariable String friendId) {
		FriendsKey id = new FriendsKey(Integer.parseInt(userId), Integer.parseInt(friendId));

		friendsRepository.deleteById(id);
		friendsRepository.deleteById(id.reverse());
	}
	
}

