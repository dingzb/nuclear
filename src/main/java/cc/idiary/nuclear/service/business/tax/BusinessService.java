package cc.idiary.nuclear.service.business.tax;

import cc.idiary.nuclear.entity.business.tax.BusinessEntity;
import cc.idiary.nuclear.model.business.tax.BusAttachmentModel;
import cc.idiary.nuclear.model.business.tax.BusinessModel;
import cc.idiary.nuclear.model.business.tax.ExamineModel;
import cc.idiary.nuclear.model.business.tax.statistics.FenjuModel;
import cc.idiary.nuclear.model.business.tax.statistics.XianjuModel;
import cc.idiary.nuclear.query.business.tax.BusinessQuery;
import cc.idiary.nuclear.query.business.tax.FenjuQuery;
import cc.idiary.nuclear.model.PagingModel;
import cc.idiary.nuclear.model.business.tax.statistics.StatisticsCategoryTypeModel;
import cc.idiary.nuclear.query.business.tax.StatisticsQuery;
import cc.idiary.nuclear.query.business.tax.XianjuQuery;
import cc.idiary.nuclear.service.BaseService;
import cc.idiary.nuclear.service.ServiceException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.function.Function;

/**
 * Created by Neo on 2017/5/9.
 */
public interface BusinessService extends BaseService<BusinessEntity> {

    /**
     * 基于登陆用户的分页获取
     *
     * @param query
     * @return
     * @throws ServiceException
     */
    PagingModel pagingBaseUser(BusinessQuery query) throws ServiceException;

//    /**
//     * 获取所有业务分页数据
//     *
//     * @param query
//     * @return
//     * @throws ServiceException
//     */
//    PagingModel paging(BusinessQuery query) throws ServiceException;

    /**
     * 获取 新建业务
     *
     * @param query
     * @return
     * @throws ServiceException
     */
    PagingModel pagingCreated(BusinessQuery query) throws ServiceException;

    /**
     * 获取完成自查的业务
     *
     * @param query
     * @return
     * @throws ServiceException
     */
    PagingModel pagingFirst(BusinessQuery query) throws ServiceException;

    /**
     * @param query
     * @return
     * @throws ServiceException
     */
    PagingModel pagingSecond(BusinessQuery query) throws ServiceException;

    /**
     * @param query
     * @return
     * @throws ServiceException
     */
    PagingModel pagingThird(BusinessQuery query) throws ServiceException;

    /**
     * 查询需要整改的业务
     *
     * @param query
     * @return
     * @throws ServiceException
     */
    PagingModel pagingAmendment(BusinessQuery query) throws ServiceException;

    /**
     * 统计页面 业务详情专用
     *
     * @param query
     * @return
     * @throws ServiceException
     */
    PagingModel pagingError(BusinessQuery query) throws ServiceException;

    @Transactional
    PagingModel paging(BusinessQuery query) throws ServiceException;

    void add(BusinessModel businessModel) throws ServiceException;

    void edit(BusinessModel business) throws ServiceException;

    /**
     *
     * @param ids
     * @param getAbsPath 附件的绝对路径
     * @return
     * @throws ServiceException
     */
    Integer del(String[] ids, Function<String, String> getAbsPath) throws ServiceException;

    /**
     * 县局报表统计
     *
     * @param query
     * @return
     * @throws ServiceException
     */
    List<XianjuModel> xianju(XianjuQuery query) throws ServiceException;

    /**
     * 分局报表统计
     *
     * @param query
     * @return
     * @throws ServiceException
     */
    List<FenjuModel> fenju(FenjuQuery query) throws ServiceException;

    /**
     * 个人报表统计
     *
     * @param query
     * @return
     * @throws ServiceException
     */
    List<StatisticsCategoryTypeModel> person(StatisticsQuery query) throws ServiceException;

    /**
     * 提交业务
     *
     * @param ids
     * @throws ServiceException
     */
    void commit(String[] ids) throws ServiceException;

    /**
     * 提交检查结果
     *
     * @param examine
     * @throws ServiceException
     */
    void commitExamine(ExamineModel examine) throws ServiceException;

    /**
     * 提交整改
     *
     * @param ids
     * @throws ServiceException
     */
    void commitAmendment(String[] ids) throws ServiceException;

    /**
     * 获取业务中问题详情
     *
     * @param busId
     * @param step
     * @return
     * @throws ServiceException
     */
    ExamineModel examineDetail(String busId, String step) throws ServiceException;


    List<BusAttachmentModel> listAttachment(String busId) throws ServiceException;
}
