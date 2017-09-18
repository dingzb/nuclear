package cc.idiary.nuclear.service.selection;

import cc.idiary.nuclear.entity.selection.AwardEntity;
import cc.idiary.nuclear.model.PagingModel;
import cc.idiary.nuclear.model.selection.AwardModel;
import cc.idiary.nuclear.query.selection.AwardQuery;
import cc.idiary.nuclear.service.BaseService;
import cc.idiary.nuclear.service.ServiceException;

public interface AwardService extends BaseService<AwardEntity> {
    /**
     * 分页数据
     *
     * @param query
     * @return
     * @throws ServiceException
     */
    PagingModel paging(AwardQuery query) throws ServiceException;

    /**
     * 添加
     * 1、判断名称重复
     * 2、判断指定活动时候处在提交状态之前,否则无法创建
     *
     * @param award
     * @throws ServiceException
     */
    void add(AwardModel award) throws ServiceException;

    /**
     * 编辑
     * 1、判断名称重复
     * 2、判断指定活动时候处在提交状态之前，否则无法修改
     *
     * @param award
     * @throws ServiceException
     */
    void edit(AwardModel award) throws ServiceException;

    /**
     * 根据名称判断是否存在
     * @param name
     * @return
     * @throws ServiceException
     */
    boolean existByName(String name) throws ServiceException;

    void del(String[] ids) throws ServiceException;
}
