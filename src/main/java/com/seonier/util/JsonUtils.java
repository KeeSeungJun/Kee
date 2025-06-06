package com.seonier.util;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import lombok.extern.slf4j.Slf4j;

/**
 * Json string to Object or Object to Json string conversion utils
 *
 * @version 1.0.0
 */
@Slf4j
@SuppressWarnings("unused")
public class JsonUtils {

	private static final AtomicReference<ObjectMapper> MAPPER = new AtomicReference<>(new ObjectMapper());

	private JsonUtils() throws IllegalAccessException {
		throw new IllegalAccessException("access to class not allowed.");
	}

	/**
	 * Conversion Object to Map.
	 *
	 * @param obj the conversion object.
	 * @return the map.
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> convertMap(final Object obj) {
		return MAPPER.get().convertValue(obj, LinkedHashMap.class);
	}

	/**
	 * Conversion Object to JSON String.
	 *
	 * @param obj the conversion object.
	 * @return the string.
	 * @throws JsonProcessingException the json processing errors.
	 */
	public static String toJson(final Object obj) throws JsonProcessingException {
		return MAPPER.get().writeValueAsString(obj);
	}

	/**
	 * Conversion Object to JSON String.
	 *
	 * <pre>
	 * - Add options below if needed.
	 *   MAPPER.configure(SerializationFeature.WRAP_ROOT_VALUE, true);
	 *   MAPPER.configure(SerializationFeature.INDENT_OUTPUT, true);
	 * </pre>
	 *
	 * @param obj the conversion object.
	 * @return teh string.
	 */
	public static String toJsonLog(final Object obj) {
		try {
			return MAPPER.get().writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			log.warn("Json Processing Exception - {}", obj.toString(), e);
			return null;
		}
	}

	/**
	 * Converting Object to JSON indented String.
	 *
	 * @param obj the conversion object.
	 * @return the json string.
	 */
	public static String toJsonLogIndent(final Object obj) {
		try {
			MAPPER.get().configure(SerializationFeature.INDENT_OUTPUT, true);
			return MAPPER.get().writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			log.warn("Json Processing Exception - {}", obj.toString(), e);
			return null;
		}
	}

	/**
	 * Convert JSON Object to Object.
	 *
	 * @param json the json conversion object.
	 * @param clazz the Object conversion class.
	 * @param <T> This is the type parameter.
	 * @return the object.
	 * @throws IOException in case of I/O errors in case of I/O errors.
	 */
	public static <T> Object toObject(final String json, final Class<T> clazz) throws IOException {
		return MAPPER.get().readValue(json, clazz);
	}

	/**
	 * Convert JSON Object to Object.
	 *
	 * @param json the json conversion object.
	 * @param clazz the Object conversion class.
	 * @param <T> This is the type parameter.
	 * @return th object.
	 * @throws IOException in case of I/O errors in case of I/O errors.
	 */
	public static <T> Object toObject(final Object json, final Class<T> clazz) throws IOException {
		return MAPPER.get().readValue(json.toString(), clazz);
	}

	/**
	 * Convert JSON Object to Object.
	 *
	 * @param json the json conversion object.
	 * @param typeRef the conversion type reference.
	 * @param <T> This is the type parameter.
	 * @return th object.
	 * @throws IOException in case of I/O errors in case of I/O errors.
	 */
	public static <T> Object toObject(final Object json, final TypeReference<T> typeRef) throws IOException {
		return MAPPER.get().readValue(toJson(json), typeRef);
	}

	/**
	 * Convert JSON Object to Object.
	 *
	 * @param json the json conversion object.
	 * @param typeRef the conversion type reference.
	 * @param <T> This is the type parameter.
	 * @return the object.
	 * @throws IOException in case of I/O errors in case of I/O errors.
	 */
	public static <T> Object toObject(final String json, final TypeReference<T> typeRef) throws IOException {
		return MAPPER.get().readValue(json, typeRef);
	}

}
