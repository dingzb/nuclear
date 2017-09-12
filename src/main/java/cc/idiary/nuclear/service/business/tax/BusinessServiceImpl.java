package cc.idiary.nuclear.service.business.tax;

import cc.idiary.nuclear.dao.business.tax.*;
import cc.idiary.nuclear.entity.business.tax.*;
import cc.idiary.nuclear.model.business.tax.BusAttachmentModel;
import cc.idiary.nuclear.model.business.tax.BusIssueModel;
import cc.idiary.nuclear.model.business.tax.BusinessModel;
import cc.idiary.nuclear.model.business.tax.ExamineModel;
import cc.idiary.nuclear.model.business.tax.statistics.XianjuModel;
import cc.idiary.nuclear.query.business.tax.FenjuQuery;
import cc.idiary.nuclear.config.UserType;
import cc.idiary.nuclear.dao.system.UserDao;
import cc.idiary.nuclear.entity.system.UserEntity;
import cc.idiary.nuclear.model.PagingModel;
import cc.idiary.nuclear.model.business.tax.statistics.FenjuModel;
import cc.idiary.nuclear.model.business.tax.statistics.StatisticsCategoryModel;
import cc.idiary.nuclear.model.business.tax.statistics.StatisticsCategoryTypeModel;
import cc.idiary.nuclear.query.business.tax.BusinessQuery;
import cc.idiary.nuclear.query.business.tax.StatisticsQuery;
import cc.idiary.nuclear.query.business.tax.XianjuQuery;
import cc.idiary.nuclear.service.BaseServiceImpl;
import cc.idiary.nuclear.service.ServiceException;
import cc.idiary.nuclear.utils.UploadTools;
import cc.idiary.utils.common.StringTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by Neo on 2017/5/9.
 */
@Service("businessService")
public class BusinessServiceImpl extends BaseServiceImpl<BusinessEntity> implements BusinessService {

    private static final Logger logger = LoggerFactory.getLogger(BusinessServiceImpl.class);
    @Autowired
    private BusinessDao businessDao;

    @Autowired
    private BusinessCategoryDao businessCategoryDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private BusinessIssueDao businessIssueDao;

    @Autowired
    private AgencyDao agencyDao;

    @Autowired
    private BusinessAttachmentDao businessAttachmentDao;

    @Transactional
    @Override
    public PagingModel pagingCreated(BusinessQuery query) throws ServiceException {
        queryBasicAssert(query);
//        query.setIncludeStatus(new Integer[]{BUS_STATUS.CREATE, BUS_STATUS.FIRST, BUS_STATUS.SECOND, BUS_STATUS.THIRD, BUS_STATUS.HAS_ISSUE, BUS_STATUS.FINISH});
        query.setStatus(BusinessEntity.BUS_STATUS.CREATE);
        return pagingBaseUser(query);
    }

    @Transactional
    @Override
    public PagingModel pagingFirst(BusinessQuery query) throws ServiceException {
        queryBasicAssert(query);
//        query.setIncludeStatus(new Integer[]{BUS_STATUS.FIRST, BUS_STATUS.SECOND, BUS_STATUS.THIRD, BUS_STATUS.HAS_ISSUE, BUS_STATUS.FINISH});
        query.setStatus(BusinessEntity.BUS_STATUS.FIRST);
        return pagingBaseUser(query);
    }

    @Transactional
    @Override
    public PagingModel pagingSecond(BusinessQuery query) throws ServiceException {
        queryBasicAssert(query);
//        query.setIncludeStatus(new Integer[]{BUS_STATUS.SECOND, BUS_STATUS.THIRD, BUS_STATUS.HAS_ISSUE, BUS_STATUS.FINISH});
        query.setStatus(BusinessEntity.BUS_STATUS.SECOND);
        return pagingBaseUser(query);
    }

    @Transactional
    @Override
    public PagingModel pagingThird(BusinessQuery query) throws ServiceException {
        queryBasicAssert(query);
//        query.setIncludeStatus(new Integer[]{BUS_STATUS.THIRD, BUS_STATUS.HAS_ISSUE, BUS_STATUS.FINISH});
        query.setStatus(BusinessEntity.BUS_STATUS.THIRD);
        return pagingBaseUser(query);
    }

    @Transactional
    @Override
    public PagingModel pagingAmendment(BusinessQuery query) throws ServiceException {
        queryBasicAssert(query);
//        query.setStatus(BUS_STATUS.HAS_ISSUE);
        query.setIncludeStatus(new Integer[]{BusinessEntity.BUS_STATUS.ERROR_FIRST, BusinessEntity.BUS_STATUS.ERROR_SECOND, BusinessEntity.BUS_STATUS.ERROR_THIRD});
        return pagingBaseUser(query);
    }

    @Transactional
    @Override
    public PagingModel pagingError(BusinessQuery query) throws ServiceException {
        //query预处理
//        if (query.getAmendmentIssue() != null && query.getAmendmentIssue()) {   //统计已整改的记录，这里任何一个阶段完成整改均算作该业务记录整改过。
//            query.setIncAmendmentCode(new Integer[]{7, 6, 5, 4, 3, 2, 1});
//        } else {
            query.setIncludeStatus(new Integer[]{BusinessEntity.BUS_STATUS.ERROR_FIRST, BusinessEntity.BUS_STATUS.ERROR_SECOND, BusinessEntity.BUS_STATUS.ERROR_THIRD, BusinessEntity.BUS_STATUS.FINISH});  // 修改为包含已经整改的业务，这里添加状态 5 但是由于query中同时指定了 第一次、第二次或第三次检查是必须包含错误，这样就避免了把没有错误的 处于完成状态的业务也统计进来
//        }


        PagingModel result = new PagingModel();
        List<BusinessModel> businessModels = new ArrayList<>();
        result.setRows(businessModels);
        try {
            List<BusinessEntity> businessEntities = businessDao.pagingError(query);
            businessEntities.forEach(businessEntity -> {
                BusinessModel businessModel = new BusinessModel();
                BeanUtils.copyProperties(businessEntity, businessModel);

                AgencyEntity agencyEntity = businessEntity.getAgency();
                if (agencyEntity != null) {
                    businessModel.setAgencyId(agencyEntity.getId());
                    businessModel.setAgencyName(agencyEntity.getName());
                }

                BusCategoryEntity categoryEntity = businessEntity.getCategory();
                if (categoryEntity != null) {
                    businessModel.setCategoryId(categoryEntity.getId());
                    businessModel.setCategoryName(categoryEntity.getName());

                    BusCategoryTypeEntity categoryTypeEntity = categoryEntity.getType();
                    if (categoryTypeEntity != null) {
                        businessModel.setCategoryTypeId(categoryTypeEntity.getId());
                        businessModel.setCategoryTypeName(categoryTypeEntity.getName());
                    }
                }

                ExamineEntity first = businessEntity.getFirstExamine();
                convertIssue(first, businessModel, businessModel::setFirstHasIssue, businessModel::setFirstExamine);

                ExamineEntity second = businessEntity.getSecondExamine();
                convertIssue(second, businessModel, businessModel::setSecondHasIssue, businessModel::setSecondExamine);

                ExamineEntity third = businessEntity.getThirdExamine();
                convertIssue(third, businessModel, businessModel::setThirdHasIssue, businessModel::setThirdExamine);

                UserEntity create = businessEntity.getCreate();
                if (create != null) {
                    businessModel.setCreateId(create.getId());
                    businessModel.setCreateName(create.getName());
                }

                UserEntity check = businessEntity.getCheck();
                if (check != null) {
                    businessModel.setCheckId(check.getId());
                    businessModel.setCheckName(check.getName());
                }

                UserEntity finalCheck = businessEntity.getFinalCheck();
                if (finalCheck != null) {
                    businessModel.setFinalCheckId(finalCheck.getId());
                    businessModel.setFinalCheckName(finalCheck.getName());
                }

                businessModels.add(businessModel);
            });
        } catch (Exception e) {
            logger.error("", e);
            throw new ServiceException();
        }

        result.setTotal(businessDao.getCount(query));
        return result;
    }

    @Transactional
    @Override
    public PagingModel pagingBaseUser(BusinessQuery query) throws ServiceException {
        queryBasicAssert(query);
        try {
            UserEntity curUser = userDao.getById(query.getUserId());
            if (UserType.NORMAL.equals(curUser.getType())) {                     //非管理员
                if (curUser.getAgencyBoss()) {
                    Set<UserEntity> agencyUsers = curUser.getAgency().getUsers();
                    query.setCreateUserIds(getIds(agencyUsers));
                } else if (curUser.getAgency().getChildren() != null) {          //县局
                    Set<String> childrenUserIds = new HashSet<>();
                    for (AgencyEntity a : curUser.getAgency().getChildren()) {
                        childrenUserIds.addAll(getIds(a.getUsers()));
                    }
                    childrenUserIds.add(curUser.getId());                       // 县局用户自己，o(￣▽￣)ｄ 似乎没什么用
                    query.setCreateUserIds(childrenUserIds);
                } else {
                    query.setCreateUserId(curUser.getId());
                }
            }
        } catch (Exception e) {
            logger.error("", e);
            throw new ServiceException();
        }
        return paging(query);
    }


    @Transactional
    @Override
    public PagingModel paging(BusinessQuery query) throws ServiceException {

        queryBasicAssert(query);

        List<BusinessModel> businessModels = new ArrayList<>();
        PagingModel paging = new PagingModel();
        Long total = null;

        try {
            List<BusinessEntity> businessEntities = businessDao.paging(query);
            for (BusinessEntity businessEntity : businessEntities) {

                BusinessModel businessModel = new BusinessModel();
                BeanUtils.copyProperties(businessEntity, businessModel);

                AgencyEntity agencyEntity = businessEntity.getAgency();
                if (agencyEntity != null) {
                    businessModel.setAgencyId(agencyEntity.getId());
                    businessModel.setAgencyName(agencyEntity.getName());
                }

                BusCategoryEntity categoryEntity = businessEntity.getCategory();
                if (categoryEntity != null) {
                    businessModel.setCategoryId(categoryEntity.getId());
                    businessModel.setCategoryName(categoryEntity.getName());

                    BusCategoryTypeEntity categoryTypeEntity = categoryEntity.getType();
                    if (categoryTypeEntity != null) {
                        businessModel.setCategoryTypeId(categoryTypeEntity.getId());
                        businessModel.setCategoryTypeName(categoryTypeEntity.getName());
                    }
                }

                if (businessEntity.getFirstHasIssue() != null) {
                    businessModel.setFirstHasIssue(businessEntity.getFirstHasIssue());
                } else {
                    ExamineEntity first = businessEntity.getFirstExamine();
                    convertIssue(first, businessModel, businessModel::setFirstHasIssue, businessModel::setFirstExamine);
                }

                if (businessEntity.getSecondHasIssue() != null) {
                    businessModel.setSecondHasIssue(businessEntity.getSecondHasIssue());
                } else {
                    ExamineEntity second = businessEntity.getSecondExamine();
                    convertIssue(second, businessModel, businessModel::setSecondHasIssue, businessModel::setSecondExamine);
                }

                if (businessEntity.getThirdHasIssue() != null) {
                    businessModel.setThirdHasIssue(businessEntity.getThirdHasIssue());
                } else {
                    ExamineEntity third = businessEntity.getThirdExamine();
                    convertIssue(third, businessModel, businessModel::setThirdHasIssue, businessModel::setThirdExamine);
                }

                UserEntity create = businessEntity.getCreate();
                if (create != null) {
                    businessModel.setCreateId(create.getId());
                    businessModel.setCreateName(create.getName());
                }

                UserEntity check = businessEntity.getCheck();
                if (check != null) {
                    businessModel.setCheckId(check.getId());
                    businessModel.setCheckName(check.getName());
                }

                UserEntity finalCheck = businessEntity.getFinalCheck();
                if (finalCheck != null) {
                    businessModel.setFinalCheckId(finalCheck.getId());
                    businessModel.setFinalCheckName(finalCheck.getName());
                }

                businessModels.add(businessModel);
            }
            total = businessDao.getCount(query);
        } catch (Exception e) {
            logger.error("", e);
            throw new ServiceException();
        }

        paging.setRows(businessModels);
        paging.setTotal(total);
        return paging;
    }

    /**
     * 转换问题名称
     *
     * @param examine
     * @param bus
     */
    private void convertIssue(ExamineEntity examine, BusinessModel bus, Consumer<Boolean> hasIssue, Consumer<ExamineModel> setExa) {
        if (examine == null) {
            return;
        }
        hasIssue.accept(examine.getHasIssue());
        ExamineModel examineModel = new ExamineModel();
        BeanUtils.copyProperties(examine, examineModel);
        Set<BusIssueEntity> issues = examine.getIssues();
        if (issues != null) {
            Set<BusIssueModel> busIssueModels = new HashSet<>();
            for (BusIssueEntity issue : issues) {
                BusIssueModel busIssueModel = new BusIssueModel();
                BeanUtils.copyProperties(issue, busIssueModel);
                busIssueModels.add(busIssueModel);
            }
            examineModel.setIssues(busIssueModels);
        }
        setExa.accept(examineModel);

    }

    @Override
    @Transactional
    public void add(BusinessModel businessModel) throws ServiceException {

        if (businessModel == null) {
            throw new ServiceException("业务对象不能为空！");
        }

        BusinessEntity businessEntity = new BusinessEntity();

        BeanUtils.copyProperties(businessModel, businessEntity);

        try {
            if (!StringTools.isEmpty(businessModel.getCategoryId())) {
                BusCategoryEntity categoryEntity = businessCategoryDao.getById(businessModel.getCategoryId());
                businessEntity.setCategory(categoryEntity);
            }
            if (!StringTools.isEmpty(businessModel.getUserId())) {
                UserEntity create = userDao.getById(businessModel.getUserId());
                businessEntity.setCreate(create);
                businessEntity.setAgency(create.getAgency());
            }
        } catch (Exception e) {
            logger.error("", e);
        }

        businessEntity.setCreateTime(new Date());
        businessEntity.setId(StringTools.randomUUID());
        businessEntity.setStatus(BusinessEntity.BUS_STATUS.CREATE);
        businessDao.save(businessEntity);
    }


    @Override
    @Transactional
    public void edit(BusinessModel business) throws ServiceException {

        try {
            BusinessEntity businessEntity = businessDao.getById(business.getId());
            if (businessEntity == null) {
                throw new ServiceException("ID为" + business.getId() + " 的记录已经不存在。");
            }

            businessEntity.setTaxpayerCode(business.getTaxpayerCode());
            businessEntity.setTaxpayerName(business.getTaxpayerName());
            BusCategoryEntity categoryEntity = businessCategoryDao.getById(business.getCategoryId());
            if (categoryEntity != null) {
                businessEntity.setCategory(categoryEntity);
            }
//            if (business.getHasIssue() && !StringTools.isEmpty(business.getIssueId())) {
//                BusIssueEntity issueEntity = businessIssueDao.getById(business.getIssueId());
//                if (issueEntity != null) {
//                    businessEntity.setIssue(issueEntity);
//                }
//            } else {
//                businessEntity.setHasIssue(false);
//                businessEntity.setIssue(null);
//            }
            businessEntity.setBusTime(business.getBusTime());
            businessEntity.setDescription(business.getDescription());

            businessEntity.setModifyTime(new Date());
            businessDao.save(businessEntity);
        } catch (Exception e) {
            logger.error("", e);
            throw new ServiceException();
        }

    }

    @Override
    @Transactional
    public Integer del(String[] ids, Function<String, String> getAbsPath) throws ServiceException {
        Integer result = 0;
        try {
            for (String id : ids) {
                BusinessEntity businessEntity = businessDao.getById(id);
                Set<BusAttachmentEntity> attachmentEntities = businessEntity.getAttachments();
                if (attachmentEntities != null) {
                    for (BusAttachmentEntity attachmentEntity : attachmentEntities) {
                        UploadTools.del(getAbsPath.apply(attachmentEntity.getUrl()));
                        businessAttachmentDao.delete(attachmentEntity);
                    }
                }
                businessDao.delete(businessEntity);
                result++;
            }

        } catch (Exception e) {
            logger.error("", e);
            throw new ServiceException();
        }
        return result;
    }

    @Override
    @Transactional
    public List<XianjuModel> xianju(XianjuQuery query) throws ServiceException {

        BusinessQuery bQuery = new BusinessQuery();

        bQuery.setBusTimeStart(query.getBusTimeStart());
        bQuery.setBusTimeEnd(query.getBusTimeEnd());
        if (!StringTools.isEmpty(query.getCategoryIdsStr())) {
            bQuery.setIncCategoryIds(query.getCategoryIds());
        }

        List<XianjuModel> sms = new ArrayList<>();

        try {
            for (String agencyId : query.getAgencyIds()) {
                bQuery.setAgencyId(agencyId);
                AgencyEntity agency = agencyDao.getById(agencyId);
                XianjuModel sm = new XianjuModel();
                sm.setAgencyName(agency.getName());
                sm.setAgencyId(agency.getId());
                List<BusinessEntity> bes = businessDao.getList(bQuery);
                List<StatisticsCategoryTypeModel> sctms = new ArrayList<>();

                for (BusinessEntity be : bes) {
                    StatisticsCategoryTypeModel sctmTmp = null;
                    if (be.getCategory() == null) {
                        System.err.print("skip:" + be.getId());
                        continue;
                    }
                    BusCategoryTypeEntity bcte = be.getCategory().getType();

                    for (StatisticsCategoryTypeModel sctm : sctms) {
                        if (sctm.getId().equals(bcte.getId())) {
                            sctmTmp = sctm;
                            break;
                        }
                    }
                    if (sctmTmp == null) {
                        sctmTmp = new StatisticsCategoryTypeModel();
                        sctmTmp.setName(bcte.getName());
                        sctmTmp.setId(bcte.getId());
                        sctms.add(sctmTmp);
                    }

                    BusCategoryEntity bce = be.getCategory();

                    List<StatisticsCategoryModel> scms = sctmTmp.getRecs();
                    StatisticsCategoryModel scmTmp = null;

                    if (scms != null) {
                        for (StatisticsCategoryModel scm : scms) {
                            if (scm.getId().equals(bce.getId())) {
                                scmTmp = scm;
                                break;
                            }
                        }
                    } else {
                        scms = new ArrayList<>();
                        sctmTmp.setRecs(scms);
                    }

                    if (scmTmp == null) {
                        scmTmp = new StatisticsCategoryModel();
                        scmTmp.setName(bce.getName());
                        scmTmp.setId(bce.getId());
                        scms.add(scmTmp);
                    }

                    scmTmp.setCount(scmTmp.getCount() + 1);     // 业务总数

                    boolean hasIssue = false;

                    if (be.getFirstExamine() != null && be.getFirstExamine().getHasIssue()) {
                        hasIssue = true;
                        Set<String> issueNames = new HashSet<>();
                        Set<BusIssueEntity> busIssueEntities = be.getFirstExamine().getIssues();
                        busIssueEntities.forEach(busIssueEntity -> issueNames.add(busIssueEntity.getName()));
                        Set<String> oldIssueNames = scmTmp.getIssueNames();
                        if (oldIssueNames == null) {
                            oldIssueNames = new HashSet<>();
                        }
                        oldIssueNames.addAll(issueNames);
                        scmTmp.setFirstIssueCount(scmTmp.getFirstIssueCount() + 1);
                    }
                    if (be.getSecondExamine() != null && be.getSecondExamine().getHasIssue()) {
                        hasIssue = true;
                        Set<String> issueNames = new HashSet<>();
                        Set<BusIssueEntity> busIssueEntities = be.getSecondExamine().getIssues();
                        busIssueEntities.forEach(busIssueEntity -> issueNames.add(busIssueEntity.getName()));
                        Set<String> oldIssueNames = scmTmp.getIssueNames();
                        if (oldIssueNames == null) {
                            oldIssueNames = new HashSet<>();
                        }
                        oldIssueNames.addAll(issueNames);
                        scmTmp.setSecondIssueCount(scmTmp.getSecondIssueCount() + 1);
                    }
                    if (be.getThirdExamine() != null && be.getThirdExamine().getHasIssue()) {
                        hasIssue = true;
                        Set<String> issueNames = new HashSet<>();
                        Set<BusIssueEntity> busIssueEntities = be.getThirdExamine().getIssues();
                        busIssueEntities.forEach(busIssueEntity -> issueNames.add(busIssueEntity.getName()));
                        Set<String> oldIssueNames = scmTmp.getIssueNames();
                        if (oldIssueNames == null) {
                            oldIssueNames = new HashSet<>();
                        }
                        oldIssueNames.addAll(issueNames);
                        scmTmp.setThirdIssueCount(scmTmp.getThirdIssueCount() + 1);
                    }

                    if (hasIssue && be.getAmendmentCode() != 0) {
                        scmTmp.setAmendmentCount(scmTmp.getAmendmentCount() + 1);   //已整改业务总数
                    }
                    if (hasIssue) {
                        scmTmp.setIssueCount(scmTmp.getIssueCount() + 1);      //问题业务总数
                    }

                    sm.setDetailCount(sm.getDetailCount() + 1);

                }
                sm.setRecs(sctms);
                sms.add(sm);
            }
        } catch (Exception e) {
            logger.error("", e);
            throw new ServiceException();
        }
        return sms;
    }


    @Transactional
    @Override
    public List<FenjuModel> fenju(FenjuQuery query) throws ServiceException {

        BusinessQuery bQuery = new BusinessQuery();

        bQuery.setBusTimeStart(query.getBusTimeStart());
        bQuery.setBusTimeEnd(query.getBusTimeEnd());
        if (!StringTools.isEmpty(query.getCategoryIdsStr())) {
            bQuery.setIncCategoryIds(query.getCategoryIds());
        }
        List<FenjuModel> fms = new ArrayList<>();

        try {
            for (String userId : query.getUserIds()) {
                bQuery.setCreateUserId(userId);
                UserEntity userEntity = userDao.getById(userId);
                FenjuModel fm = new FenjuModel();
                fm.setUserName(userEntity.getName());
                fm.setUserId(userEntity.getId());
                List<BusinessEntity> bes = businessDao.getList(bQuery);
                List<StatisticsCategoryTypeModel> sctms = new ArrayList<>();

                for (BusinessEntity be : bes) {
                    StatisticsCategoryTypeModel sctmTmp = null;
                    if (be.getCategory() == null) {
                        System.err.print("skip:" + be.getId());
                        continue;
                    }
                    BusCategoryTypeEntity bcte = be.getCategory().getType();

                    for (StatisticsCategoryTypeModel sctm : sctms) {
                        if (sctm.getId().equals(bcte.getId())) {
                            sctmTmp = sctm;
                            break;
                        }
                    }
                    if (sctmTmp == null) {
                        sctmTmp = new StatisticsCategoryTypeModel();
                        sctmTmp.setName(bcte.getName());
                        sctmTmp.setId(bcte.getId());
                        sctms.add(sctmTmp);
                    }

                    BusCategoryEntity bce = be.getCategory();

                    List<StatisticsCategoryModel> scms = sctmTmp.getRecs();
                    StatisticsCategoryModel scmTmp = null;

                    if (scms != null) {
                        for (StatisticsCategoryModel scm : scms) {
                            if (scm.getId().equals(bce.getId())) {
                                scmTmp = scm;
                                break;
                            }
                        }
                    } else {
                        scms = new ArrayList<>();
                        sctmTmp.setRecs(scms);
                    }

                    if (scmTmp == null) {
                        scmTmp = new StatisticsCategoryModel();
                        scmTmp.setName(bce.getName());
                        scmTmp.setId(bce.getId());
                        scms.add(scmTmp);
                    }

                    scmTmp.setCount(scmTmp.getCount() + 1);     // 业务总数

                    boolean hasIssue = false;
                    if (be.getFirstExamine() != null && be.getFirstExamine().getHasIssue()) {
                        hasIssue = true;
                        Set<String> issueNames = new HashSet<>();
                        Set<BusIssueEntity> busIssueEntities = be.getFirstExamine().getIssues();
                        busIssueEntities.forEach(busIssueEntity -> issueNames.add(busIssueEntity.getName()));
                        Set<String> oldIssueNames = scmTmp.getIssueNames();
                        if (oldIssueNames == null) {
                            oldIssueNames = new HashSet<>();
                        }
                        oldIssueNames.addAll(issueNames);
                        scmTmp.setFirstIssueCount(scmTmp.getFirstIssueCount() + 1);
                    }
                    if (be.getSecondExamine() != null && be.getSecondExamine().getHasIssue()) {
                        hasIssue = true;
                        Set<String> issueNames = new HashSet<>();
                        Set<BusIssueEntity> busIssueEntities = be.getSecondExamine().getIssues();
                        busIssueEntities.forEach(busIssueEntity -> issueNames.add(busIssueEntity.getName()));
                        Set<String> oldIssueNames = scmTmp.getIssueNames();
                        if (oldIssueNames == null) {
                            oldIssueNames = new HashSet<>();
                        }
                        oldIssueNames.addAll(issueNames);
                        scmTmp.setSecondIssueCount(scmTmp.getSecondIssueCount() + 1);
                    }
                    if (be.getThirdExamine() != null && be.getThirdExamine().getHasIssue()) {
                        hasIssue = true;
                        Set<String> issueNames = new HashSet<>();
                        Set<BusIssueEntity> busIssueEntities = be.getThirdExamine().getIssues();
                        busIssueEntities.forEach(busIssueEntity -> issueNames.add(busIssueEntity.getName()));
                        Set<String> oldIssueNames = scmTmp.getIssueNames();
                        if (oldIssueNames == null) {
                            oldIssueNames = new HashSet<>();
                        }
                        oldIssueNames.addAll(issueNames);
                        scmTmp.setThirdIssueCount(scmTmp.getThirdIssueCount() + 1);
                    }

                    if (hasIssue && be.getAmendmentCode() != 0) {
                        scmTmp.setAmendmentCount(scmTmp.getAmendmentCount() + 1);
                    }
                    if (hasIssue) {
                        scmTmp.setIssueCount(scmTmp.getIssueCount() + 1);      //问题业务总数
                    }

                    fm.setDetailCount(fm.getDetailCount() + 1);

                }
                fm.setRecs(sctms);
                fms.add(fm);
            }
        } catch (Exception e) {
            logger.error("", e);
            throw new ServiceException();
        }
        return fms;
    }


    @Transactional
    @Override
    public List<StatisticsCategoryTypeModel> person(StatisticsQuery query) throws ServiceException {

        BusinessQuery bQuery = new BusinessQuery();

        bQuery.setBusTimeStart(query.getBusTimeStart());
        bQuery.setBusTimeEnd(query.getBusTimeEnd());
        if (!StringTools.isEmpty(query.getCategoryIdsStr())) {
            bQuery.setIncCategoryIds(query.getCategoryIds());
        }
        bQuery.setCreateUserId(query.getUserId());

        List<StatisticsCategoryTypeModel> sctms = new ArrayList<>();
        try {
            List<BusinessEntity> bes = businessDao.getList(bQuery);

            for (BusinessEntity be : bes) {
                StatisticsCategoryTypeModel sctmTmp = null;
                if (be.getCategory() == null) {
                    System.err.print("skip:" + be.getId());
                    continue;
                }
                BusCategoryTypeEntity bcte = be.getCategory().getType();

                for (StatisticsCategoryTypeModel sctm : sctms) {
                    if (sctm.getId().equals(bcte.getId())) {
                        sctmTmp = sctm;
                        break;
                    }
                }
                if (sctmTmp == null) {
                    sctmTmp = new StatisticsCategoryTypeModel();
                    sctmTmp.setName(bcte.getName());
                    sctmTmp.setId(bcte.getId());
                    sctms.add(sctmTmp);
                }

                BusCategoryEntity bce = be.getCategory();

                List<StatisticsCategoryModel> scms = sctmTmp.getRecs();
                StatisticsCategoryModel scmTmp = null;

                if (scms != null) {
                    for (StatisticsCategoryModel scm : scms) {
                        if (scm.getId().equals(bce.getId())) {
                            scmTmp = scm;
                            break;
                        }
                    }
                } else {
                    scms = new ArrayList<>();
                    sctmTmp.setRecs(scms);
                }

                if (scmTmp == null) {
                    scmTmp = new StatisticsCategoryModel();
                    scmTmp.setName(bce.getName());
                    scmTmp.setId(bce.getId());
                    scms.add(scmTmp);
                }

                scmTmp.setCount(scmTmp.getCount() + 1);     // 业务总数

                boolean hasIssue = false;

                if (be.getFirstExamine() != null && be.getFirstExamine().getHasIssue()) {
                    hasIssue = true;
                    Set<String> issueNames = new HashSet<>();
                    Set<BusIssueEntity> busIssueEntities = be.getFirstExamine().getIssues();
                    busIssueEntities.forEach(busIssueEntity -> issueNames.add(busIssueEntity.getName()));
                    Set<String> oldIssueNames = scmTmp.getIssueNames();
                    if (oldIssueNames == null) {
                        oldIssueNames = new HashSet<>();
                    }
                    oldIssueNames.addAll(issueNames);
                    scmTmp.setFirstIssueCount(scmTmp.getFirstIssueCount() + 1);
                }
                if (be.getSecondExamine() != null && be.getSecondExamine().getHasIssue()) {
                    hasIssue = true;
                    Set<String> issueNames = new HashSet<>();
                    Set<BusIssueEntity> busIssueEntities = be.getSecondExamine().getIssues();
                    busIssueEntities.forEach(busIssueEntity -> issueNames.add(busIssueEntity.getName()));
                    Set<String> oldIssueNames = scmTmp.getIssueNames();
                    if (oldIssueNames == null) {
                        oldIssueNames = new HashSet<>();
                    }
                    oldIssueNames.addAll(issueNames);
                    scmTmp.setSecondIssueCount(scmTmp.getSecondIssueCount() + 1);
                }
                if (be.getThirdExamine() != null && be.getThirdExamine().getHasIssue()) {
                    hasIssue = true;
                    Set<String> issueNames = new HashSet<>();
                    Set<BusIssueEntity> busIssueEntities = be.getThirdExamine().getIssues();
                    busIssueEntities.forEach(busIssueEntity -> issueNames.add(busIssueEntity.getName()));
                    Set<String> oldIssueNames = scmTmp.getIssueNames();
                    if (oldIssueNames == null) {
                        oldIssueNames = new HashSet<>();
                    }
                    oldIssueNames.addAll(issueNames);
                    scmTmp.setThirdIssueCount(scmTmp.getThirdIssueCount() + 1);
                }

                if (hasIssue && be.getAmendmentCode() != 0) {
                    scmTmp.setAmendmentCount(scmTmp.getAmendmentCount() + 1);
                }
                if (hasIssue) {
                    scmTmp.setIssueCount(scmTmp.getIssueCount() + 1);      //问题业务总数
                }
            }
        } catch (Exception e) {
            logger.error("", e);
            throw new ServiceException();
        }
        return sctms;
    }

    @Transactional
    @Override
    public void commit(String[] ids) throws ServiceException {
        changeStatus(ids, BusinessEntity.BUS_STATUS.FIRST);
    }

    private void changeStatus(String[] ids, int status) throws ServiceException {
        try {
            for (String id : ids) {
                BusinessEntity business = businessDao.getById(id);
                if (business == null) {
                    throw new ServiceException("业务不存在");
                }
                business.setStatus(status);
                businessDao.update(business);
            }
        } catch (Exception e) {
            logger.error("", e);
            throw new ServiceException();
        }
    }

    @Transactional
    @Override
    public void commitExamine(ExamineModel examine) throws ServiceException {
        if (examine.getStep() == 0) {
            throw new ServiceException("审查阶段错误");
        }

        String busId = examine.getBusId();
        BusinessEntity business;
        try {
            business = businessDao.getById(busId);
        } catch (Exception e) {
            logger.error("", e);
            throw new ServiceException();
        }
        if (business == null) {
            throw new ServiceException("业务不存在");
        }

//        if (BUS_STATUS.HAS_ISSUE == business.getStatus()) {
//            throw new ServiceException("业务已经被标记为有问题");
//        }

//        if (examine.getStep() + 1 == business.getStatus()) {
//            throw new ServiceException("业务已经检查过");
//        }

        ExamineEntity examineEntity = new ExamineEntity();
        examineEntity.setId(StringTools.randomUUID());
        examineEntity.setHasIssue(examine.getHasIssue());

        String issueIdStr = examine.getIssueIdStrs();
        if (!StringTools.isEmpty(issueIdStr)) {
            String[] issueIds = issueIdStr.split(",");
            Set<BusIssueEntity> issues = businessIssueDao.getByIds(Arrays.asList(issueIds));
            examineEntity.setIssues(issues);
            examineEntity.setDescription(examine.getDescription());
        }

        switch (examine.getStep()) {
            case BusinessEntity.BUS_STATUS.FIRST:
                business.setFirstExamine(examineEntity);
                break;
            case BusinessEntity.BUS_STATUS.SECOND:
                business.setSecondExamine(examineEntity);
                break;
            case BusinessEntity.BUS_STATUS.THIRD:
                business.setThirdExamine(examineEntity);
                break;
            default:
                throw new ServiceException("业务阶段码错误");
        }

        if (examine.getHasIssue()) {
            business.setStatus(-examine.getStep()); //设置为对应阶段的错误代码
//            business.setAmendment(false);   //初始化整改状态为否
        } else {
            business.setStatus(BusinessEntity.BUS_STATUS.next(examine.getStep()));
        }

        //设置业务整改状态为未整改（包含有问题和没问题）
        business.setAmendmentCode(BusinessEntity.BusAmendment.setAmendment(examine.getStep(), business.getAmendmentCode(), false));

        try {
            businessDao.update(business);
        } catch (Exception e) {
            logger.error("", e);
            throw new ServiceException();
        }


    }

    @Transactional
    @Override
    public void commitAmendment(String[] ids) throws ServiceException {

        try {
            for (String id : ids) {
                BusinessEntity business = businessDao.getById(id);
                if (business == null) {
                    throw new ServiceException("业务不存在");
                }
                int step = -business.getStatus();
                business.setStatus(BusinessEntity.BUS_STATUS.next(business.getStatus())); //错误状态到下一个状态
//                business.setAmendment(true);
                business.setAmendmentCode(BusinessEntity.BusAmendment.setAmendment(step, business.getAmendmentCode(), true));
                businessDao.update(business);
            }
        } catch (Exception e) {
            logger.error("", e);
            throw new ServiceException();
        }
    }

    @Transactional
    @Override
    public ExamineModel examineDetail(String busId, String step) throws ServiceException {
        ExamineModel examineModel = new ExamineModel();
        try {
            BusinessEntity businessEntity = businessDao.getById(busId);
            ExamineEntity examineEntity = null;
            if (Integer.toString(BusinessEntity.BUS_STATUS.FIRST).equals(step)) {
                examineEntity = businessEntity.getFirstExamine();
            } else if (Integer.toString(BusinessEntity.BUS_STATUS.SECOND).equals(step)) {
                examineEntity = businessEntity.getSecondExamine();
            } else if (Integer.toString(BusinessEntity.BUS_STATUS.THIRD).equals(step)) {
                examineEntity = businessEntity.getThirdExamine();
            }
            if (examineEntity != null) {
                BeanUtils.copyProperties(examineEntity, examineModel);
                Set<BusIssueEntity> issues = examineEntity.getIssues();
                if (issues != null) {
                    Set<BusIssueModel> busIssueModels = new HashSet<>();
                    for (BusIssueEntity issue : issues) {
                        BusIssueModel busIssueModel = new BusIssueModel();
                        BeanUtils.copyProperties(issue, busIssueModel);
                        busIssueModels.add(busIssueModel);
                    }
                    examineModel.setIssues(busIssueModels);
                }
            }
        } catch (Exception e) {
            logger.error("", e);
            throw new ServiceException();
        }
        return examineModel;
    }

    @Transactional
    @Override
    public List<BusAttachmentModel> listAttachment(String busId) throws ServiceException {
        List<BusAttachmentModel> result = new ArrayList<>();
        try {
            BusinessEntity businessEntity = businessDao.getById(busId);
            Set<BusAttachmentEntity> attachmentEntities = businessEntity.getAttachments();
            if (attachmentEntities != null) {
                attachmentEntities.forEach(busAttachmentEntity -> {
                    BusAttachmentModel busAttachmentModel = new BusAttachmentModel();
                    BeanUtils.copyProperties(busAttachmentEntity, busAttachmentModel);
                    result.add(busAttachmentModel);
                });
            }
            result.sort(Comparator.comparing(BusAttachmentModel::getFileName));
            return result;
        } catch (Exception e) {
            logger.error("", e);
            throw new ServiceException();
        }
    }
}
