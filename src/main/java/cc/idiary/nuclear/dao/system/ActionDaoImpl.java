package cc.idiary.nuclear.dao.system;

import cc.idiary.nuclear.query.system.ActionQuery;
import cc.idiary.nuclear.dao.BaseDaoImpl;
import cc.idiary.nuclear.query.BaseQuery;
import cc.idiary.nuclear.entity.system.ActionEntity;
import cc.idiary.utils.common.StringTools;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * @author Dzb
 */
@Repository("actionDao")
public class ActionDaoImpl extends BaseDaoImpl<ActionEntity> implements ActionDao {

    public ActionDaoImpl() {
        super(ActionEntity.class);
    }

    @Override
    protected StringBuilder getExtensionSql(BaseQuery query,
                                            Map<String, Object> params) {
        ActionQuery actionQuery = (ActionQuery) query;
        StringBuilder hqlsb = new StringBuilder(
                " from ActionEntity {0} where 1=1");
        if (!StringTools.isEmpty(actionQuery.getStateId())) {
            hqlsb.append(" and {0}.state.id =:stateId");
            params.put("stateId", actionQuery.getStateId());
        }
        if (!StringTools.isEmpty(actionQuery.getName())) {
            hqlsb.append(" and {0}.name like :name");
            params.put("name", "%" + actionQuery.getName() + "%");
        }
        if (!StringTools.isEmpty(actionQuery.getCode())) {
            hqlsb.append(" and {0}.code=:code");
            params.put("code", "%" + actionQuery.getCode() + "%");
        }
        return hqlsb;
    }

    @Override
    public List<ActionEntity> getByStateCodes(Set<String> stateCodes) {
        if (stateCodes.isEmpty())
            return new ArrayList<ActionEntity>();
        String hql = "from ActionEntity where state.code in (:stateCodes)";
        Map<String, Object> params = new HashMap<>();
        params.put("stateCodes", stateCodes);
        return getByHql(hql, params);
    }

    @Override
    public Boolean existByCode(String code) {
        return existBy("code", code);
    }

    @Override
    public List<ActionEntity> getPaging(ActionQuery query) {
        Map<String, Object> params = new HashMap<String, Object>();
        String hql = getHql(query, "action", params);
        return getByHqlPaging(hql, params, query.getPage(), query.getSize());
    }

    @Override
    public List<ActionEntity> getByRoleIds(Set<String> roleIds) {
        if (roleIds.isEmpty())
            return new ArrayList<ActionEntity>();
        String hql = "select new ActionEntity(action.id, action.name, action.code, action.url, action.method) "
                + "from ActionEntity action left join action.roles role where role.id in (:roleIds)";
        Map<String, Object> params = new HashMap<>();
        params.put("roleIds", roleIds);
        return getByHql(hql, params);
    }

    @Override
    public List<ActionEntity> getByStateId(String stateId) {
        if (StringTools.isEmpty(stateId))
            return new ArrayList<ActionEntity>();
        String hql = "from ActionEntity where state.id =:stateId";
        Map<String, Object> params = new HashMap<>();
        params.put("stateId", stateId);
        return getByHql(hql, params);
    }

    @Override
    public ActionEntity getByCode(String code) {
        String hql = "from ActionEntity where code = :code";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("code", code);
        return getByHql(hql, params).get(0);
    }

    @Override
    public List<ActionEntity> getByRoleId(String roleId) {
        Set<String> roleIds = new HashSet<>();
        roleIds.add(roleId);
        return getByRoleIds(roleIds);
    }
}
