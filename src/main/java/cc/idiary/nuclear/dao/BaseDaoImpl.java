package cc.idiary.nuclear.dao;

import cc.idiary.nuclear.entity.BaseEntity;
import cc.idiary.nuclear.query.BaseQuery;
import cc.idiary.nuclear.query.PagingQuery;
import cc.idiary.utils.common.StringTools;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.text.MessageFormat;
import java.util.*;

/**
 * @param <E>
 * @author Dzb
 */
public abstract class BaseDaoImpl<E extends BaseEntity> implements BaseDao<E> {

    @Autowired
    private SessionFactory sessionFactory;

    private Logger logger = LogManager.getLogger(BaseDaoImpl.class);

    private Class<?> clazz;

    public BaseDaoImpl(Class<?> clazz) {
        super();
        this.sessionFactory = sessionFactory;
        this.clazz = clazz;
    }

    @Override
    public Serializable save(E e) {
        return sessionFactory.getCurrentSession().save(e);
    }

    @Override
    public void save(List<E> es) {
        Session session = sessionFactory.getCurrentSession();
        for (int i = 0; i < es.size(); i++) {
            session.save(es.get(i));
            if (i % 1000 == 0) {
                session.flush();
                session.clear();
            }
        }
    }

    @Override
    public void delete(E e) {
        sessionFactory.getCurrentSession().delete(e);
    }

    @Override
    public void update(E e) {
        sessionFactory.getCurrentSession().update(e);
    }

    @Override
    public void saveOrUpdate(E e) {
        sessionFactory.getCurrentSession().saveOrUpdate(e);
    }

    @SuppressWarnings("unchecked")
    public E getById(String id) {
        return (E) sessionFactory.getCurrentSession().get(clazz, id);
    }

    @Override
    public Boolean existById(String id) {
        String hql = "select count(*) from " + clazz.getSimpleName()
                + " where id=:id";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        Long count = (Long) getUnique(hql, params);
        return count > 0;
    }

    @Override
    public Set<E> getByIds(Collection<String> ids) {
        Set<E> es = new HashSet<E>();
        if (ids != null && !ids.isEmpty()) {
            for (String id : ids) {
                es.add(getById(id));
            }
        }
        return es;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<E> getByHql(String hql, Map<String, Object> params) {
        return getQuery(hql, params).list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> getByJoinHql(String hql, Map<String, Object> params) {
        return getQuery(hql, params).list();
    }

    @Override
    public int executeHql(String hql, Map<String, Object> params) {
        return getQuery(hql, params).executeUpdate();
    }

    @Override
    public Object getUnique(String hql, Map<String, Object> params) {
        return getQuery(hql, params).uniqueResult();
    }

    @Override
    public List<String> getIds(Collection<E> es) {
        List<String> ids = new ArrayList<String>();
        if (es != null && !es.isEmpty()) {
            for (E e : es) {
                ids.add(e.getId());
            }
        }
        return ids;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<E> getByHqlPaging(String hql, Map<String, Object> params, int page, int size) {
        Query query = getQuery(hql, params);
        query.setFirstResult(page * size - size);
        query.setMaxResults(size);
        return query.list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<E> getByHqlTop(String hql, Map<String, Object> params, int top) {
        Query query = getQuery(hql, params);
        query.setMaxResults(top);
        return query.list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> getByHqlJoinPaging(String hql, Map<String, Object> params, int page, int size) {
        Query query = getQuery(hql, params);
        query.setFirstResult(page * size - size);
        query.setMaxResults(size);
        return query.list();
    }

    /**
     * @param query  查询Model对象
     * @param alias  hql中实体的别名
     * @param params 参数列表Map
     * @return
     */
    protected String getHql(BaseQuery query, String alias,
                            Map<String, Object> params) {
        StringBuilder hqlsb = getExtensionSql(query, params);
        if (!StringTools.isEmpty(query.getId())) {
            hqlsb.append(" and {0}.id=:id");
            params.put("id", query.getId());
        }

        String hql = hqlsb.toString();

        for (String key : params.keySet()) {
            Object val = params.get(key);
            if (val instanceof String) {
                String str = (String) val;
                String s = "";
                String e = "";
                if (containLike(key, hql)) {
                    s = String.valueOf(str.charAt(0));
                    e = String.valueOf(str.charAt(str.length() - 1));
                    str = str.substring(1, str.length() - 1);
                }
                params.put(key, s + str.replace("%", "\\%").replace("_", "\\_") + e);
            }
        }

        return MessageFormat.format(hql, alias);
    }

    /*
     * 判断指定参数前是否包含 like 关键字
     * @param param
     * @param hql
     * @return
     */
    private boolean containLike(String param, String hql) {
        int ps = hql.indexOf(":" + param);
        int ls = hql.substring(0, ps).lastIndexOf("like");
        String section = hql.substring(ls + 5, ps);
        return section.trim().isEmpty();
    }

    /**
     * 获取子QueryModel的查询语句
     *
     * @param query
     * @param params
     * @return
     */
    protected abstract StringBuilder getExtensionSql(BaseQuery query, Map<String, Object> params);

    @Override
    public Integer delByIds(String[] ids) {
        String hql = "delete from " + clazz.getSimpleName()
                + " where id in (:ids)";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ids", ids);
        return executeHql(hql, params);
    }

    /**
     * @param by    依据的名称
     * @param byVal 依据名称的值
     * @return
     */
    @Override
    public boolean existBy(String by, Object byVal) {

        Field field = null;
        try {
            field = clazz.getDeclaredField(by);
        } catch (NoSuchFieldException | SecurityException e) {
            logger.catching(e);
        }
        String hql = "select count(*) from " + clazz.getSimpleName()
                + " where " + by + "=:" + by;

        if (field != null) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put(field.getName(), field.getType().cast(byVal));
            Long count = (Long) getUnique(hql, params);
            return count > 0;
        } else {
            return false;
        }
    }

    @Override
    public List<E> getList() {
        String hql = "from " + clazz.getSimpleName();
        return getByHql(hql, null);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> findBySql(String sql) {
        SQLQuery q = getSQLQuery(sql);
        return q.list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> findBySql(String sql, int page, int rows) {
        SQLQuery q = getSQLQuery(sql);
        return q.setFirstResult((page - 1) * rows).setMaxResults(rows).list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> findBySql(String sql, Map<String, Object> params) {
        SQLQuery q = getSQLQuery(sql);
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                q.setParameter(key, params.get(key));
            }
        }
        return q.list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> findBySql(String sql, Map<String, Object> params, int page, int rows) {
        return getSQLQuery(sql, params).setFirstResult((page - 1) * rows).setMaxResults(rows).list();
    }

    @Override
    public int executeSql(String sql) {
        SQLQuery q = getSQLQuery(sql);
        return q.executeUpdate();
    }

    @Override
    public int executeSql(String sql, Map<String, Object> params) {
        return getSQLQuery(sql, params).executeUpdate();
    }

    @Override
    public BigInteger countBySql(String sql) {
        SQLQuery q = getSQLQuery(sql);
        return (BigInteger) q.uniqueResult();
    }

    @Override
    public BigInteger countBySql(String sql, Map<String, Object> params) {
        SQLQuery q = getSQLQuery(sql);
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                q.setParameter(key, params.get(key));
            }
        }
        return (BigInteger) q.uniqueResult();
    }

    @Override
    public List<?> getBySql(String sql, Map<String, Object> params) {
        return getSQLQuery(sql, params).list();
    }

    @Override
    public long getCount(BaseQuery query) {
        if (query == null)
            return 0;
        Map<String, Object> params = new HashMap<String, Object>();
        return (Long) getUnique("select count(*)" + getHql(query, "r", params), params);
    }

    @Override
    public List<E> getByHqlPaging(PagingQuery query) {
        Map<String, Object> params = new HashMap<String, Object>();
        String hql = getHql(query, "pg", params);
        return getByHqlPaging(hql, params, query.getPage(), query.getSize());
    }

    @Override
    public List<E> getList(BaseQuery query) {
        Map<String, Object> params = new HashMap<String, Object>();
        String hql = getHql(query, "li", params);
        return getByHql(hql, params);
    }

    private Query getQuery(String hql) {
        return getQuery(hql, null);
    }

    private Query getQuery(String hql, Map<String, Object> params) {
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        if (params != null) {
            setParamsOnQuery(query, params);
        }
        return query;
    }

    private SQLQuery getSQLQuery(String sql) {
        return getSQLQuery(sql, null);
    }

    private SQLQuery getSQLQuery(String sql, Map<String, ?> params) {
        SQLQuery sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(sql);
        if (params != null) {
            setParamsOnQuery(sqlQuery, params);
        }
        return sqlQuery;
    }

    private <T extends Query> void setParamsOnQuery(T query, Map<String, ?> params) {
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                Object value = params.get(key);
                if (value instanceof Collection) {
                    query.setParameterList(key, (Collection<?>) value);
                } else if (value instanceof Object[]) {
                    query.setParameterList(key, (Object[]) value);
                } else {
                    query.setParameter(key, value);
                }
            }
        }
    }
}
