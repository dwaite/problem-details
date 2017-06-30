package com.github.dwaite.problemdetails;

import java.net.URI;
import java.sql.Date;
import java.time.Instant;
import java.util.function.Consumer;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.validation.constraints.NotNull;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.StatusType;
import javax.ws.rs.core.Response.Status.Family;


public class ProblemDetails {
	private static final class StatusTypeImpl implements StatusType {
		private final int statusCode;
		private final JsonObject obj;

		public StatusTypeImpl(int statusCode, JsonObject obj) {
			this.statusCode = statusCode;
			this.obj = obj;
		}

		@Override
		public int getStatusCode() {
			return statusCode;
		}

		@Override
		public String getReasonPhrase() {
			return obj.getString("title");
		}

		@Override
		public Family getFamily() {
			return Family.familyOf(statusCode);
		}
	}

	private JsonObjectBuilder builder;
	
	public ProblemDetails(@NotNull URI typeUri, int statusCode) {
		builder = Json.createObjectBuilder();
		if (!typeUri.equals(ProblemType.BLANK_TYPE))
			builder.add("type", typeUri.toString());
		builder.add("status", statusCode);
	}

	public static @NotNull ProblemDetails ofType(@NotNull URI uri, int statusCode) {
		return new ProblemDetails(uri, statusCode);
	}

	public static @NotNull ProblemDetails ofType(@NotNull ProblemType type) {
		return new ProblemDetails(type.getType(), type.getStatusCode())
				.title(type.getTitle());
	}

	public @NotNull ProblemDetails title(String title) {
		builder.add("title", title);
		return this;
	}

	public @NotNull ProblemDetails status(@NotNull int statusCode) {
		if (statusCode != 0) {
			builder.add("status", statusCode);
		}
		return this;
	}
	
	public @NotNull ProblemDetails detail(@NotNull String details) {
		builder.add("detail", details);
		return this;
	}
	
	public @NotNull ProblemDetails instance(@NotNull URI instance) {
		builder.add("instance", instance.toString());
		return this;
	}

	public @NotNull ProblemDetails customAttributes(@NotNull Consumer<JsonObjectBuilder> consumer) {
		consumer.accept(builder);
		return this;
	}
	
	public ResponseBuilder builder() {
		JsonObject obj = builder.build();
		StatusType status;
		try {
			int statusCode = obj.getInt("status");
			status = Status.fromStatusCode(statusCode);
			if (status == null) {
				status = new StatusTypeImpl(statusCode, obj);
			}
		}
		catch (NullPointerException npe) {
			throw new IllegalStateException("HTTP Problem does not have a status code");
		}
		return Response
				.status(status)
				.expires(Date.from(Instant.EPOCH))
				.cacheControl(CacheControl.valueOf("no-store, must-revalidate"))
				.entity(obj);
	}

	public Response build() {
		return builder().build();
	}
}
