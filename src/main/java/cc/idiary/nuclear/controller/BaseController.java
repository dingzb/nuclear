package cc.idiary.nuclear.controller;

import cc.idiary.nuclear.model.Json;

public class BaseController {

	private Json json;

	protected Json fail(String message) {
		json = new Json();
		json.setSuccess(false);
		json.setMessage(message);
		return json;
	}

	protected Json fail(Exception e) {
		json = new Json();
		json.setSuccess(false);
		json.setMessage(e.getMessage());
		return json;
	}

	protected Json fail() {
		json = new Json();
		json.setSuccess(false);
		return json;
	}

	protected Json success() {
		json = new Json();
		json.setSuccess(true);
		return json;
	}

	protected Json success(Object data) {
		json = new Json();
		json.setData(data);
		json.setSuccess(true);
		return json;
	}

	protected Json success(String msg) {
		json = new Json();
		json.setSuccess(true);
		json.setMessage(msg);
		return json;
	}

	protected Json success(Object data, String msg) {
		json = new Json();
		json.setData(data);
		json.setSuccess(true);
		json.setMessage(msg);
		return json;
	}
}
