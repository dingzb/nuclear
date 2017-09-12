package cc.idiary.nuclear.dao.system;

import cc.idiary.nuclear.config.RoleType;
import cc.idiary.nuclear.query.system.RoleQuery;
import cc.idiary.nuclear.dao.BaseDaoImpl;
import cc.idiary.nuclear.entity.system.RoleEntity;
import cc.idiary.nuclear.query.BaseQuery;
import cc.idiary.utils.common.StringTools;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository("roleDao")
public class RoleDaoImpl extends BaseDaoImpl<RoleEntity> implements RoleDao {

    public RoleDaoImpl() {
        super(RoleEntity.class);
    }

    @Override
    protected StringBuilder getExtensionSql(BaseQuery queryModel,
                                            Map<String, Object> params) {
        RoleQuery query = (RoleQuery) queryModel;
        StringBuilder hqlsb = new StringBuilder(
                " from RoleEntity {0} where 1=1");
        if (!StringTools.isEmpty(query.getInclUserId())) {
            hqlsb.append(" and :inclUserId member of {0}.users");
            params.put("inclUserId", query.getInclUserId());
        }
        if (!StringTools.isEmpty(query.getExclUserId())) {
            hqlsb.append(" and :exclUserId not member of {0}.users");
            params.put("exclUserId", query.getExclUserId());
        }
        if (!StringTools.isEmpty(query.getName())) {
            hqlsb.append(" and {0}.name like :name");
            params.put("name", "%" + query.getName() + "%");
        }
        if (!StringTools.isEmpty(query.getDescription())) {
            hqlsb.append(" and {0}.description like :description");
            params.put("description", "%" + query.getDescription() + "%");
        }
        if (query.getTypes() != null) {
            if (query.getTypes().isEmpty()) {
                hqlsb.append(" and {0}.type in ('''')");
            } else {
                hqlsb.append(" and {0}.type in (:types)");
                params.put("types", query.getTypes());
            }
        }
        return hqlsb;
    }

    @Override
    public boolean existByName(String name) {
        return existBy("name", name);
    }

    @Override
    public List<RoleEntity> getPaging(RoleQuery query) {
        Map<String, Object> params = new HashMap<>();
        String hql = getHql(query, "role", params);
        return getByHqlPaging(hql, params, query.getPage(), query.getSize());
    }

    @Override
    public RoleEntity getUniqueRole(RoleType type) {
        String hql = "from RoleEntity r where r.type =:type";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("type", type);
        if (!RoleType.ADMIN.equals(type)) {
            throw new IllegalArgumentException("只允许 ADMIN,AGENT,MERCHANT 类型");
        }
        List<RoleEntity> roles = getByHql(hql, params);
        if (roles != null && !roles.isEmpty()) {
            return roles.get(0);
        }
        return null;
    }

    @Override
    public Set<RoleEntity> getCodes(Set<String> roleCodes) {
        String hql = "from RoleEntity role where role.code in (:codes)";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("codes", roleCodes);
        List<RoleEntity> roles = getByHql(hql, params);
        Set<RoleEntity> result = new HashSet<RoleEntity>();
        result.addAll(roles);
        return result;
    }

    @Override
    public RoleEntity getUserRole(String userId) {
        if (StringTools.isEmpty(userId)) {
            return null;
        }
        String hql = "from RoleEntity role where role.type in (:types) and :userId member of role.users";
        Map<String, Object> params = new HashMap<String, Object>();
        Set<RoleType> types = new HashSet<RoleType>();
        types.add(RoleType.ADMIN);
        types.add(RoleType.USER);
        params.put("types", types);
        params.put("userId", userId);
        List<RoleEntity> roles = getByHql(hql, params);
        if (roles != null && !roles.isEmpty()) {
            return roles.get(0);
        }
        return null;
    }
}
