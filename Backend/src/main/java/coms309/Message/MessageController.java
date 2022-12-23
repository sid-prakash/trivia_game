package coms309.Message;

import java.util.List;

import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "MessageController", description = "REST APIs related to Message Entity")
@RestController
public class MessageController {
	
	@Autowired
	MessageRepository messageRepository;

	@ApiOperation(value = "Get list of all Messages by username")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success|OK"),
			@ApiResponse(code = 401, message = "not authorized"),
			@ApiResponse(code = 403, message = "forbidden"),
			@ApiResponse(code = 404, message = "not found") })
	@GetMapping("/message/history/{username}")
	public List<Message> getMessageHistory(@ApiParam(value = "Name of User") @PathVariable("username") String username) {
		return messageRepository.getMessageHistory(username);
	}

	@ApiOperation(value = "Delete all Messages between two Users")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success|OK"),
			@ApiResponse(code = 401, message = "not authorized"),
			@ApiResponse(code = 403, message = "forbidden"),
			@ApiResponse(code = 404, message = "not found") })
	@DeleteMapping("/message/all/{participant1}/{participant2}")
	public void deleteAllActivity(@ApiParam(value = "Name of first User") @PathVariable("participant1") String p1,
								  @ApiParam(value = "Name of second User") @PathVariable("participant2") String p2) {
		messageRepository.deleteAllActivity(p1, p2);
	}

	@ApiOperation(value = "Delete Message by id")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success|OK"),
			@ApiResponse(code = 401, message = "not authorized"),
			@ApiResponse(code = 403, message = "forbidden"),
			@ApiResponse(code = 404, message = "not found") })
	@DeleteMapping("/message/{id}")
	public String deleteById(@ApiParam(value = "id of Message") @PathVariable("id") String id) {
		if (messageRepository.existsById(Long.parseLong(id))) {
			messageRepository.deleteById(Long.parseLong(id));
			return "success";
		}
		return "failure";
	}
}
