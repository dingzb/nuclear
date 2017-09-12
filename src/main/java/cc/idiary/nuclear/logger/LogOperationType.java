package cc.idiary.nuclear.logger;

/**
 * 业务日志操作类型
 * 
 * @author Neo
 *
 */
public enum LogOperationType {
	/**
	 * 登录
	 */
	LOGIN("LOGIN"),
	/**
	 * 添加
	 */
	ADD("ADD"),
	/**
	 * 修改
	 */
	MODIFY("MODIFY"),
	/**
	 * 删除
	 */
	DELETE("DELETE"),
	/**
	 * 导出
	 */
	EXPORT("EXPORT"),
	/**
	 * 导入
	 */
	IMPORT("IMPORT"),
	/**
	 * 查询
	 */
	QUERY("QUERY");

	private String value;

	LogOperationType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
