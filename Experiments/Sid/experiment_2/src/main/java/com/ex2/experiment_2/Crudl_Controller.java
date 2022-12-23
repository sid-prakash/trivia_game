package com.ex2.experiment_2;

import java.util.ArrayList;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Crudl_Controller {

	ArrayList<String> players = new ArrayList<>();
	
	@GetMapping("/")
	public String getPlayers() {
		return "Leaderboards: " + players.toString();
	}
	
	@PostMapping("/add")
//	@RequestMapping(value = "/add", method = {RequestMethod.GET, RequestMethod.POST})
    public String addPlayer(@RequestParam(value="name", defaultValue="player") String player) {
        players.add(player);
        return "Leaderboards: " + players.toString();
	}
	
	@PostMapping("add/{player}")
//	@RequestMapping(value = "/add/{player}", method = {RequestMethod.GET, RequestMethod.POST})
    public String addPlayers(@PathVariable String player) {
        return addPlayer(player);
	}
	
	@PutMapping("update/{old}/{newName}") 
//	@RequestMapping(value = "/update/{old}/{newName}", method = {RequestMethod.GET, RequestMethod.PUT})
	public String updatePlayer(@PathVariable String old, @PathVariable String newName){
		for(int i = 0; i < players.size(); i++) {
			if(players.get(i).equals(old)) {
				players.set(i, newName);
				return "Leaderboards: " + players.toString();
			}
		}
		return "Invalid player name";
	}
	
	@DeleteMapping("delete/{player}")
//	@RequestMapping(value = "/delete/{player}", method = {RequestMethod.GET, RequestMethod.DELETE})
	public String deletePlayer(@PathVariable String player) {
		for(int i = 0; i < players.size(); i++) {
			if(players.get(i).equals(player)) {
				players.remove(i);
				return "Leaderboards: " + players.toString();
			}
		}
		return "Invalid player name";
	}
	
	@PostMapping("playerInfo")
//	@RequestMapping(value = "/playerInfo", method = {RequestMethod.GET, RequestMethod.POST})
	public String fromBody(@RequestBody Player player) {
		players.add(player.getName());
		return "Player name: " + player.getName() + "\nPlayer Score: " + player.getScore();
	}
}
