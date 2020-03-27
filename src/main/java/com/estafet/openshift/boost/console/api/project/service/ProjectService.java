package com.estafet.openshift.boost.console.api.project.service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.openshift.restclient.ClientBuilder;
import com.openshift.restclient.IClient;
import com.openshift.restclient.ResourceKind;
import com.openshift.restclient.capability.CapabilityVisitor;
import com.openshift.restclient.capability.resources.IBuildTriggerable;
import com.openshift.restclient.model.IBuild;
import com.openshift.restclient.model.IBuildConfig;
import com.openshift.restclient.model.IProject;

import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.tag.Tags;

import com.estafet.openshift.boost.console.api.project.model.Project;
import com.estafet.openshift.boost.console.api.project.model.ProjectRequest;
import com.estafet.openshift.boost.console.api.project.openshift.OpenShiftClient;
import com.estafet.openshift.boost.console.api.project.util.ENV;
import com.estafet.openshift.boost.messages.users.User;

@Service
public class ProjectService {

	@Autowired
	private OpenShiftClient client;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private Tracer tracer;
	


	public List<Project> getProjects() {
		List<Project> projects = new ArrayList<Project>();;
		List<IProject> iprojects = client.getProjects();
		System.out.println(iprojects);
		for (IProject iproject : iprojects) {
		    Project project = new Project();
		    project.setTitle(iproject.getDisplayName());
		    project.setNamespace(iproject.getNamespaceName());	
		    System.out.println("Project: " + iproject.getDisplayName() + "user url: " + ENV.USER_SERVICE_API + "/user/uid/" + iproject.getLabels().get("userId"));
		    User user = restTemplate.getForObject(ENV.USER_SERVICE_API + "/user/uid/" + iproject.getLabels().get("userId"), User.class);
		    project.setOwner(user.getName());
		    projects.add(project);
		}		
		return projects;
	}
	

	public String createProject(Project project) {
		User user = new User();
		user.setName(project.getOwner());
		user.setUid((restTemplate.getForObject(ENV.USER_SERVICE_API + "/user/name/" + project.getOwner() + "/", User.class).getUid()));
		String uid = user.getUid();
		System.out.println("User name: " + project.getOwner() + ", UID: " + uid);
		client.executeCreateEnviromentPipeline(project, uid);
		return "success";
	}
	
	public String deleteProject(Project project) {
		client.deleteProject(project);
		return "success";
	}

}
