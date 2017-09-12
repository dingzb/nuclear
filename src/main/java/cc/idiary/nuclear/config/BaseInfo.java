package cc.idiary.nuclear.config;

import cc.idiary.nuclear.model.system.StateModel;

/**
 * 系统的基本配置信息
 * 
 * @author Dzb
 *
 */
public class BaseInfo {

//	public static final UserModel USER_SUPREME = new UserModel();
	public static final StateModel STATE_ROOT = new StateModel();
	public static final StateModel STATE_MAIN = new StateModel();
	public static final String PASSWORD_DEF = "123123";
	static {
//		USER_SUPREME.setName("supreme");
//		USER_SUPREME.setId("BC980CFC3B7D6DBBA09205904C5E2789");
//		USER_SUPREME.setType(UserType.BUILD_IN.toString());
		STATE_ROOT.setCode("root");
		STATE_MAIN.setCode("main");
	}
}
