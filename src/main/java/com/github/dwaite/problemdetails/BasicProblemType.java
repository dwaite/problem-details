package com.github.dwaite.problemdetails;

import java.net.URI;

public class BasicProblemType implements ProblemType {
	
	private final URI typeUri;
	private final String titleStr;
	private final int statusCode;
	
	public BasicProblemType(URI typeUri, int statusCode, String titleString) {
		this.typeUri = typeUri;
		this.titleStr = titleString;
		this.statusCode = statusCode;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public String getTitle() {
		return titleStr;
	}

	public URI getType() {
		return typeUri;
	}
	
	public ProblemDetails builder() {
		return ProblemDetails.ofType(this);
	}
	public static BasicProblemType forHttpStatus(int statusCode, String statusMessage) {
		return new BasicProblemType(BLANK_TYPE, statusCode, statusMessage);
	}
}