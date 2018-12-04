package io.norwa.demo.spring4aspectlogging.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Slf4j
@RestController
public class TestController {

	@RequestMapping(value = "/test",method = RequestMethod.GET)
	public String test(){
		log.info("test");
		return "OK:" + LocalDateTime.now();
	}
}
