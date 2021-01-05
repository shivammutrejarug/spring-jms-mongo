package com.codenotfound.routes;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;
// import static org.apache.activemq.camel.component.ActiveMQComponent.activeMQComponent;

@Component
public class CamelRoutes extends RouteBuilder{

	@Override
	public void configure() throws Exception {
        from("direct:bar")
            .to("activemq:helloworld.q");
	}

}
