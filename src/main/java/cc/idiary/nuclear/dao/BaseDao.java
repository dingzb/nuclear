package cc.idiary.nuclear.dao;


import cc.idiary.nuclear.entity.BaseEntity;
import cc.idiary.nuclear.query.BaseQuery;
import cc.idiary.nuclear.query.PagingQuery;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @param <E>
 * @author Dzb
 */
public interface BaseDao<E extends BaseEntity> {

    /**
     * 保存实体对象
     *
     * @param e 实体对象
     * @return
     */
    Serializable save(E e);

    /**
     * 批量保存实体，防止内存溢出制定每次插入1000条<br/>
     * 当导入大量数据时考虑使用此方法，其他情况使用{@link #save(E)}
     *
     * @param es
     */
    void save(List<E> es);

    /**
     * 删除实体对象
     *
     * @param e 实体对象
     */
    void delete(E e);

    /**
     * 更新实体对象
     *
     * @param e 实体对象
     */
    void update(E e);

    /**
     * 保存或更新实体对象
     *
     * @param e 实体对象
     */
    void saveOrUpdate(E e);

    /**
     * 通过id获取实体对象
     *
     * @param id
     * @return
     */
    E getById(String id);

    /**
     * 根据Id判断对象是否存在
     *
     * @param id
     * @return
     */
    Boolean existById(String id);

    /**
     * 通过id列表获取实体对象列表
     *
     * @param ids 实体对象id列表
     * @return
     */
    Set<E> getByIds(Collection<String> ids);

    /**
     * 根据Hql语句返回实体对象列表，带参数 <br/>
     * 如果params中包含Collection类型的参数则使用setParameterList进行设置参数
     *
     * @param hql    Hql语句
     * @param params 查询参数列表
     * @return
     */
    List<E> getByHql(String hql, Map<String, Object> params);

    /**
     * 获取关联查询
     *
     * @param hql
     * @param params
     * @return
     */
    List<Object[]> getByJoinHql(String hql, Map<String, Object> params);

    /**
     * 执行hql语句 <br/>
     * 如果params中包含Collection类型的参数则使用setParameterList进行设置参数
     *
     * @param hql
     * @param params
     * @return
     */
    int executeHql(String hql, Map<String, Object> params);

    /**
     * 根据Hql语句返回唯一结果（一般用于查询记录数），带参数
     *
     * @param hql    Hql语句
     * @param params 查询参数列表
     * @return
     */
    Object getUnique(String hql, Map<String, Object> params);

    /**
     * 获取集合实体类的ID集合
     *
     * @param e
     * @return
     */
    List<String> getIds(Collection<E> e);

    /**
     * 分页查询
     *
     * @param hql    Hql语句
     * @param params 查询参数列表
     * @param page   查询页码
     * @param size   每页数据条数
     * @return
     */
    List<E> getByHqlPaging(String hql, Map<String, Object> params, int page, int size);

    /**
     * 获取指定数量记录
     *
     * @param hql    Hql语句
     * @param params 查询参数列表
     * @param top    记录数量
     * @return
     */
    List<E> getByHqlTop(String hql, Map<String, Object> params, int top);

    /**
     * @param hql
     * @param params
     * @param page
     * @param size
     * @return
     */
    List<Object[]> getByHqlJoinPaging(String hql, Map<String, Object> params, int page, int size);

    /**
     * 根据ids删除对象
     *
     * @param ids
     * @return
     */
    Integer delByIds(String[] ids);

    /**
     * 根据指定字段判断记录是否存在
     *
     * @param by
     * @param byVal
     * @return
     */
    boolean existBy(String by, Object byVal);

    /**
     * 获取当前对象列表，不带过滤
     *
     * @return
     * @author Dzb
     */
    List<E> getList();

    /**
     * 获得结果集
     *
     * @param sql SQL语句
     * @return 结果集
     */
    public List<Object[]> findBySql(String sql);

    /**
     * 获得结果集
     *
     * @param sql  SQL语句
     * @param page 要显示第几页
     * @param rows 每页显示多少条
     * @return 结果集
     */
    public List<Object[]> findBySql(String sql, int page, int rows);

    /**
     * 获得结果集
     *
     * @param sql    SQL语句
     * @param params 参数
     * @return 结果集
     */
    public List<Object[]> findBySql(String sql, Map<String, Object> params);

    /**
     * 获得结果集
     *
     * @param sql    SQL语句
     * @param params 参数
     * @return 结果集
     */
    public List<?> getBySql(String sql, Map<String, Object> params);

    /**
     * 获得结果集
     *
     * @param sql    SQL语句
     * @param params 参数
     * @param page   要显示第几页
     * @param rows   每页显示多少条
     * @return 结果集
     */
    public List<Object[]> findBySql(String sql, Map<String, Object> params, int page, int rows);

    /**
     * 执行SQL语句
     *
     * @param sql SQL语句
     * @return 响应行数
     */
    public int executeSql(String sql);

    /**
     * 执行SQL语句
     *
     * @param sql    SQL语句
     * @param params 参数
     * @return 响应行数
     */
    public int executeSql(String sql, Map<String, Object> params);

    /**
     * 统计
     *
     * @param sql SQL语句
     * @return 数目
     */
    public BigInteger countBySql(String sql);

    /**
     * 统计
     *
     * @param sql    SQL语句
     * @param params 参数
     * @return 数目
     */
    public BigInteger countBySql(String sql, Map<String, Object> params);


    /**
     * 通过query对象获取分页数据
     *
     * @param query
     * @return
     */
    List<E> getByHqlPaging(PagingQuery query);

    /**
     * 通过 Query查询满足条件的记录数
     *
     * @param query
     * @return
     */
    long getCount(BaseQuery query);

    /**
     * 通过query获取列表
     *
     * @param query
     * @return
     */
    List<E> getList(BaseQuery query);

}
