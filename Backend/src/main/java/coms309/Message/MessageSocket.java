package coms309.Message;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import coms309.Lobby.LobbySocket;

@Controller
@ServerEndpoint(value = "/message/{userName}")
public class MessageSocket {
	private static MessageRepository msgRepo;
	
	@Autowired
	public void setMessageRepository(MessageRepository repo) {
		msgRepo = repo;
	}
	
	private static Map<Session, String> sessionUsernameMap = new Hashtable<>();
	private static Map<String, Session> usernameSessionMap = new Hashtable<>();
	
	private final Logger logger = LoggerFactory.getLogger(LobbySocket.class);
	
	@OnOpen
	public void onOpen(Session session, @PathParam("userName") String userName) {
		logger.info("Entered into Open");
		
		sessionUsernameMap.put(session, userName);
		usernameSessionMap.put(userName, session);
	}
	
	@OnMessage
	public void onMessage(Session session, String message) throws IOException {
		logger.info("Entered into Message: Got Message " + message);
		String username = sessionUsernameMap.get(session);
		String receiverName = null;
		String content = null;
		
		if (message.startsWith("@")) {
			receiverName = message.split(" ")[0].substring(1);
			content = message.substring(receiverName.length() + 2);
			
			if (usernameSessionMap.containsKey(receiverName)) {
				logger.info("Entered into contains user");
				sendMessageToParticularUser(receiverName, username + ": " + content);
			}	
		}	
		
		msgRepo.save(new Message(username, receiverName, username + ": " + content));
	}
	
	@OnClose
	public void onClose(Session session) {
		logger.info("Entered into Close");
		
		usernameSessionMap.remove(sessionUsernameMap.get(session));
		sessionUsernameMap.remove(session);
	}
	
	private void sendMessageToParticularUser(String username, String message) {
		try {
			usernameSessionMap.get(username).getBasicRemote().sendText(message);
		} catch(IOException e) {
			logger.info("Exception: " + e.getMessage().toString());
			e.printStackTrace();
		}
	}
}
