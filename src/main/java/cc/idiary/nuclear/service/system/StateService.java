package cc.idiary.nuclear.service.system;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import cc.idiary.nuclear.model.system.UserModel;
import cc.idiary.nuclear.query.system.StateQuery;
import cc.idiary.nuclear.model.PagingModel;
import cc.idiary.nuclear.service.BaseService;
import cc.idiary.nuclear.service.ServiceException;
import cc.idiary.nuclear.entity.system.StateEntity;
import cc.idiary.nuclear.model.system.StateModel;

/**
 * 状态服务 针对AngularJs ui-router的state
 *
 * @author Dzb
 */
public interface StateService extends BaseService<StateEntity> {

    /**
     * 添加
     *
     * @param stateModel
     * @return
     * @throws ServiceException
     */
    Serializable add(StateModel stateModel) throws ServiceException;

    /**
     * 编辑
     *
     * @param stateModel
     * @throws ServiceException
     */
    void edit(StateModel stateModel) throws ServiceException;

    /**
     * 根据roleIds获取其包含的stateList
     *
     * @param roleIds
     * @return
     */
    List<StateModel> getByRoleIds(Set<String> roleIds) throws ServiceException;

    /**
     * 根据用户ID获取stateList
     *
     * @param userId
     * @return
     * @throws ServiceException
     */
    List<StateModel> getByUserId(String userId) throws ServiceException;

    /**
     * 获取state列表
     *
     * @param query
     * @return
     * @throws ServiceException
     */
    PagingModel getPaging(StateQuery query) throws ServiceException;

    /**
     * 获取向上至subsystem的层级结构 List由上至下层级增加
     *
     * @param id
     * @return
     * @throws ServiceException
     */
    List<StateModel> getHierarchySubsystemById(String id)
            throws ServiceException;

    /**
     * 获取所有子state
     *
     * @param id
     * @param roleId
     * @return
     * @throws ServiceException
     */
    List<StateModel> filterByRoleId(String id, String roleId, UserModel user)
            throws ServiceException;

    /**
     * 获取所有子state 获取根节点下所有,并根据角色Id判断是否已经包含
     *
     * @param roleId
     * @return
     * @throws ServiceException
     */
    List<StateModel> filterByRoleId(String roleId, UserModel user) throws ServiceException;

    /**
     * 根据Id删除实体
     *
     * @param id
     * @throws ServiceException
     */
    void delete(String id) throws ServiceException;

    /**
     * 根据Id数组删除实体
     *
     * @param ids
     * @return
     * @throws ServiceException
     */
    Integer delete(String[] ids) throws ServiceException;

    /**
     * 获取列表结构的子节点
     *
     * @return
     * @throws ServiceException
     */
    List<StateModel> getList() throws ServiceException;

    /**
     * 根据角色在用户所拥有权限的state生成属相结构
     *
     * @param roleId
     * @param userId
     * @return
     * @throws ServiceException
     */
    List<StateModel> treeWithRoleUser(String roleId, String userId) throws ServiceException;
}
