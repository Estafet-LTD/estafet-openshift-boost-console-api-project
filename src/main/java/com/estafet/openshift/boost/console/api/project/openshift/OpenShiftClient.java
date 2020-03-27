package com.estafet.openshift.boost.console.api.project.openshift;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.estafet.openshift.boost.commons.lib.env.ENV;
import com.estafet.openshift.boost.console.api.project.model.Project;
import com.openshift.restclient.ClientBuilder;
import com.openshift.restclient.IClient;
import com.openshift.restclient.ResourceKind;
import com.openshift.restclient.capability.CapabilityVisitor;
import com.openshift.restclient.capability.resources.IBuildTriggerable;
import com.openshift.restclient.model.IBuild;
import com.openshift.restclient.model.IBuildConfig;
import com.openshift.restclient.model.IProject;
import com.openshift.restclient.model.IResource;
import com.openshift.restclient.model.user.IUser;

import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.tag.Tags;

@Component
public final class OpenShiftClient {

	@Autowired
	private Tracer tracer;
	
	private String product;

	private IClient getClient() {
		product = System.getenv("PRODUCT");
		return new ClientBuilder("https://" + System.getenv("OPENSHIFT_HOST_PORT"))
				.withUserName(System.getenv("OPENSHIFT_USER"))
				.withPassword(System.getenv("OPENSHIFT_PASSWORD"))
				.build();
	}

//	@SuppressWarnings("unchecked")
//	public List<IUser> getUsers() {
//		Span span = tracer.buildSpan("OpenShiftClient.getUsers").start();
//		try {
//			return (List<IUser>) getClient().get(ResourceKind.USER, product + "-cicd");
//		} catch (RuntimeException e) {
//			throw handleException(span, e);
//		} finally {
//			span.finish();
//		}
//	}
	
	@SuppressWarnings("unchecked")
	public List<IProject> getProjects() {
		Span span = tracer.buildSpan("OpenShiftClient.getUsers").start();
		try {		 
			Map<String, String> labels = new HashMap<String, String>();
			labels.put("type", product + "-dq");
			return (List<IProject>)(List<?>) (List<IResource>) getClient().list(ResourceKind.PROJECT, labels);
		} catch (RuntimeException e) {
			throw handleException(span, e);
		} finally {
			span.finish();
		}
	}
	
	@SuppressWarnings("deprecation")
	public void executeCreateEnviromentPipeline(Project project, String uid) {
		System.out.println("In executeCreateEnviromentPipeline");

		Span span = tracer.buildSpan("OpenShiftClient.getCreateEnviromentPipeline").start();
		System.out.println("Created span");
		try {
			executePipeline((IBuildConfig) getClient().get(ResourceKind.BUILD_CONFIG, "create-enviroment", ENV.PRODUCT + "-cicd"), project, uid);
		} catch (RuntimeException e) {
			throw handleException(span, e);
		} finally {
			span.finish();
		}
	}
	
	
	private void executePipeline(IBuildConfig pipeline, Project project, String uid) {
		System.out.println("In executePipeline");
		pipeline.accept(new CapabilityVisitor<IBuildTriggerable, IBuild>() {
            @Override
            public IBuild visit(IBuildTriggerable capability) {
            	capability.setEnvironmentVariable("USER_NAME", project.getOwner());
            	capability.setEnvironmentVariable("USER_ID", uid);
                return capability.trigger();
            }
        }, null);
	}

	private RuntimeException handleException(Span span, RuntimeException e) {
		Tags.ERROR.set(span, true);
		Map<String, Object> logs = new HashMap<String, Object>();
		logs.put("event", "error");
		logs.put("error.object", e);
		logs.put("message", e.getMessage());
		StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
		logs.put("stack", sw.toString());
		span.log(logs);
		return e;
	}
	
}
