package org.inventivetalent.gson;

import com.google.gson.*;
import com.google.gson.stream.JsonWriter;

/**
 * Builder for Gson objects and arrays, based on the method structure of {@link JsonWriter}
 */
public class JsonBuilder {

	protected NestedJsonElement currentElement;

	JsonBuilder(JsonElement base) {
		this.currentElement = new NestedJsonElement(base);
	}

	/**
	 * Retreats back to the current element's parent
	 *
	 * @return the old/replaced current-element
	 * @throws IllegalStateException if we're already at the root element
	 */
	NestedJsonElement retreat() throws IllegalStateException {
		if (this.currentElement.isRoot()) { throw new IllegalStateException("cannot retreat from root"); }
		NestedJsonElement oldCurrent = this.currentElement;
		this.currentElement = this.currentElement.parent;
		return oldCurrent;
	}

	JsonBuilder append(JsonElement jsonElement) throws IllegalStateException {
		if (this.currentElement.element.isJsonArray()) {
			this.currentElement.element.getAsJsonArray().add(jsonElement);
		} else if (this.currentElement.element.isJsonObject()) {
			if (this.currentElement.name == null) { throw new IllegalStateException("tried to append JsonObject without name"); }
			this.currentElement.element.getAsJsonObject().add(this.currentElement.name, jsonElement);
		} else {
			throw new IllegalStateException("attempt to append element to " + this.currentElement.element.getClass());
		}
		return this;
	}

	/**
	 * Begin an array
	 *
	 * @return this builder
	 */
	public JsonBuilder beginArray() {
		this.currentElement = new NestedJsonElement(this.currentElement, new JsonArray());
		return this;
	}

	/**
	 * End an array
	 *
	 * @return this builder
	 */
	public JsonBuilder endArray() {
		return append(retreat().element);
	}

	/**
	 * Begin an object
	 *
	 * @return this builder
	 */
	public JsonBuilder beginObject() {
		this.currentElement = new NestedJsonElement(this.currentElement, new JsonObject());
		return this;
	}

	/**
	 * End an object
	 *
	 * @return this builder
	 */
	public JsonBuilder endObject() {
		return append(retreat().element);
	}

	/**
	 * Specify the name for the next value/object/array
	 *
	 * @param name the name
	 * @return this builder
	 */
	public JsonBuilder name(String name) {
		if (name == null) { throw new NullPointerException("name == null"); }
		this.currentElement.name = name;
		return this;
	}

	/**
	 * Append a JsonElement
	 *
	 * @param jsonElement the value
	 * @return this builder
	 */
	public JsonBuilder value(JsonElement jsonElement) {
		append(jsonElement);
		return this;
	}

	/**
	 * Append a JsonPrimitive value
	 *
	 * @param jsonPrimitive the value
	 * @return this builder
	 */
	public JsonBuilder value(JsonPrimitive jsonPrimitive) {
		append(jsonPrimitive);
		return this;
	}

	/**
	 * Append a String value
	 *
	 * @param string the value
	 * @return this builder
	 * @see #value(JsonPrimitive)
	 */
	public JsonBuilder value(String string) {
		return value(new JsonPrimitive(string));
	}

	/**
	 * Append a boolean value
	 *
	 * @param bool the value
	 * @return this builder
	 * @see #value(JsonPrimitive)
	 */
	public JsonBuilder value(Boolean bool) {
		return value(new JsonPrimitive(bool));
	}

	/**
	 * Append a number value
	 *
	 * @param number the value
	 * @return this builder
	 * @see #value(JsonPrimitive)
	 */
	public JsonBuilder value(Number number) {
		return value(new JsonPrimitive(number));
	}

	/**
	 * Append a character value
	 *
	 * @param character the value
	 * @return this builder
	 * @see #value(JsonPrimitive)
	 */
	public JsonBuilder value(Character character) {
		return value(new JsonPrimitive(character));
	}

	/**
	 * Append a <code>null</code> value
	 *
	 * @return this builder
	 */
	public JsonBuilder nullValue() {
		append(JsonNull.INSTANCE);
		return this;
	}

	/**
	 * Finish this builder and get the built element
	 *
	 * @return the built JsonElement
	 * @throws IllegalStateException if the current element is not closed (i.e. is not root)
	 */
	public JsonElement build() throws IllegalStateException {
		if (!this.currentElement.isRoot()) { throw new IllegalStateException("current element is not root"); }
		return this.currentElement.element;
	}

	/**
	 * Build this as a json object
	 *
	 * @return the built JsonObject
	 * @see #build()
	 */
	public JsonObject buildObject() {
		return build().getAsJsonObject();
	}

	/**
	 * Build this as a json array
	 *
	 * @return the built JsonArray
	 * @see #build()
	 */
	public JsonArray buildArray() {
		return build().getAsJsonArray();
	}

	/**
	 * Create a new object {@link JsonBuilder} with a base object
	 *
	 * @param base base to build on
	 * @return a new ObjectBuilder
	 */
	public static JsonBuilder object(JsonObject base) {
		return new JsonBuilder(base);
	}

	/**
	 * Create a new object {@link JsonBuilder}
	 *
	 * @return a new ObjectBuilder
	 */
	public static JsonBuilder object() {
		return new JsonBuilder(new JsonObject());
	}

	/**
	 * Create a new array {@link JsonBuilder} with a base array
	 *
	 * @param base base to build on
	 * @return a new ArrayBuilder
	 */
	public static JsonBuilder array(JsonArray base) {
		return new JsonBuilder(base);
	}

	/**
	 * Create a new array {@link JsonBuilder}
	 *
	 * @return a new ArrayBuilder
	 */
	public static JsonBuilder array() {
		return new JsonBuilder(new JsonArray());
	}

	class NestedJsonElement {
		NestedJsonElement parent;
		final JsonElement element;
		String name;

		@java.beans.ConstructorProperties({ "element" })
		NestedJsonElement(JsonElement element) {
			this.element = element;
		}

		@java.beans.ConstructorProperties({
												  "parent",
												  "element" })
		NestedJsonElement(NestedJsonElement parent, JsonElement element) {
			this.parent = parent;
			this.element = element;
		}

		@java.beans.ConstructorProperties({
												  "parent",
												  "name",
												  "element" })
		NestedJsonElement(NestedJsonElement parent, String name, JsonElement element) {
			this.parent = parent;
			this.name = name;
			this.element = element;
		}

		boolean isRoot() {
			return this.parent == null;
		}
	}

}
