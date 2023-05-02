package io.anuragksinha.springbootconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {
	@Autowired
	private DBSettings dbSettings;

	@GetMapping("/greeting")
	public String geetings(){
		return dbSettings.getHost()+dbSettings.getConnection();
	}


}
