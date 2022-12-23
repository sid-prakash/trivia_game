package coms309;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
class WelcomeController {

    @GetMapping("/")
    public String welcome() {
        return "This should work now... Oh... Hello! Add /Testing";
    }
    
    @GetMapping("/{anything}")
    public String welcome(@PathVariable String anything) {
    	// return different(); //Interesting stuff, but expected 
    	if (anything.equals("Testing")) return "You did a test";
        return "Hello! " + anything;
    }
    
    @GetMapping("/strange")
    public String different() {
    	return "////||||\\\\ \n";
    }

}
