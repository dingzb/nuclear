package cc.idiary.nuclear.dao.system;

import cc.idiary.nuclear.config.GroupType;
import cc.idiary.nuclear.config.UserType;
import cc.idiary.nuclear.entity.system.GroupEntity;
import cc.idiary.nuclear.query.system.GroupQuery;
import cc.idiary.nuclear.dao.BaseDaoImpl;
import cc.idiary.nuclear.query.BaseQuery;
import cc.idiary.utils.common.StringTools;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository("groupDao")
public class GroupDaoImpl extends BaseDaoImpl<GroupEntity> implements GroupDao{

	public GroupDaoImpl() {
		super(GroupEntity.class);
	}

	@Override
	protected StringBuilder getExtensionSql(BaseQuery query,
			Map<String, Object> params) {
		GroupQuery queryModel = (GroupQuery) query;
		StringBuilder hqlsb = new StringBuilder(
				" from GroupEntity {0} where 1=1");
		if (!StringTools.isEmpty(queryModel.getName())) {
			hqlsb.append(" and {0}.name like :name");
			params.put("name", "%" + queryModel.getName() + "%");
		}
		if (!StringTools.isEmpty(queryModel.getDescription())) {
			hqlsb.append(" and {0}.description like :description");
			params.put("description", "%" + queryModel.getDescription() + "%");
		}
		if (queryModel.getTypes() != null) {
			if (queryModel.getTypes().isEmpty()) {
				hqlsb.append(" and {0}.type in ('''')");
			} else {
				hqlsb.append(" and {0}.type in (:types)");
				params.put("types", queryModel.getTypes());
			}
		}

		if (!StringTools.isEmpty(queryModel.getInclUserId())) {
			hqlsb.append(" and :inclUserId member of {0}.users");
			params.put("inclUserId", queryModel.getInclUserId());
		}
		if (!StringTools.isEmpty(queryModel.getExclUserId())) {
			hqlsb.append(" and :exclUserId not member of {0}.users");
			params.put("exclUserId", queryModel.getExclUserId());
		}

		return hqlsb;
	}

	@Override
	public List<GroupEntity> getPaging(GroupQuery query) {
		Map<String, Object> params = new HashMap<>();
		String hql = getHql(query, "gp", params);
		return getByHqlPaging(hql, params, query.getPage(), query.getSize());
	}

	@Override
	public boolean existByName(String name) {
		return existBy("name", name);
	}

	@Override
	public Set<String> getAuthGroupIdsByUserId(String userId, int option) {

		if (StringTools.isEmpty(userId)) {
			return null;
		}
		String hql = "from GroupEntity gp where gp.id in "
				+ "(select new GroupEntity(g.id) from GroupEntity g left join g.users u where u.id = :userId)";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		List<GroupEntity> self = getByHql(hql, params);
		List<GroupEntity> children = new ArrayList<GroupEntity>();
		List<GroupEntity> monitors = new ArrayList<GroupEntity>();

		if ((option & 1) == 1) {
			children = getChildren(self);
		}
		Set<String> result = new HashSet<String>();
		result.addAll(getIds(self));
		result.addAll(getIds(children));
		result.addAll(getIds(monitors));
		return result;
	}

	/**
	 * 获取所有子组，递归获取
	 * 
	 * @param groups
	 * @return
	 */
	private List<GroupEntity> getChildren(Collection<GroupEntity> groups) {
		List<GroupEntity> children = new ArrayList<GroupEntity>();
		for (GroupEntity group : groups) {
			children.add(group);
			if (group.getChildren() != null && !group.getChildren().isEmpty()) {
				children.addAll(getChildren(group.getChildren()));
			}
		}
		return children;
	}

	@Override
	public GroupEntity getUserGroup(String userId, UserType userType) {
		String hql = "from GroupEntity gp where gp.id in "
				+ "(select new GroupEntity(g.id) from GroupEntity g left join g.users u where u.id = :userId)";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		List<GroupEntity> list = getByHql(hql, params);
		if (list != null && list.size() > 0) {
			for (GroupEntity group : list) {
				// 1.如果所属用户类型为admin，则返回该用户的admin组属性
				if (UserType.ADMIN.equals(userType)
						&& GroupType.ADMIN.equals(group.getType())) {
					return group;
				} else {// 2.如果用户非admin组，则返回用户用户组属性
					if (GroupType.USER.equals(group.getType())) {
						return group;
					}
				}
			}
		}
		return null;
	}

	@Override
	public GroupEntity getUserGroup(String userId) {
		if (StringTools.isEmpty(userId)) {
			return null;
		}
		String hql = "from GroupEntity gp where gp.type in (:types) and :userId member of gp.users";
		Map<String, Object> params = new HashMap<String, Object>();
		Set<GroupType> types = new HashSet<GroupType>();
		types.add(GroupType.ADMIN);
		types.add(GroupType.USER);
		params.put("types", types);
		params.put("userId", userId);
		List<GroupEntity> groups = getByHql(hql, params);
		if (groups != null && !groups.isEmpty()) {
			return groups.get(0);
		}
		return null;
	}

	@Override
	public Set<GroupEntity> getCodes(Set<String> groupCodes) {
		String hql = "from GroupEntity gp where gp.code in (:codes)";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("codes", groupCodes);
		List<GroupEntity> groups = getByHql(hql, params);
		Set<GroupEntity> result = new HashSet<GroupEntity>();
		result.addAll(groups);
		return result;
	}

}
