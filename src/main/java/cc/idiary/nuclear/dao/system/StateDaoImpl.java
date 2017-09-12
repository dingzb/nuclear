package cc.idiary.nuclear.dao.system;

import cc.idiary.nuclear.query.system.StateQuery;
import cc.idiary.nuclear.dao.BaseDaoImpl;
import cc.idiary.nuclear.entity.system.StateEntity;
import cc.idiary.nuclear.query.BaseQuery;
import cc.idiary.utils.common.StringTools;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository("stateDao")
public class StateDaoImpl extends BaseDaoImpl<StateEntity> implements StateDao {

    public StateDaoImpl() {
        super(StateEntity.class);
    }

    @Override
    public Boolean existByCode(String code) {
        return existBy("code", code);
    }

    @Override
    protected StringBuilder getExtensionSql(BaseQuery query,
                                            Map<String, Object> params) {
        StateQuery stateQuery = (StateQuery) query;
        StringBuilder sbhql = new StringBuilder(
                " from StateEntity {0} where 1=1");
        if (!StringTools.isEmpty(stateQuery.getName())) {
            sbhql.append(" and {0}.name like :name");
            params.put("name", "%" + stateQuery.getName() + "%");
        }
        if (!StringTools.isEmpty(stateQuery.getCode())) {
            sbhql.append(" and {0}.code like :code");
            params.put("code", "%" + stateQuery.getCode() + "%");
        }
        if (stateQuery.getType() != null) {
            sbhql.append(" and {0}.type =:type");
            params.put("type", stateQuery.getType());
        }
        if (!StringTools.isEmpty(stateQuery.getParentId())) {
            sbhql.append(" and {0}.parent.id=:parentId");
            params.put("parentId", stateQuery.getParentId());
        }
        if (StringTools.isEmpty(stateQuery.getParentId())
                && stateQuery.getSubsystemId() != null) {
            List<StateEntity> parents = getChilred(stateQuery.getSubsystemId());
            List<String> ids = getIds(parents);
            if (!ids.isEmpty()) {
                sbhql.append(" and {0}.parent.id in (:parentIds)");
                params.put("parentIds", ids);
            }
        }
        sbhql.append(" and {0}.code like ''main.%''");

        return sbhql;
    }

    /**
     * 获取指定Id的所有一级子节点
     *
     * @return
     */
    private List<StateEntity> getChilred(String id) {
        String hql = "from StateEntity where parent.id =:parentId";
        Map<String, Object> params = new HashMap<>();
        params.put("parentId", id);
        return getByHql(hql, params);
    }

    @Override
    public StateEntity getByCode(String code) {
        String hql = "from StateEntity where code = :code";
        Map<String, Object> params = new HashMap<>();
        params.put("code", code);
        List<StateEntity> result = getByHql(hql, params);
        return result.isEmpty() ? null : result.get(0);
    }

    @Override
    public List<StateEntity> getByRoleIds(Set<String> roleIds) {
        if (roleIds.isEmpty())
            return new ArrayList<StateEntity>();
        String hql = "select new StateEntity(state.id, state.name, state.code, state.type, state.sequence)"
                + " from StateEntity state left join state.roles role where role.id in (:roleIds)"
                + " group by state.id";
        Map<String, Object> params = new HashMap<>();
        params.put("roleIds", roleIds);
        return getByHql(hql, params);
    }

    @Override
    public List<StateEntity> getPaging(StateQuery query) {
        Map<String, Object> params = new HashMap<>();
        String hql = getHql(query, "state", params);
        hql += " order by state.sequence";
        return getByHqlPaging(hql, params, query.getPage(), query.getSize());
    }

    @Override
    public List<StateEntity> getByRoleId(String roleId) {
        Set<String> ids = new HashSet<>();
        ids.add(roleId);
        return getByRoleIds(ids);
    }
}
