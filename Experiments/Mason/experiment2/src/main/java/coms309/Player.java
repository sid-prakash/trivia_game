package coms309;

public class Player {
	String name;
	int score;
	
	Player(String name, int score) {
		this.name = name;
		this.score = score;
	}
	
    
    public String getName() {
    	return name;
    }
    
    public int getScore() {
    	return score;
    }
    
    public void setScore(int score) {
    	this.score = score;
    }
}
