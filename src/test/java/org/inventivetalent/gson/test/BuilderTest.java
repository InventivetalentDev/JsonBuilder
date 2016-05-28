package org.inventivetalent.gson.test;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.inventivetalent.gson.JsonBuilder;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BuilderTest {

	// Basic

	@Test
	public void objectTest() {
		JsonObject jsonObject = JsonBuilder.object().name("objectTest").value("Object Value").buildObject();
		System.out.println(jsonObject);

		assertTrue(jsonObject.has("objectTest"));
		assertTrue(jsonObject.get("objectTest").isJsonPrimitive());
		assertEquals(jsonObject.get("objectTest").getAsString(), "Object Value");
	}

	@Test
	public void arrayTest() {
		JsonArray jsonArray = JsonBuilder.array().value(new JsonPrimitive("Array Value 1")).value("Array Value 2").buildArray();
		System.out.println(jsonArray);

		assertTrue(jsonArray.size() == 2);
		assertEquals(jsonArray.get(0).getAsString(), "Array Value 1");
		assertEquals(jsonArray.get(1).getAsString(), "Array Value 2");
	}

	// Nested

	@Test
	public void nestedObjectTest() {
		JsonObject jsonObject = JsonBuilder.object().name("nest1").beginObject().name("nest1Name").value("Nest 1 Value").endObject().buildObject();
		System.out.println(jsonObject);

		assertTrue(jsonObject.has("nest1"));
		assertTrue(jsonObject.get("nest1").isJsonObject());
		assertTrue(jsonObject.get("nest1").getAsJsonObject().has("nest1Name"));
		assertEquals(jsonObject.get("nest1").getAsJsonObject().get("nest1Name").getAsString(), "Nest 1 Value");
	}

	@Test
	public void nestedArrayTest() {
		JsonArray jsonArray = JsonBuilder.array().value("Array Value 1").beginArray().value("Nest Value 1").value("Nest Value 2").endArray().buildArray();
		System.out.println(jsonArray);

		assertTrue(jsonArray.size() == 2);
		assertTrue(jsonArray.get(0).isJsonPrimitive());
		assertEquals(jsonArray.get(0).getAsString(), "Array Value 1");

		assertTrue(jsonArray.get(1).isJsonArray());
		assertTrue(jsonArray.get(1).getAsJsonArray().size() == 2);
		assertEquals(jsonArray.get(1).getAsJsonArray().get(0).getAsString(), "Nest Value 1");
		assertEquals(jsonArray.get(1).getAsJsonArray().get(1).getAsString(), "Nest Value 2");
	}

	// Random tests

	@Test
	public void randomTest() {
		final Random random = new Random();

		JsonBuilder builder = JsonBuilder.object();
		int r = (random.nextInt(100) + 1);
		for (int i = 0; i < r; i++) {
			builder.name("random:" + i);

			if (random.nextBoolean()) {
				builder.beginArray();
				int s = (random.nextInt(10) + 1);
				for (int j = 0; j < s; j++) {
					builder.value("r:" + j);
				}
				builder.endArray();
			} else {
				builder.beginObject();
				int s = (random.nextInt(10) + 1);
				for (int j = 0; j < s; j++) {
					builder.name("r:" + j).value(random.nextDouble());
				}
				builder.endObject();
			}
		}

		JsonObject jsonObject = builder.buildObject();
		System.out.println(jsonObject);
	}

	// Expected fails

	@Test(expected = IllegalStateException.class)
	public void unclosedObjectTest() throws IllegalStateException {
		JsonObject jsonObject = JsonBuilder.object().name("unclosed").beginObject().name("test1").buildObject();
		System.out.println(jsonObject);
	}

	@Test(expected = IllegalStateException.class)
	public void multiUnclosedObjectTest() {
		JsonObject jsonObject = JsonBuilder.object().beginObject().beginObject().beginObject().name("test1").value("Test 1").buildObject();
		System.out.println(jsonObject);
	}

	@Test(expected = IllegalStateException.class)
	public void unclosedArrayTest() throws IllegalStateException {
		JsonArray jsonArray = JsonBuilder.array().beginArray().value("test1").buildArray();
		System.out.println(jsonArray);
	}

	@Test(expected = NullPointerException.class)
	public void nullNameTest() {
		JsonObject jsonObject = JsonBuilder.object().name(null).value("Null").buildObject();
		System.out.println(jsonObject);
	}

	@Test(expected = IllegalStateException.class)
	public void rootRetreat() {
		JsonObject jsonObject = JsonBuilder.object().endObject().buildObject();
		System.out.println(jsonObject);
	}

	@Test(expected = IllegalStateException.class)
	public void arrayAsObjectTest() {
		JsonObject jsonObject = JsonBuilder.array().buildObject();
		System.out.println(jsonObject);
	}

	@Test(expected = IllegalStateException.class)
	public void objectAsArrayTest() {
		JsonArray jsonArray = JsonBuilder.object().buildArray();
		System.out.println(jsonArray);
	}

}
