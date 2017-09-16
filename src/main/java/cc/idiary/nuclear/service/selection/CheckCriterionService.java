package cc.idiary.nuclear.service.selection;

import cc.idiary.nuclear.entity.selection.CheckCriterionEntity;
import cc.idiary.nuclear.model.PagingModel;
import cc.idiary.nuclear.model.selection.CheckCriterionModel;
import cc.idiary.nuclear.query.selection.CheckCriterionQuery;
import cc.idiary.nuclear.service.BaseService;
import cc.idiary.nuclear.service.ServiceException;

import java.util.List;

public interface CheckCriterionService extends BaseService<CheckCriterionEntity> {
    /**
     * 分页数据
     *
     * @param query
     * @return
     * @throws ServiceException
     */
    PagingModel paging(CheckCriterionQuery query) throws ServiceException;

    /**
     * 添加
     * 1、判断名称重复
     * 2、判断指定活动时候处在形式审核状态之前，若不在这个状态之前则无法创建
     *
     * @param checkCriterion
     * @throws ServiceException
     */
    void add(CheckCriterionModel checkCriterion) throws ServiceException;

    /**
     * 编辑
     * 1、判断名称重复
     * 2、判断指定活动时候处在形式审核状态之前，若不在这个状态之前则无法创建
     *
     * @param checkCriterion
     * @throws ServiceException
     */
    void edit(CheckCriterionModel checkCriterion) throws ServiceException;

    /**
     * 根据名称判断是否存在
     * @param name
     * @return
     * @throws ServiceException
     */
    boolean existByName(String name) throws ServiceException;

    void del(String[] ids) throws ServiceException;
}
