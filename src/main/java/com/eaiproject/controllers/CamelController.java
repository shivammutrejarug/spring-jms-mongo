package com.eaiproject.controllers;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
// import static org.apache.activemq.camel.component.ActiveMQComponent.activeMQComponent;

@RestController
public class CamelController {
	
	@Autowired
	CamelContext camelContext;
	
	@Autowired
	ProducerTemplate producerTemplate;
	
	@RequestMapping(value = "/call")
	public void startCamel() {
		producerTemplate.sendBody("direct:bar", "Calling via Spring Boot Rest Controller");
	}
}
