package cc.idiary.nuclear.dao.system;

import cc.idiary.nuclear.entity.system.ActionEntity;
import cc.idiary.nuclear.query.system.ActionQuery;
import cc.idiary.nuclear.dao.BaseDao;

import java.util.List;
import java.util.Set;

/**
 * @author Dzb
 */
public interface ActionDao extends BaseDao<ActionEntity> {

    /**
     * 根据stateCodes获取action
     *
     * @param stateCodes
     * @return
     */
    List<ActionEntity> getByStateCodes(Set<String> stateCodes);

    /**
     * 根据stateId获取action列表
     *
     * @param stateId
     * @return
     */
    List<ActionEntity> getByStateId(String stateId);

    /**
     * 根据编码判断是否存在
     *
     * @param code
     * @return
     */
    Boolean existByCode(String code);

    /**
     * 获取分页列表
     *
     * @param query
     * @return
     */
    List<ActionEntity> getPaging(ActionQuery query);

    /**
     * 根据角色获取Action列表
     *
     * @param roleIds
     * @return
     */
    List<ActionEntity> getByRoleIds(Set<String> roleIds);

    /**
     * 根据编码获取action
     *
     * @param code
     * @return
     */
    ActionEntity getByCode(String code);

    /**
     * 根据角色获取action列表
     *
     * @param roleId
     * @return
     */
    List<ActionEntity> getByRoleId(String roleId);
}
