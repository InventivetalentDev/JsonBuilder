# JsonBuilder

[![Build Status](http://ci.inventivetalent.org/job/JsonBuilder/badge/icon)](https://ci.inventivetalent.org/job/JsonBuilder)

Simple builder for Gson objects &amp; arrays. Based on Gson's [JsonWriter](https://google.github.io/gson/apidocs/com/google/gson/stream/JsonWriter.html).


# Examples

## Object
```
JsonObject jsonObject = JsonBuilder
		.object()
			.name("foo")
			.value("Bar")
			.beginObject()
				.name("foo-1")
				.value("Bar 1")
			.endObject()
		.buildObject();
```

## Array
```
JsonArray jsonArray = JsonBuilder
		.array()
			.value("foo")
			.value("bar")
			.beginArray()
				.value("foo-1")
				.value("bar-1")
			.endArray()
		.buildArray();
```


# Maven
```
<repositories>
	<repository>
		<id>inventive-repo</id>
		<url>https://repo.inventivetalent.org/content/groups/public/</url>
	</repository>
</repositories>

<dependencies>
	<dependency>
		<groupId>org.inventivetalent</groupId>
		<artifactId>jsonbuilder</artifactId>
		<version>1.0.0</version>
	</dependency>
</dependencies>
```
