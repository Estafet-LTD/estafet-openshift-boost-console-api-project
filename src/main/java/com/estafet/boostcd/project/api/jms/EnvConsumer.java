package com.estafet.boostcd.project.api.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.estafet.boostcd.project.api.service.EnvironmentService;
import com.estafet.openshift.boost.messages.environments.Environments;

import io.opentracing.Tracer;

@Component
public class EnvConsumer {

	public final static String TOPIC = "environments.topic";

	@Autowired
	private Tracer tracer;
	
	@Autowired
	private EnvironmentService environmentService;

	@JmsListener(destination = TOPIC, containerFactory = "myFactory")
	public void onMessage(String message) {
		try {
			environmentService.updateEnv(Environments.fromJSON(message));
		} finally {
			if (tracer.activeSpan() != null) {
				tracer.activeSpan().close();
			}
		}
	}

}
