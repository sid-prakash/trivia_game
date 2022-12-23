package coms309.Lobby;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import java.util.stream.Collectors;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import coms309.LobbyMembers.LobbyMembers;
import coms309.LobbyMembers.LobbyMembersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import coms309.Profile.Profile;
import coms309.Profile.ProfileRepository;

@Controller
@ServerEndpoint(value = "/lobby/{username}/{lobbyName}")
public class LobbySocket {
	private static LobbyRepository lbyRepo;
	
	@Autowired
	public void setLobbyRepository(LobbyRepository repo) {
		lbyRepo = repo;
	}
	
	private static ProfileRepository pflRepo;
	
	@Autowired
	public void setProfileRepository(ProfileRepository repo) {
		pflRepo = repo;
	}

	private static LobbyMembersRepository lmbrRepo;

	@Autowired
	public void setLobbyMembersRepository(LobbyMembersRepository repo) {
		lmbrRepo = repo;
	}
	
	private static Map<Session, String> sessionUsernameMap = new Hashtable<>();
	private static Map<String, Session> usernameSessionMap = new Hashtable<>();
	private static Map<Session, Integer> sessionHostIdMap = new Hashtable<>();
	private static Map<String, Integer> lobbyNameHostIdMap = new Hashtable<>();
	private static Map<String, Integer> lobbyNameLobbyIdMap = new Hashtable<>();

	private final Logger logger = LoggerFactory.getLogger(LobbySocket.class);
	
	@OnOpen
	public void onOpen(Session session, @PathParam("lobbyName") String lobbyName, @PathParam("username") String username) {
		logger.info("Entered into Open");
		
		sessionUsernameMap.put(session, username);
		usernameSessionMap.put(username, session);

		Profile pfl = pflRepo.findByUsername(username);
		Lobby lby;

		if (lobbyNameHostIdMap.containsKey(lobbyName)) {
			sessionHostIdMap.put(session, lobbyNameHostIdMap.get(lobbyName));
			lby = lbyRepo.findByLobbyId(lobbyNameLobbyIdMap.get(lobbyName));
			lmbrRepo.saveAndFlush(new LobbyMembers(pfl, lby));
			lbyRepo.saveAndFlush(lby);
		} else {
			lby = new Lobby(pfl.getId(), lobbyName);
			lbyRepo.saveAndFlush(lby);
			lobbyNameHostIdMap.put(lobbyName, pfl.getId());
			lobbyNameLobbyIdMap.put(lobbyName, lby.getLobbyId());
			sessionHostIdMap.put(session, pfl.getId());
		}

		sendMessageToParticularUser(session, "Lobby Host: " + pflRepo.findById(lobbyNameHostIdMap.get(lobbyName)).getUsername());
		sendMessageToParticularUser(session, "Lobby Id: " + lby.getLobbyId());
		sendMessageToParticularUser(session, "Lobby Members: " + lmbrRepo.findByLobbyId(lby.getLobbyId()).stream()
				.map(lm -> lm.getMember().getUsername())
				.collect(Collectors.joining(",")));
		broadcast(sessionHostIdMap.get(session), pfl.getUsername() + ": joined the lobby!");
	}

	@OnMessage
	public void onMessage(Session session, String message, @PathParam("lobbyName") String lobbyName) throws IOException {
		logger.info("Entered into Message: Got Message: " + message);
		String username = sessionUsernameMap.get(session);

		if (!lobbyNameLobbyIdMap.containsKey(lobbyName)) {
			sendMessageToParticularUser(session, "Lobby session has ended, please disconnect!");
			return;
		}

		Lobby lby = lbyRepo.findByLobbyId(lobbyNameLobbyIdMap.get(lobbyName));
		Profile pfl = pflRepo.findByUsername(username);

		if (message.equals("/members")) {
			sendMessageToParticularUser(session, "Lobby Members: " + lmbrRepo.findByLobbyId(lby.getLobbyId()).stream()
					.map(lm -> lm.getMember().getUsername())
					.collect(Collectors.joining(",")));
		} else {
			broadcast(lby.getHostId(), pfl.getUsername() + ": " + message);
		}
	}
	
	@OnClose
	public void onClose(Session session, @PathParam("lobbyName") String lobbyName) throws IOException {
		logger.info("Entered into Close");

		String username = sessionUsernameMap.get(session);
		Profile pfl = pflRepo.findByUsername(username);
		int hostId = sessionHostIdMap.get(session);

		if (lobbyNameHostIdMap.containsKey(lobbyName) && lobbyNameHostIdMap.get(lobbyName) == pfl.getId()) {
			lobbyNameHostIdMap.remove(lobbyName);
			lobbyNameLobbyIdMap.remove(lobbyName);
		}
		usernameSessionMap.remove(username);
		sessionUsernameMap.remove(session);
		sessionHostIdMap.remove(session);

		broadcast(hostId, pfl.getUsername() + ": disconnected!");
	}
	
	@OnError
	public void onError(Throwable throwable) {
		logger.info("Entered into Error");
		throwable.printStackTrace();
	}

	private void broadcast(int hostId, String message) {
		sessionHostIdMap.forEach((session, integer) -> {
			if (hostId == integer.intValue()) {
				try {
					session.getBasicRemote().sendText(message);
				} catch (IOException e) {
					logger.info("Exception: " + e.getMessage().toString());
					e.printStackTrace();
				}
			}
		});
	}
	
	private void sendMessageToParticularUser(Session session, String message) {
		try {
			session.getBasicRemote().sendText(message);
		} catch(IOException e) {
			logger.info("Exception: " + e.getMessage().toString());
			e.printStackTrace();
		}
	}

	public static Map<String, Integer> getLobbyNameLobbyIdMap() {
		return lobbyNameLobbyIdMap;
	}
}	

