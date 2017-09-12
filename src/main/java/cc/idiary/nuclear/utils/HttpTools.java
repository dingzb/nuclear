package cc.idiary.nuclear.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author Neo
 *
 */
public class HttpTools {

	private CloseableHttpClient httpClient = HttpClients.createDefault();
	private ObjectMapper objectMapper = new ObjectMapper();

	private HttpTools() {
	}

	public static HttpTools getInstance() {
		return new HttpTools();
	}

	/**
	 * 发送 post 请求
	 * 
	 * @param uri
	 *            目标地址
	 * @param params
	 *            参数 Map
	 * @return
	 */
	public HttpResponse post(String uri, Map<String, String> params) {
		return doPost(uri, null, params);
	}

	/**
	 * 发送 post 请求
	 * 
	 * @param uri
	 *            目标地址
	 * @param entityParams
	 *            实体参数 bean <br>
	 *            对于不希望转成json的字段使用
	 *            {@link com.fasterxml.jackson.annotation.JsonIgnore} 注解
	 * @return
	 */
	public HttpResponse post(String uri, Object entityParams) {
		return doPost(uri, entityParams, null);
	}

	/**
	 * 发送请求并对返回的json字符串序列化成对象
	 * 
	 * @param uri
	 *            目标地址
	 * @param entityParams
	 *            实体参数 bean <br>
	 *            对于不希望转成json的字段使用
	 *            {@link com.fasterxml.jackson.annotation.JsonIgnore} 注解
	 * @param clazz
	 *            序列化 Response 体中的字符串到指定类型
	 * @return
	 **/
	public <T> T post(String uri, Object entityParams, Class<T> clazz) {
		HttpResponse response = doPost(uri, entityParams, null);
		return getObject(response, clazz);
	}

	/**
	 * 发送 post 请求
	 * 
	 * @param uri
	 *            目标地址
	 * @param params
	 *            参数 Map
	 * @return
	 */
	public HttpResponse get(String uri, Map<String, String> params) {
		return doGet(uri, null, params);
	}

	/**
	 * 发送 post 请求
	 * 
	 * @param uri
	 *            目标地址
	 * @param entityParams
	 *            实体参数 bean <br>
	 *            对于不希望转成json的字段使用
	 *            {@link com.fasterxml.jackson.annotation.JsonIgnore} 注解
	 * @return
	 */
	public HttpResponse get(String uri, Object entityParams) {
		return doGet(uri, entityParams, null);
	}

	/**
	 * 发送请求并对返回的json字符串序列化成对象
	 * 
	 * @param uri
	 *            目标地址
	 * @param entityParams
	 *            实体参数 bean <br>
	 *            对于不希望转成json的字段使用
	 *            {@link com.fasterxml.jackson.annotation.JsonIgnore} 注解
	 * @param clazz
	 *            序列化 Response 体中的字符串到指定类型
	 * @return
	 */
	public <T> T get(String uri, Object entityParams, Class<T> clazz) {
		HttpResponse response = doGet(uri, entityParams, null);
		return getObject(response, clazz);
	}

	/**
	 * 
	 * @param uri
	 *            目标地址
	 * @param entityParams
	 *            实体参数 bean <br>
	 *            对于不希望转成json的字段使用
	 *            {@link com.fasterxml.jackson.annotation.JsonIgnore} 注解
	 * @return
	 */
	private HttpResponse doPost(String uri, Object entityParams,
			Map<String, String> params) {
		return doRequest(uri, entityParams, params, RequestMethod.POST);

	}

	/**
	 * 
	 * @param uri
	 * @param params
	 * @return
	 */
	private HttpResponse doGet(String uri, Object entityParams,
			Map<String, String> params) {
		return doRequest(uri, entityParams, params, RequestMethod.GET);
	}

	/**
	 * 
	 * @param uri
	 * @param params
	 * @param method
	 * @return
	 */
	private HttpResponse doRequest(String uri, Object entityParams,
			Map<String, String> params, RequestMethod method) {

		RequestBuilder builder = RequestBuilder
				.create(method.toString())
				.setUri(uri);

		HttpResponse response = null;

		if (entityParams != null) {
			HttpEntity entity = null;
			builder.addHeader("Content-Type","application/json; charset=utf-8");
			try {
				entity = new StringEntity(getJson(entityParams), "utf-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			builder.setEntity(entity);
		}

		if (params != null && !params.isEmpty()) {
			String par = "";
			builder.addHeader("Content-Type",
					"application/x-www-form-urlencoded; charset=utf-8");
			for (String name : params.keySet()) {
				par+=(name+"="+params.get(name)+"&");
			}
			HttpEntity entity = null;
			try {
				entity = new StringEntity(par.substring(0,par.length()-1), "utf-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			builder.setEntity(entity);
		}

		try {
			response = httpClient.execute(builder.build());
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}

	/**
	 * 返回json字符串
	 * 
	 * @param value
	 * @return
	 */
	private String getJson(Object value) {
		String json = null;

		try {
			json = objectMapper.writeValueAsString(value);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return json;
	}

	/**
	 * 根据json字符串返回对象
	 * 
	 * @param response
	 * @param clazz
	 * @return
	 */
	private <T> T getObject(HttpResponse response, Class<T> clazz) {

		// TODO
		String content = null;
		try {
			content = EntityUtils.toString(response.getEntity());
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			return objectMapper.readValue(content, clazz);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String inputStreamToString(InputStream is) {
		StringBuilder builder = new StringBuilder();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(is, "utf-8"));
			String line;
			while ((line = reader.readLine()) != null) {
				builder.append(line + "\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return builder.toString();
	}
}
