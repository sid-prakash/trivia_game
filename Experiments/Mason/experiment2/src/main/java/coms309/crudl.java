package coms309;

import java.util.ArrayList;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class crudl {
	ArrayList<String> names = new ArrayList<>();
	String[] deskLine = {"Mason", "Kyle", "Andrew"};
	Player user = new Player("John", 20);
	
	@PostMapping("/postName")
    public String postName(@RequestParam(value = "name", defaultValue = "") String name) {
        if (!name.equals("")) {
        	names.add(name);
        }
        
        String nameList = "";
        
        for (int i = 0; i < names.size(); ++i) {
        	nameList += names.get(i) + "\n";
        }
		
		return nameList;
    }
	
	@PostMapping("/postName/{name}")
	public String postNamePath(@PathVariable String name) {
		return postName(name);
	}
	
	@GetMapping("/getLineIndex")
	public int getLineIndex(@RequestParam(value = "name", defaultValue = "") String name) {
		for (int i = 0; i < deskLine.length; ++i) {
			if (deskLine[i].equals(name)) return i;
		}
			return -1;
	}
	
	@GetMapping("/getLineIndex/{name}")
	public int getLineIndexPath(@PathVariable String name) {
		return getLineIndex(name);
	}
	
	@PutMapping("/updatePlayer/{name}/{points}")
	public String updatePlayer(@PathVariable String name, @PathVariable String points) {
		int score = Integer.parseInt(points);
		
		if (user.getName().equals(name)) user.setScore(score);
		
		return user.getName() + " now has a score of " + user.getScore();
	}
	
	@DeleteMapping("/deleteName")
	public String deleteName(@RequestParam(value = "name", defaultValue = "") String name) {
		for (int i = 0; i < deskLine.length; ++i) {
			if (deskLine[i].equals(name)) deskLine[i] = "";
		}
		
		String nameList = "";
		
		for (int i = 0; i < deskLine.length; ++i) {
        	nameList += deskLine[i] + "\n";
        }
		
		return nameList;
	}
	
	@PostMapping("/postPlayer")
	public String postPlayer(@RequestBody Player player) {
		return "You sent a request body... Player name: " + player.getName() + " and Player score: " + player.getScore();
	}
}
