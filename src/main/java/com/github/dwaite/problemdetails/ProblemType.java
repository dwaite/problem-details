package com.github.dwaite.problemdetails;

import java.net.URI;

import javax.ws.rs.core.Response.StatusType;

public interface ProblemType {
	public static URI BLANK_TYPE = URI.create("about:blank");
	
	public int getStatusCode();

	public String getTitle();

	public URI getType();
	
	public ProblemDetails builder();
	
	public static ProblemType forHttpStatus(int statusCode, String statusMessage) {
		return new BasicProblemType(URI.create("about:blank"), statusCode, statusMessage);
	}

	public static ProblemType forHttpStatus(StatusType status) {
		return new BasicProblemType(
				URI.create("about:blank"), 
				status.getStatusCode(), 
				status.getReasonPhrase());
	}
}