package coms309.Profile;

import java.util.List;

import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Api(value = "TriviaController", description = "REST APIs related to Profile Entity")
@RestController
class ProfileController {

	@Autowired
	ProfileRepository profileRepository;

	@ApiOperation(value = "Creating a new profile")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success|OK"),
			@ApiResponse(code = 401, message = "not authorized"),
			@ApiResponse(code = 403, message = "forbidden"),
			@ApiResponse(code = 404, message = "not found") })
	@PostMapping("/profile")
	public String createProfile(
			@ApiParam(value = "Profile to be created") @RequestBody Profile user)
	{
		if (user.getUsername() == null || user.getPassword() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Need username and password");
		} else if (profileRepository.existsByUsername(user.getUsername())) {
			throw new ResponseStatusException(HttpStatus.ALREADY_REPORTED, "User already exists");
		}
		user.setDisplayname(user.getUsername());
		profileRepository.save(user);

		return "success";
	}

	@ApiOperation(value = "Get all active profiles")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success|OK"),
			@ApiResponse(code = 401, message = "not authorized"),
			@ApiResponse(code = 403, message = "forbidden"),
			@ApiResponse(code = 404, message = "not found") })
	@GetMapping("/profile/active")
	public List<Profile> getActiveProfiles() {
		return profileRepository.findByIsActiveTrue();
	}

	@ApiOperation(value = "Get profile by id")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success|OK"),
			@ApiResponse(code = 401, message = "not authorized"),
			@ApiResponse(code = 403, message = "forbidden"),
			@ApiResponse(code = 404, message = "not found") })
	@GetMapping("/profile/{id}")
	public Profile getProfileById(
			@ApiParam(value = "id of profile") @PathVariable String id)
	{
		if (profileRepository.existsById(Integer.parseInt(id))) {
			return profileRepository.findById(Integer.parseInt(id));
		} else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id not found");
		}
	}

	@ApiOperation(value = "Get profile given the correct credentials")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success|OK"),
			@ApiResponse(code = 401, message = "not authorized"),
			@ApiResponse(code = 403, message = "forbidden"),
			@ApiResponse(code = 404, message = "not found") })
	@GetMapping("/profile/{username}/{password}")
	public Profile getProfile(
			@ApiParam(value = "username of Profile") @PathVariable String username,
			@ApiParam(value = "password of Profile") @PathVariable String password)
	{
		Profile user = profileRepository.findByUsername(username);
		if (user.getPassword().equals(password)) {
			profileRepository.save(user);
			return user;
		} else {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Profile not found");
		}
	}

	@ApiOperation(value = "Updating a profile")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success|OK"),
			@ApiResponse(code = 401, message = "not authorized"),
			@ApiResponse(code = 403, message = "forbidden"),
			@ApiResponse(code = 404, message = "not found") })
	@PutMapping("/profile/{id}")
	public String putProfile(
			@ApiParam(value = "id of Profile") @PathVariable String id,
			@ApiParam(value = "Updated Profile") @RequestBody Profile request)
	{
		if(request == null || !profileRepository.existsById(Integer.parseInt(id))) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id not found");
		}
		Profile user = profileRepository.findById(Integer.parseInt(id));
		user.updateProfile(request);
		profileRepository.save(user);
		return "success";
	}

	@ApiOperation(value = "Deleting a profile")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success|OK"),
			@ApiResponse(code = 401, message = "not authorized"),
			@ApiResponse(code = 403, message = "forbidden"),
			@ApiResponse(code = 404, message = "not found") })
	@DeleteMapping("/profile/{id}")
	public String deleteProfile(
			@ApiParam(value = "id of Profile") @PathVariable String id)
	{
		if (profileRepository.existsById(Integer.parseInt(id))) {
			profileRepository.deleteById(Integer.parseInt(id));
			return "success";
		} else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id not found");
		}
	}

}