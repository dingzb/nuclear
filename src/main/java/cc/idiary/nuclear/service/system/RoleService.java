package cc.idiary.nuclear.service.system;

import cc.idiary.nuclear.query.system.RoleQuery;
import cc.idiary.nuclear.model.PagingModel;
import cc.idiary.nuclear.service.BaseService;
import cc.idiary.nuclear.service.ServiceException;
import cc.idiary.nuclear.entity.system.RoleEntity;
import cc.idiary.nuclear.model.system.RoleModel;
import cc.idiary.nuclear.model.system.UserModel;

import java.io.Serializable;
import java.util.List;

public interface RoleService extends BaseService<RoleEntity> {

    /**
     * 添加
     *
     * @param roleModel
     * @return
     * @throws ServiceException
     */
    Serializable add(RoleModel roleModel) throws ServiceException;

    /**
     * 分页查询
     *
     * @param query
     * @return
     * @throws ServiceException
     */
    PagingModel getPaging(RoleQuery query) throws ServiceException;

    /**
     * 编辑
     *
     * @param roleModel
     * @throws ServiceException
     */
    void edit(RoleModel roleModel) throws ServiceException;

    /**
     * 权限添加
     *
     * @param roleId    角色Id
     * @param actionIds 动作Id数组
     * @param stateId   状态id
     * @throws ServiceException
     */
    void saveAction(String roleId, String[] actionIds, String stateId)
            throws ServiceException;

    /**
     * 权限添加
     *
     * @param roleId   角色Id
     * @param stateIds 状态Id数组
     * @throws ServiceException
     */
    void saveState(String roleId, String[] stateIds) throws ServiceException;

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
     * 根据名称判断角色是否存在
     *
     * @param name
     * @return
     * @throws ServiceException
     */
    boolean existByName(String name) throws ServiceException;


    List<String> getTypes(UserModel user) throws ServiceException;

}
