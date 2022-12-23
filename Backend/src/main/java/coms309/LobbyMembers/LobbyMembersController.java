package coms309.LobbyMembers;

import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;

@Api(value = "LobbyMembersController", description = "Rest APIs related to LobbyMembers Entity")
@RestController
public class LobbyMembersController {

    @Autowired
    LobbyMembersRepository lobbyMembersRepository;

    @ApiOperation(value = "Increment lobby member's score and max score")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 401, message = "not authorized"),
            @ApiResponse(code = 403, message = "forbidden"),
            @ApiResponse(code = 404, message = "not found") })
    @PutMapping("/lobbymember/correct/{lobbyId}/{memberId}")
    public void incrementLobbyMemberScoreAndMaxScore(@ApiParam(value = "Lobby Id") @PathVariable String lobbyId,
                                                     @ApiParam(value = "Member Id") @PathVariable String memberId) {
        LobbyMembers lobbyMember = lobbyMembersRepository.findByLobbyIdAndMemberId(Integer.parseInt(lobbyId), Integer.parseInt(memberId));

        lobbyMember.setScore(lobbyMember.getScore() + 1);
        lobbyMember.setMaxScore(lobbyMember.getMaxScore() + 1);

        lobbyMembersRepository.save(lobbyMember);
    }

    @ApiOperation(value = "Increment lobby member's max score")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 401, message = "not authorized"),
            @ApiResponse(code = 403, message = "forbidden"),
            @ApiResponse(code = 404, message = "not found") })
    @PutMapping("/lobbymember/incorrect/{lobbyId}/{memberId}")
    public void incrementLobbyMemberMaxScore(@ApiParam(value = "Lobby Id") @PathVariable String lobbyId,
                                             @ApiParam(value = "Member Id") @PathVariable String memberId) {
        LobbyMembers lobbyMember = lobbyMembersRepository.findByLobbyIdAndMemberId(Integer.parseInt(lobbyId), Integer.parseInt(memberId));

        lobbyMember.setMaxScore(lobbyMember.getMaxScore() + 1);

        lobbyMembersRepository.save(lobbyMember);
    }

    @ApiOperation(value = "Returns lobby member's score")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 401, message = "not authorized"),
            @ApiResponse(code = 403, message = "forbidden"),
            @ApiResponse(code = 404, message = "not found") })
    @GetMapping("/lobbymember/score/{lobbyId}/{memberId}")
    public String getLobbyMembersScore(@ApiParam(value = "Lobby Id") @PathVariable String lobbyId,
                                       @ApiParam(value = "Member Id") @PathVariable String memberId) {
        LobbyMembers lobbyMember = lobbyMembersRepository.findByLobbyIdAndMemberId(Integer.parseInt(lobbyId), Integer.parseInt(memberId));

        return lobbyMember.getScore() + "/" + lobbyMember.getMaxScore();
    }
}
