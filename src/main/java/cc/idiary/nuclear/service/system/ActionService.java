package cc.idiary.nuclear.service.system;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import cc.idiary.nuclear.entity.system.ActionEntity;
import cc.idiary.nuclear.model.system.ActionModel;
import cc.idiary.nuclear.query.system.ActionQuery;
import cc.idiary.nuclear.model.PagingModel;
import cc.idiary.nuclear.service.BaseService;
import cc.idiary.nuclear.service.ServiceException;

public interface ActionService extends BaseService<ActionEntity> {
    /**
     * 根据stateCodeIds获取对应的action列表
     *
     * @param stateCodeIds
     * @return
     * @throws ServiceException
     */
    List<ActionModel> getByStateCodeIds(Set<String> stateCodeIds)
            throws ServiceException;

    /**
     * 根据角色Id获取列表
     *
     * @param roleIds
     * @return
     * @throws ServiceException
     */
    List<ActionModel> getByRoleIds(Set<String> roleIds) throws ServiceException;

    /**
     * 添加
     *
     * @param actionModel
     * @return
     * @throws ServiceException
     */
    Serializable add(ActionModel actionModel) throws ServiceException;

    /**
     * 获取action分页列表
     *
     * @param query
     * @return
     * @throws ServiceException
     */
    PagingModel getPaging(ActionQuery query) throws ServiceException;

    /**
     * 编辑
     *
     * @param actionModel
     * @throws ServiceException
     */
    void edit(ActionModel actionModel) throws ServiceException;

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
     * 获取所有列表结构的action
     *
     * @return
     * @throws ServiceException
     */
    List<ActionModel> getList() throws ServiceException;

    /**
     * 根据用户ID获取actionList
     *
     * @param userId
     * @return
     * @throws ServiceException
     */
    List<ActionModel> getByUserId(String userId) throws ServiceException;
}
