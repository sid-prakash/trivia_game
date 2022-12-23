package coms309.Trivia;

import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

@Api(value = "TriviaController", description = "REST APIs related to Trivia Entity")
@RestController
class TriviaController {

    @Autowired
    TriviaRepository triviaRepository;

    @ApiOperation(value = "Creating a trivia question")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 401, message = "not authorized"),
            @ApiResponse(code = 403, message = "forbidden"),
            @ApiResponse(code = 404, message = "not found") })
    @PostMapping("/trivia")
    public String createTriviaQuestion(
            @ApiParam(value = "Trivia Question to be created") @RequestBody Trivia trivia )
    {
        if(trivia.getQuestion() == null || trivia.getAnswer() == null || trivia.getQuestionType() == 0) {
            return "failure";
        }
        else if (triviaRepository.existsByQuestion(trivia.getQuestion())) {
            return "exists";
        }
        if(trivia.getQuestionTheme() == null) {
            trivia.setQuestionTheme("general");
        }
        triviaRepository.save(trivia);
        return "success";
    }

    @ApiOperation(value = "Get a trivia question by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 401, message = "not authorized"),
            @ApiResponse(code = 403, message = "forbidden"),
            @ApiResponse(code = 404, message = "not found") })
    @GetMapping("/trivia/{id}")
    public Trivia getTriviaById(
            @ApiParam(value = "Id of the Trivia question") @PathVariable String id)
    {
        if(triviaRepository.existsById(Integer.parseInt(id))) {
            return triviaRepository.findById(Integer.parseInt(id));
        }
        return null;
    }

    @ApiOperation(value = "Update a trivia question")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 401, message = "not authorized"),
            @ApiResponse(code = 403, message = "forbidden"),
            @ApiResponse(code = 404, message = "not found") })
    @PutMapping("/trivia/{id}")
    public String putTrivia(
            @ApiParam(value = "Id of the Trivia question") @PathVariable String id,
            @ApiParam(value = "Updated Trivia question") @RequestBody Trivia request)
    {
        Trivia user = triviaRepository.findById(Integer.parseInt(id));

        if(request == null) {
            return "failure";
        }

        user.updateTrivia(request);
        triviaRepository.save(user);

        return "success";
    }

}
