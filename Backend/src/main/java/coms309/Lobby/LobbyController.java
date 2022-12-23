package coms309.Lobby;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import coms309.Trivia.Trivia;
import coms309.Trivia.TriviaRepository;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "LobbyController", description = "REST APIs related to Lobby Entity")
@RestController
class LobbyController {
	
	@Autowired
	LobbyRepository lobbyRepository;

	@Autowired
	TriviaRepository triviaRepository;
	
	@ApiOperation(value = "Get list of all joinable Lobby")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success|OK"),
			@ApiResponse(code = 401, message = "not authorized"),
			@ApiResponse(code = 403, message = "forbidden"),
			@ApiResponse(code = 404, message = "not found") })
	@GetMapping("/lobby/all")
	public List<Lobby> getLobbys() {
		return LobbySocket.getLobbyNameLobbyIdMap()
				.values()
				.stream()
				.map(l -> lobbyRepository.findByLobbyId(l))
				.collect(Collectors.toList());
	}

	@ApiOperation(value = "Get string indicating if lobby name is available")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success|OK"),
			@ApiResponse(code = 401, message = "not authorized"),
			@ApiResponse(code = 403, message = "forbidden"),
			@ApiResponse(code = 404, message = "not found") })
	@GetMapping("/lobby/available/{lobbyName}")
	public String isAvaiblable(@ApiParam(value = "Name of Lobby") @PathVariable String lobbyName) {
		if (LobbySocket.getLobbyNameLobbyIdMap().containsKey(lobbyName)) {
			return "not available";
		} else {
			return "available";
		}
	}

	@ApiOperation(value = "Get lobby by Lobby's ID")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success|OK"),
			@ApiResponse(code = 401, message = "not authorized"),
			@ApiResponse(code = 403, message = "forbidden"),
			@ApiResponse(code = 404, message = "not found") })
	@GetMapping("/lobby/{lobbyId}")
	public Lobby getLobbyByLobbyId(@ApiParam(value = "Lobby ID") @PathVariable String lobbyId) {
		return lobbyRepository.findByLobbyId(Integer.parseInt(lobbyId));
	}

	@ApiOperation(value = "Get random Trivia Questions and save them")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success|OK"),
			@ApiResponse(code = 401, message = "not authorized"),
			@ApiResponse(code = 403, message = "forbidden"),
			@ApiResponse(code = 404, message = "not found") })
	@GetMapping("trivia/random/{num}/{lobbyId}")
	public List<Trivia> getRandomQuestion(
			@ApiParam(value = "number of question") @PathVariable String num,
			@ApiParam(value = "the lobby Id") @PathVariable String lobbyId)
	{
		int n = Integer.parseInt(num);
		Lobby lobby = lobbyRepository.findByLobbyId(Integer.parseInt(lobbyId));
		List<Integer> allId = triviaRepository.getAllIds();

		if(lobby == null) return null;

		if(n >= triviaRepository.count()) {
			lobby.setQuestionIds(allId);
			lobbyRepository.save(lobby);
			return triviaRepository.findAll();
		}

		HashMap<Integer, Trivia> randomQuestion = new HashMap<>();

		for(int i = 0; i < n; i++) {
			Random rand = new Random();
			int qId = allId.get(rand.nextInt(allId.size()));
			while(randomQuestion.containsKey(qId)) {
				qId = (int) allId.get(rand.nextInt(n));
			}
			randomQuestion.put(qId, triviaRepository.findById(qId));
		}
		List<Integer> questionIds = new ArrayList<>(randomQuestion.keySet());
		lobby.setQuestionIds(questionIds);
		lobbyRepository.save(lobby);
		List<Trivia> list = new ArrayList<Trivia>(randomQuestion.values());
		return list;
	}

	@ApiOperation(value = "Get random Trivia Questions by theme")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success|OK"),
			@ApiResponse(code = 401, message = "not authorized"),
			@ApiResponse(code = 403, message = "forbidden"),
			@ApiResponse(code = 404, message = "not found") })
	@GetMapping("trivia/random/{theme}/{num}/{lobbyId}")
	public List<Trivia> getRandomQuestionByTheme(
			@ApiParam(value = "theme of questions") @PathVariable String theme,
			@ApiParam(value = "number of question") @PathVariable String num,
			@ApiParam(value = "the lobby Id") @PathVariable String lobbyId)
	{
		int n = Integer.parseInt(num);
		Lobby lobby = lobbyRepository.findByLobbyId(Integer.parseInt(lobbyId));
		List<Integer> allId = triviaRepository.getAllIdsByQuestionTheme(theme);

		if(lobby == null) return null;

		if(n >= allId.size()) {
			lobby.setQuestionIds(allId);
			lobbyRepository.save(lobby);
			return triviaRepository.findAllByQuestionTheme(theme);
		}
		HashMap<Integer, Trivia> randomQuestion = new HashMap<>();

		for(int i = 0; i < n; i++) {
			Random rand = new Random();
			int qId = allId.get(rand.nextInt(allId.size()));
			while(randomQuestion.containsKey(qId)) {
				qId = (int) allId.get(rand.nextInt(n));
			}
			randomQuestion.put(qId, triviaRepository.findById(qId));
		}
		List<Integer> questionIds = new ArrayList<>(randomQuestion.keySet());
		lobby.setQuestionIds(questionIds);
		lobbyRepository.save(lobby);
		List<Trivia> list = new ArrayList<Trivia>(randomQuestion.values());
		return list;
	}
}
