package com.jiangyu.common.utils;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

/**
 * <p>Title: Json工具类 </p>
 * <p> Description:</p>
 * <p>1.json数组字符串转化成对象数组</p>
 * <p>2.将json字符串转化成某个对象</p>
 * <p>3.转换字符串为Json对象</p>
 *
 */
public class JsonUtil {
	/**
	 * json数组转化成对象数组
	 * 
	 * @param jsonStr
	 * @return
	 */
	public static <T> List<T> jsonToArray(String jsonStr, Class<T[]> clazz) {
		T[] arr = new Gson().fromJson(jsonStr, clazz);
		return Arrays.asList(arr);
	}

	public static <T> List<T> jsonToArray(JSONArray jsonArray, Class<T[]> clazz) {
		return jsonToArray(jsonArray.toString(), clazz);
	}
	/**
	 * 对象转Json字符串
	 * 
	 * @param entity
	 *            需要转换的对象实体
	 * @return
	 */
	public static String toJson(Object entity) {
		if (entity == null) {
			return "";
		}
		try {
			Gson gson = new Gson();
			return gson.toJson(entity);
		} catch (Exception e) {
			Log.e("toJson 异常。", e);
			return "";
		}
	}
	//禁止转义json
	public static String toJsonNew(Object entity) {
		if (entity == null) {
			return "";
		}
		try {
			Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
			return gson.toJson(entity);
		} catch (Exception e) {
			Log.e("toJson 异常。", e);
			return "";
		}
	}

	/**
	 * Json字符串转对象
	 * 
	 * @param <T>
	 * @param result
	 *            Json字符串
	 * @return
	 */
	public static <T> T toObject(String result, Class<T> clazz) {
		if (clazz == null || StringUtil.isEmpty(result)) {
			return null;
		}
		try {
			Gson gson = new Gson();
			return gson.fromJson(result, clazz);
		} catch (Exception e) {
			Log.e("JSON 转换异常！", e);
			try {
				return clazz.newInstance();
			} catch (IllegalAccessException e1) {
				Log.e("toObject IllegalAccessException 实例化异常", e1);
			} catch (InstantiationException e1) {
				Log.e("toObject IllegalAccessException 实例化异常", e1);
			}
		}
		return null;
	}

	/**
	 * 转换字符串为Json对象
	 * 
	 * @param json
	 *            json字符串
	 * @return
	 */
	public static JSONObject parseJSON(String json) {
		try {
			return new JSONObject(json);
		} catch (JSONException e) {
			Log.e("JSONException :" + e);
		}
		return new JSONObject();
	}
}
