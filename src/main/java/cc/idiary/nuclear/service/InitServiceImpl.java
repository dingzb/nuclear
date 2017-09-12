package cc.idiary.nuclear.service;

import cc.idiary.nuclear.config.GroupType;
import cc.idiary.nuclear.config.UserType;
import cc.idiary.nuclear.dao.system.*;
import cc.idiary.nuclear.entity.system.*;
import cc.idiary.utils.common.StringTools;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cc.idiary.nuclear.config.RoleType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service("systemConfigService")
public class InitServiceImpl implements InitService {

	private static final Logger logger = LogManager
			.getLogger(InitServiceImpl.class);
	@Autowired
	private StateDao stateDao;
	@Autowired
	private ActionDao actionDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private RoleDao roleDao;
	@Autowired
	private GroupDao groupDao;

	private InputStream getInitFile(String fileName) {
		return this.getClass().getResourceAsStream("/init/" + fileName);
	}

	/**
	 * 判断系统是否初始化
	 * 
	 * @return
	 */
	private boolean isInit() {
		try {
			if (stateDao.getList().isEmpty()) {
				return false;
			}
		} catch (Exception e) {
			logger.catching(e);
		}
		return true;
	}

	@Transactional
	@Override
	public void init(InitProcess process) throws ServiceException {
		if (isInit()) {
			process.setProcess("Already initialized!", -1);
			throw new ServiceException("Already initialized!");
		}
		process.setProcess("Init Struct begin.", 0);
		importStateAction(getInitFile("Struct.json"));
		process.setProcess("Init Default begin.", 50);
		importDefault(getInitFile("Default.json"));
		process.setProcess("Init Extension begin.", 100);
	}

	private void importStateAction(InputStream is) throws ServiceException {
		if (is == null) {
			throw new ServiceException("文件不能为空");
		}

		try {
			List<ActionEntity> actions = actionDao.getList();
			List<StateEntity> states = stateDao.getList();
			for (StateEntity state : states) {
				stateDao.delete(state);
			}
			for (ActionEntity action : actions) {
				actionDao.delete(action);
			}
		} catch (Exception e) {
			logger.catching(e);
			throw new ServiceException();
		}

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode json = null;
		try {
			json = objectMapper.readTree(is);
		} catch (JsonParseException e) {
			logger.catching(e);
			throw new ServiceException();
		} catch (JsonMappingException e) {
			logger.catching(e);
			throw new ServiceException();
		} catch (IOException e) {
			logger.catching(e);
			throw new ServiceException();
		}
		saveState(json, null);
	}

	private void importDefault(InputStream is) throws ServiceException {

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode json = null;
		try {
			json = objectMapper.readTree(is);
		} catch (JsonParseException e) {
			logger.catching(e);
			throw new ServiceException();
		} catch (JsonMappingException e) {
			logger.catching(e);
			throw new ServiceException();
		} catch (IOException e) {
			logger.catching(e);
			throw new ServiceException();
		}
		importRoles(json);
		importGroups(json);
		importUsers(json);
	}

	// ----------------导入预置信息----------------------------
	/**
	 * 导入预置角色
	 * 
	 * @param json
	 * @throws ServiceException
	 */
	private void importRoles(JsonNode json) throws ServiceException {
		JsonNode jsonRolesNode = json.get("roles");
		if (jsonRolesNode == null || !jsonRolesNode.elements().hasNext()) {
			return;
		}
		Iterator<JsonNode> josnRoles = jsonRolesNode.elements();
		Set<ActionEntity> allActions = new HashSet<ActionEntity>();
		allActions.addAll(actionDao.getList());
		Set<StateEntity> allStates = new HashSet<StateEntity>();
		allStates.addAll(stateDao.getList());
		try {
			while (josnRoles.hasNext()) {
				JsonNode jsonRole = josnRoles.next();
				RoleEntity role = new RoleEntity();
				Set<ActionEntity> actions = new HashSet<ActionEntity>();
				Set<StateEntity> states = new HashSet<StateEntity>();
				role.setActions(actions);
				role.setStates(states);
				role.setId(jsonRole.get("id") == null ? StringTools
						.randomUUID() : StringTools.Md5(jsonRole.get("id")
						.textValue()));
				role.setCode(jsonRole.get("code") == null ? "" : jsonRole.get(
						"code").textValue());
				role.setName(jsonRole.get("name") == null ? "" : jsonRole.get(
						"name").textValue());
				role.setDescription(jsonRole.get("description") == null ? ""
						: jsonRole.get("description").textValue());
				role.setCreateTime(new Date());
				role.setModifyTime(new Date());
				
				if (jsonRole.get("type") != null) {
					role.setType(RoleType.valueOf(jsonRole.get("type")
							.textValue()));
				}
				// include
				JsonNode include = jsonRole.get("include");
				if (include != null) {
					// action
					if (include.has("actions")) {
						Iterator<JsonNode> jsonActionsNode = include.get(
								"actions").elements();
						while (jsonActionsNode.hasNext()) {
							JsonNode jsonAction = jsonActionsNode.next();
							if (jsonAction.isBoolean()
									&& jsonAction.asBoolean()) {
								actions.addAll(allActions);
							} else {
								ActionEntity action = actionDao
										.getByCode(jsonAction.asText());
								actions.add(action);
							}

						}
					}
					// state
					if (include.has("states")) {
						Iterator<JsonNode> jsonStatesNode = include.get(
								"states").elements();
						while (jsonStatesNode.hasNext()) {
							JsonNode jsonState = jsonStatesNode.next();
							if (jsonState.isBoolean() && jsonState.asBoolean()) {
								states.addAll(allStates);
							} else {
								StateEntity state = stateDao
										.getByCode(jsonState.asText());
								states.add(state);
							}
						}
					}
				}
				// exclude
				JsonNode exclude = jsonRole.get("exclude");
				if (exclude != null) {
					// action
					if (exclude.has("actions")) {
						Iterator<JsonNode> jsonActionsNode = exclude.get(
								"actions").elements();
						while (jsonActionsNode.hasNext()) {
							JsonNode jsonAction = jsonActionsNode.next();
							ActionEntity action = getActionByCode(allActions,
									jsonAction.asText());
							actions.remove(action);
						}
					}
					// state
					// 被排除的state 会同事排斥其子state和 他们所包含的的action
					if (exclude.has("states")) {
						Iterator<JsonNode> jsonStatesNode = exclude.get(
								"states").elements();
						while (jsonStatesNode.hasNext()) {
							JsonNode jsonState = jsonStatesNode.next();
							StateEntity state = getStateByCode(allStates,
									jsonState.asText());
							Set<StateEntity> allChildren = getState(state);
							states.remove(state);
							states.removeAll(allChildren);
							Set<ActionEntity> allContainActions = getAction(state);
							actions.removeAll(allContainActions);
						}
					}
				}
				roleDao.save(role);
			}
		} catch (Exception e) {
			logger.catching(e);
			throw new ServiceException();
		}
	}

	/**
	 * 导入预置组
	 * 
	 * @param json
	 * @throws ServiceException
	 */
	private void importGroups(JsonNode json) throws ServiceException {
		JsonNode jsonGroupsNode = json.get("groups");
		if (jsonGroupsNode == null || !jsonGroupsNode.elements().hasNext()) {
			return;
		}

		Iterator<JsonNode> jsonGroups = jsonGroupsNode.elements();

		while (jsonGroups.hasNext()) {
			JsonNode jsonGroup = jsonGroups.next();
			GroupEntity group = new GroupEntity();
			try {
				group.setId(jsonGroup.get("id") == null ? StringTools
						.randomUUID() : StringTools.Md5(jsonGroup.get("id")
						.textValue()));
				group.setCode(jsonGroup.get("code") == null ? "" : jsonGroup
						.get("code").textValue());
				group.setName(jsonGroup.get("name") == null ? "" : jsonGroup
						.get("name").textValue());
				group.setDescription(jsonGroup.get("description") == null ? ""
						: jsonGroup.get("description").textValue());
				if (jsonGroup.get("type") != null) {
					group.setType(GroupType.valueOf(jsonGroup.get("type")
							.textValue()));
				}
				groupDao.save(group);
			} catch (Exception e) {
				logger.catching(e);
				throw new ServiceException();
			}
		}
	}

	/**
	 * 导入预置用户
	 * 
	 * @param json
	 * @throws ServiceException
	 */
	private void importUsers(JsonNode json) throws ServiceException {
		JsonNode jsonUsersNode = json.get("users");
		if (jsonUsersNode == null || !jsonUsersNode.elements().hasNext()) {
			return;
		}

		Iterator<JsonNode> jsonUsers = jsonUsersNode.elements();

		while (jsonUsers.hasNext()) {
			JsonNode jsonUser = jsonUsers.next();
			UserEntity user = new UserEntity();
			try {
				user.setCreateTime(new Date());
				user.setModifyTime(new Date());
				user.setId(jsonUser.get("id") == null ? StringTools
						.randomUUID() : StringTools.Md5(jsonUser.get("id")
						.textValue()));
				user.setName(jsonUser.get("name") == null ? "" : jsonUser.get(
						"name").textValue());
				user.setUsername(jsonUser.get("username") == null ? ""
						: jsonUser.get("username").textValue());
				user.setPassword(StringTools
						.Md5(jsonUser.get("password") == null ? "" : jsonUser
								.get("password").textValue()));
				if (jsonUser.has("type")) {
					user.setType(UserType.valueOf(jsonUser.get("type")
							.textValue()));
				}
				if (jsonUser.has("roles")) {
					Iterator<JsonNode> rolesJson = jsonUser.get("roles")
							.elements();
					Set<String> roleCodes = new HashSet<String>();
					while (rolesJson.hasNext()) {
						roleCodes.add(rolesJson.next().textValue());
					}
					Set<RoleEntity> roles = roleDao.getCodes(roleCodes);
					user.setRoles(roles);
				}
				if (jsonUser.has("groups")) {
					Iterator<JsonNode> groupsJson = jsonUser.get("groups")
							.elements();
					Set<String> groupCodes = new HashSet<String>();
					while (groupsJson.hasNext()) {
						groupCodes.add(groupsJson.next().textValue());
					}
					Set<GroupEntity> groups = groupDao.getCodes(groupCodes);
					user.setGroups(groups);
				}
				userDao.save(user);
			} catch (Exception e) {
				logger.catching(e);
				throw new ServiceException();
			}
		}
	}


	// ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
	/**
	 * 根据code获取action
	 * 
	 * @param code
	 * @param actions
	 * @return
	 */
	private ActionEntity getActionByCode(Set<ActionEntity> actions, String code) {
		for (ActionEntity action : actions) {
			if (action.getCode().equals(code)) {
				return action;
			}
		}
		return null;
	}

	/**
	 * 根据code获取state
	 * 
	 * @param code
     * @param states
	 * @return
	 */
	private StateEntity getStateByCode(Set<StateEntity> states, String code) {
		for (StateEntity state : states) {
			if (state.getCode().equals(code)) {
				return state;
			}
		}
		return null;
	}

	/**
	 * 获取所有子节点state
	 * 
	 * @param state
	 * @return
	 */
	private Set<StateEntity> getState(StateEntity state) {
		Set<StateEntity> results = new HashSet<StateEntity>();
		List<StateEntity> stateStack = new ArrayList<StateEntity>();
		stateStack.add(state);

		while (!stateStack.isEmpty()) {
			StateEntity tmp = stateStack.remove(0);
			results.add(tmp);
			if (tmp.getChildren() != null && !tmp.getChildren().isEmpty()) {
				stateStack.addAll(tmp.getChildren());
			}
		}

		return results;
	}

	/**
	 * 获取包含子节点的所有action
	 * 
	 * @param state
	 * @return
	 */
	private Set<ActionEntity> getAction(StateEntity state) {
		Set<ActionEntity> reslut = new HashSet<ActionEntity>();
		List<StateEntity> stateStack = new ArrayList<StateEntity>();
		stateStack.add(state);

		while (!stateStack.isEmpty()) {
			StateEntity tmp = stateStack.remove(0);
			Set<String> codes=new HashSet<String>();
			codes.add(tmp.getCode());
			List<ActionEntity> actions= actionDao.getByStateCodes(codes);
			if (actions != null) {
				for (ActionEntity actionEntity : actions) {
					reslut.add(actionEntity);
				}
			}
			if (tmp.getChildren() != null && !tmp.getChildren().isEmpty()) {
				stateStack.addAll(tmp.getChildren());
			}
		}

		return reslut;
	}

	/**
	 * 保存角色
	 * 
	 * @param json
	 * @throws ServiceException
	 */
	private RoleEntity saveRole(JsonNode json) throws ServiceException {

		try {
			List<RoleEntity> roles = roleDao.getList();
			for (RoleEntity role : roles) {
				roleDao.delete(role);
			}
		} catch (Exception e) {
			logger.catching(e);
			throw new ServiceException();
		}

		RoleEntity role = new RoleEntity();
		role.setId(StringTools.randomUUID());
		role.setCreateTime(new Date());
		role.setModifyTime(new Date());

		if (json.has("role") && json.get("role").elements().hasNext()) {
			try {
				role.setCode(json.get("role").get("code") == null ? "" : json
						.get("role").get("code").textValue());
				role.setName(json.get("role").get("name") == null ? "" : json
						.get("role").get("name").textValue());
				role.setDescription(json.get("role").get("description") == null ? ""
						: json.get("role").get("description").textValue());
				if (json.get("role").get("type") != null) {
					role.setType(RoleType.valueOf(json.get("role").get("type")
							.textValue()));
				}
			} catch (Exception e) {
				logger.catching(e);
				throw new ServiceException();
			}

		} else {
			logger.warn("没有角色字段");
		}

		List<String> exState = new ArrayList<String>();
		List<String> exAction = new ArrayList<String>();

		if (json.has("exclude")) {
			if (json.get("exclude").has("state")) {
				Iterator<JsonNode> iter = json.get("exclude").get("state")
						.elements();
				try {
					while (iter.hasNext()) {
						JsonNode node = iter.next();
						StateEntity state = stateDao
								.getByCode(node.textValue());
						getCodes(state, exState, exAction);
					}

				} catch (Exception e) {
					logger.catching(e);
					throw new ServiceException();
				}

			}
			if (json.get("exclude").has("action")) {
				Iterator<JsonNode> iter = json.get("exclude").get("action")
						.elements();
				while (iter.hasNext()) {
					JsonNode node = iter.next();
					exAction.add(node.textValue());
				}
			}
		}

		if (json.has("include")) {
			// if (json.get("include").has("state")) {
			// Iterator<JsonNode> iter = json.get("include").get("state")
			// .elements();
			// while (iter.hasNext()) {
			// JsonNode node = iter.next();
			// exState.remove(node.textValue());
			// }
			// }
			if (json.get("include").has("action")) {
				Iterator<JsonNode> iter = json.get("include").get("action")
						.elements();
				while (iter.hasNext()) {
					JsonNode node = iter.next();
					exAction.remove(node.textValue());
				}
			}
		}

		try {
			Set<StateEntity> states = new HashSet<StateEntity>();
			Set<ActionEntity> actions = new HashSet<ActionEntity>();

			for (StateEntity state : stateDao.getList()) {
				if (!exState.contains(state.getCode())) {
					states.add(state);
				}
			}

			for (ActionEntity action : actionDao.getList()) {
				if (!exAction.contains(action.getCode())) {
					actions.add(action);
				}
			}

			role.setActions(actions);
			role.setStates(states);

			roleDao.save(role);
			return role;
		} catch (Exception e) {
			logger.catching(e);
			throw new ServiceException();
		}
	}

	/**
	 * 获取本身及其子节点的所有state.code,action.code
	 * 
	 * @param state
	 * @param stateCodes
	 * @param actionCodes
	 * @throws ServiceException
	 */
	private void getCodes(StateEntity state, List<String> stateCodes,
			List<String> actionCodes) throws ServiceException {
		try {
			stateCodes.add(state.getCode());
			for (ActionEntity action : state.getActions()) {
				actionCodes.add(action.getCode());
			}
			Set<StateEntity> children = state.getChildren();
			if (state.getChildren() != null) {
				for (StateEntity child : children) {
					getCodes(child, stateCodes, actionCodes);
				}
			}
		} catch (Exception e) {
			logger.catching(e);
			throw new ServiceException();
		}
	}

	/**
	 * @param json
	 * @throws ServiceException
	 */
	private GroupEntity saveGroup(JsonNode json) throws ServiceException {

		try {
			List<GroupEntity> groups = groupDao.getList();
			for (GroupEntity group : groups) {
				groupDao.delete(group);
			}
		} catch (Exception e) {
			logger.catching(e);
			throw new ServiceException();
		}

		GroupEntity group = new GroupEntity();
		group.setId(StringTools.randomUUID());
		group.setCreateTime(new Date());
		group.setModifyTime(new Date());

		if (json.has("group") && json.get("group").elements().hasNext()) {
			try {
				group.setCode(json.get("group").get("code") == null ? "" : json
						.get("group").get("code").textValue());
				group.setName(json.get("role").get("name") == null ? "" : json
						.get("group").get("name").textValue());
				group.setDescription(json.get("group").get("description") == null ? ""
						: json.get("group").get("description").textValue());
				if (json.get("group").get("type") != null) {
					group.setType(GroupType.valueOf(json.get("group")
							.get("type").textValue()));
				}
			} catch (Exception e) {
				logger.catching(e);
				throw new ServiceException();
			}

		} else {
			logger.warn("没有组字段");
		}

		try {
			groupDao.save(group);
			return group;
		} catch (Exception e) {
			logger.catching(e);
			throw new ServiceException();
		}
	}

	/**
	 * 
	 * @param json
	 * @param role
	 * @throws ServiceException
	 */
	private void saveUser(JsonNode json, RoleEntity role, GroupEntity group)
			throws ServiceException {

		try {
			List<UserEntity> users = userDao.getList();
			for (UserEntity user : users) {
				userDao.delete(user);
			}
		} catch (Exception e) {
			logger.catching(e);
			throw new ServiceException();
		}

		UserEntity user = new UserEntity();

		user.setId(StringTools.randomUUID());
		user.setCreateTime(new Date());
		user.setModifyTime(new Date());
		Set<RoleEntity> roles = new HashSet<RoleEntity>();
		roles.add(role);
		Set<GroupEntity> groups = new HashSet<GroupEntity>();
		groups.add(group);
		user.setRoles(roles);
		user.setGroups(groups);

		if (json.has("user") && json.get("user").elements().hasNext()) {
			try {
				user.setName(json.get("user").get("name") == null ? "" : json
						.get("user").get("name").textValue());
				user.setUsername(json.get("user").get("username") == null ? ""
						: json.get("user").get("username").textValue());
				user.setPassword(StringTools.Md5(json.get("user").get(
						"password") == null ? "" : json.get("user")
						.get("password").textValue()));
				if (json.get("user").get("type") != null) {
					user.setType(UserType.valueOf(json.get("user").get("type")
							.textValue()));
				}
				userDao.save(user);
			} catch (Exception e) {
				logger.catching(e);
				throw new ServiceException();
			}

		} else {
			logger.warn("没有用户字段");
		}
	}

	/**
	 * 保存state对象
	 * 
	 * @param json
	 * @param parent
	 * @throws ServiceException
	 */
	private void saveState(JsonNode json, StateEntity parent)
			throws ServiceException {

		if (!json.has("name") || !json.has("code") || !json.has("type")
				|| !json.has("actions") || !json.has("children")) {
			throw new ServiceException("格式错误");
		}
		StateEntity state = new StateEntity();

		state.setId(StringTools.randomUUID());
		state.setCreateTime(new Date());
		state.setModifyTime(new Date());
		state.setParent(parent);

		try {
			state.setName(json.get("name") == null ? "" : json.get("name")
					.textValue());
			state.setCode(json.get("code") == null ? "" : json.get("code")
					.textValue());
			state.setType((json.get("type") == null || json.get("type")
					.textValue().isEmpty()) ? 0 : Integer.parseInt(json.get(
					"type").textValue()));
			state.setDescription(json.get("description") == null ? "" : json
					.get("description").textValue());
			state.setSequence((json.get("sequence") == null || json
					.get("sequence").textValue().isEmpty()) ? 0 : Integer
					.parseInt(json.get("sequence").textValue()));
		} catch (Exception e) {
			logger.catching(e);
			throw new ServiceException("格式错误");
		}

		try {
			stateDao.save(state);
		} catch (Exception e) {
			logger.catching(e);
			throw new ServiceException();
		}
		JsonNode actions = null;
		try {
			actions = json.get("actions");
		} catch (Exception e) {
			logger.catching(e);
			throw new ServiceException();
		}

		if (actions != null) {
			Iterator<JsonNode> actionIter = json.get("actions").elements();
			if (actionIter != null) {
				while (actionIter.hasNext()) {
					saveAction(actionIter.next(), state);
				}
			}
		}

		JsonNode children = null;
		try {
			children = json.get("children");
		} catch (Exception e) {
			logger.catching(e);
			throw new ServiceException();
		}

		if (children != null) {
			Iterator<JsonNode> childrenIter = json.get("children").elements();
			if (childrenIter != null) {
				while (childrenIter.hasNext()) {
					saveState(childrenIter.next(), state);
				}
			}
		}

	}

	/**
	 * 保存action对象
	 * 
	 * @param json
	 * @param state
	 * @throws ServiceException
	 */
	private void saveAction(JsonNode json, StateEntity state)
			throws ServiceException {

		if (!json.elements().hasNext()) {
			return;
		}

		ActionEntity action = new ActionEntity();

		action.setId(StringTools.randomUUID());
		action.setCreateTime(new Date());
		action.setModifyTime(new Date());
		action.setState(state);

		try {
			if (json.has("name")) {
				action.setName(json.get("name").textValue());
			}
			action.setName(json.get("name") == null ? "" : json.get("name")
					.textValue());
			action.setCode(json.get("code") == null ? "" : json.get("code")
					.textValue());
			action.setMethod((json.get("method") == null || json.get("method")
					.textValue().isEmpty()) ? null : Integer.parseInt(json.get(
					"method").textValue()));
			action.setUrl(json.get("url") == null ? "" : json.get("url")
					.textValue());
			action.setDescription(json.get("description") == null ? "" : json
					.get("description").textValue());
		} catch (Exception e) {
			logger.catching(e);
			throw new ServiceException("格式错误");
		}
		try {
			actionDao.save(action);
		} catch (Exception e) {
			logger.catching(e);
			throw new ServiceException();
		}
	}

	/**
	 * 初始化进度显示
	 * 
	 * @author Neo
	 *
	 */
	public static class InitProcess {
		private String name;
		private Integer value;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Integer getValue() {
			return value;
		}

		public void setValue(Integer value) {
			this.value = value;
		}

		public void setProcess(String name, Integer value) {
			this.name = name;
			this.value = value;
		}
	}
}