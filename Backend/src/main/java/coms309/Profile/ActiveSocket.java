package coms309.Profile;

import java.io.IOException;

import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
@ServerEndpoint(value = "/profile/{username}")
public class ActiveSocket {
	
	private static ProfileRepository profileRepository;
	
	@Autowired
	public void setProfileRepository(ProfileRepository repo) {
		profileRepository = repo;
	}
	
	private final Logger logger = LoggerFactory.getLogger(ActiveSocket.class);
	
	@OnOpen
	public void onOpen(@PathParam("username") String username) throws IOException {
		logger.info("Entered into Open");
		
		Profile activeUser = profileRepository.findByUsername(username);
		activeUser.setIsActive(true);
		profileRepository.save(activeUser);
	}
	
	@OnClose
	public void onClose(@PathParam("username") String username) throws IOException {
		logger.info("Entered into Close");
		
		Profile activeUser = profileRepository.findByUsername(username);
		activeUser.setIsActive(false);
		profileRepository.save(activeUser);
	}
	
	@OnError
	public void onError(Session session, Throwable throwable) {
		logger.info("Entered into Error");
		throwable.printStackTrace();
	}
}
