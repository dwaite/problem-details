Problem Details for JAX-RS
==========================

This is a simple builder pattern for generating [RFC 7807 Problem Details](https://tools.ietf.org/html/rfc7807) in JAX-RS applications. This library currently only supports JSON output.

Example Usage
-------------

```java
return ProblemDetails.ofType(ProblemType.forHttpStatus(Status.CONFLICT))
        .detail("existing record has been invalidated")
        .customAttributes((builder) ->
        builder.add("record_id", id)
        builder.add("invalidation_time", invalidatedAt.toString()))
        .build();
```

will result in a response of

```http
HTTP/1.1 409 Conflict
Content-Type: application/json
Cache-Control: no-store, must-revalidate
Expires: Thu, 01 Jan 1970 00:00:00 GMT

{
    "status" : 409,
    "title" : "Conflict",
    "details" : "existing record has been invalidated",
    "record_id" : 1007,
    "invalidation_time" : "Fri, 30 Jun 2017 16:11:09 GMT"
}
```
