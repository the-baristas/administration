package com.utopia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@SpringBootApplication
public class AdminApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdminApplication.class, args);
	}
	
	@RestController 
	public class HomeContoller {
	    @RequestMapping("/")
	    public ModelAndView index() {
	        ModelAndView modelAndView = new ModelAndView();
	        modelAndView.setViewName("index");
	        return modelAndView;
	    }
	    
	    @RequestMapping("/done")
	    public ModelAndView done() {
	        ModelAndView modelAndView = new ModelAndView();
	        modelAndView.setViewName("done");
	        return modelAndView;
	    }
	}

}
