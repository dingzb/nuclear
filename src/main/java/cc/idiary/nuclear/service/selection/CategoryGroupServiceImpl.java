package cc.idiary.nuclear.service.selection;

import cc.idiary.nuclear.config.ActivityStage;
import cc.idiary.nuclear.dao.selection.ActivityDao;
import cc.idiary.nuclear.dao.selection.CategoryDao;
import cc.idiary.nuclear.dao.selection.CategoryGroupDao;
import cc.idiary.nuclear.entity.selection.ActivityEntity;
import cc.idiary.nuclear.entity.selection.CategoryEntity;
import cc.idiary.nuclear.entity.selection.CategoryGroupEntity;
import cc.idiary.nuclear.entity.selection.ExpertEntity;
import cc.idiary.nuclear.model.PagingModel;
import cc.idiary.nuclear.model.selection.CategoryGroupModel;
import cc.idiary.nuclear.query.selection.CategoryGroupQuery;
import cc.idiary.nuclear.service.BaseServiceImpl;
import cc.idiary.nuclear.service.ServiceException;
import cc.idiary.utils.common.StringTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service("categoryGroupService")
public class CategoryGroupServiceImpl extends BaseServiceImpl<CategoryGroupEntity> implements CategoryGroupService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryGroupServiceImpl.class);

    private final CategoryGroupDao categoryGroupDao;
    private final ActivityDao activityDao;
    private final CategoryDao categoryDao;

    @Autowired
    public CategoryGroupServiceImpl(CategoryGroupDao categoryGroupDao, ActivityDao activityDao, CategoryDao categoryDao) {
        this.categoryGroupDao = categoryGroupDao;
        this.activityDao = activityDao;
        this.categoryDao = categoryDao;
    }

    @Override
    @Transactional
    public PagingModel paging(CategoryGroupQuery query) throws ServiceException {
        if (query.getCurrent()) {
            try {
                ActivityEntity curAct = activityDao.current();
                if (curAct == null) {
                    throw new ServiceException("当前没有正在进行的活动");
                }
                query.setActivityId(curAct.getId());
            } catch (Exception e) {
                logger.error("", e);
                throw new ServiceException();
            }
        }
        PagingModel page = new PagingModel();
        List<CategoryGroupModel> cgms = new ArrayList<>();
        try {
            List<CategoryGroupEntity> cges = categoryGroupDao.paging(query);
            for (CategoryGroupEntity cge : cges) {
                CategoryGroupModel cgm = new CategoryGroupModel();
                BeanUtils.copyProperties(cge, cgm);
                Set<ExpertEntity> experts = cge.getExperts();
                if (experts != null) {
                    cgm.setExpertCount(cge.getExperts().size());
                }
                Set<CategoryEntity> categories = cge.getCategories();
                if (categories != null){
                    List<String> codes = new ArrayList<>();
                    for (CategoryEntity c : categories) {
                        codes.add(c.getCode());
                    }
                    cgm.setCategoryCodes(codes);
                }
                cgms.add(cgm);
            }
            page.setRows(cgms);
            page.setTotal(categoryGroupDao.getCount(query));
        } catch (Exception e) {
            logger.error("", e);
            throw new ServiceException();
        }
        return page;
    }

    @Override
    @Transactional
    public void add(CategoryGroupModel categoryGroup) throws ServiceException {
        if (StringTools.isEmpty(categoryGroup.getName())) {
            throw new ServiceException("名称不能为空");
        }
        ActivityEntity curAct = activityCheck(categoryGroup.getActivityId());
        CategoryGroupEntity cge = new CategoryGroupEntity();
        cge.setId(StringTools.randomUUID());
        cge.setName(categoryGroup.getName());
        cge.setDescription(categoryGroup.getDescription());
        cge.setLimitFirst(categoryGroup.getLimitFirst());
        cge.setLimitSecond(categoryGroup.getLimitSecond());
        cge.setLimitThird(categoryGroup.getLimitThird());

        try {
            cge.setActivity(curAct);
            categoryGroupDao.save(cge);
        } catch (Exception e) {
            logger.error("", e);
            throw new ServiceException();
        }
    }

    @Override
    @Transactional
    public void edit(CategoryGroupModel categoryGroup) throws ServiceException {
        if (StringTools.isEmpty(categoryGroup.getId())) {
            throw new ServiceException("奖项不存在");
        }
        if (StringTools.isEmpty(categoryGroup.getName())) {
            throw new ServiceException("名称不能为空");
        }

        CategoryGroupEntity cge = null;
        try {
            cge = categoryGroupDao.getById(categoryGroup.getId());
        } catch (Exception e) {
            logger.error("", e);
            throw new ServiceException();
        }
        if (cge == null) {
            throw new ServiceException("专家专业组不存在");
        }

        ActivityEntity curAct = activityCheck(cge.getActivity().getId());

        cge.setName(categoryGroup.getName());
        cge.setDescription(categoryGroup.getDescription());
        cge.setDescription(categoryGroup.getDescription());
        cge.setLimitFirst(categoryGroup.getLimitFirst());
        cge.setLimitSecond(categoryGroup.getLimitSecond());
        cge.setLimitThird(categoryGroup.getLimitThird());

        try {
            categoryGroupDao.update(cge);
        } catch (Exception e) {
            logger.error("", e);
            throw new ServiceException();
        }
    }

    @Override
    @Transactional
    public void del(String[] ids) throws ServiceException {
        try {
            for (String id : ids) {
                CategoryGroupEntity categoryGroup = categoryGroupDao.getById(id);
                if (categoryGroup != null) {
                    categoryGroupDao.delete(categoryGroup);
                }
            }
        } catch (Exception e) {
            logger.error("", e);
            throw new ServiceException();
        }
    }

    @Override
    @Transactional
    public boolean existByName(String name) throws ServiceException {
        try {
            return categoryGroupDao.existByName(name);
        } catch (Exception e) {
            logger.error("", e);
            throw new ServiceException();
        }
    }


    @Override
    @Transactional
    public void addCategory(String categoryGroupId, String categoryId) throws ServiceException {
        if (StringTools.isEmpty(categoryGroupId) || StringTools.isEmpty(categoryId)) {
            throw new ServiceException("专家专业组或专业不能为空");
        }

        CategoryGroupEntity cge = null;

        try {
            cge = categoryGroupDao.getById(categoryGroupId);
        } catch (Exception e) {
            logger.error("", e);
            throw new ServiceException();
        }

        if (cge == null) {
            throw new ServiceException("专家专业组不存在");
        }

        CategoryEntity ce = null;
        try {
            ce =  categoryDao.getById(categoryId);
        } catch (Exception e) {
            logger.error("", e);
            throw new ServiceException();
        }
        if (ce == null) {
            throw new ServiceException("专业代码不能为空");
        }

        try {
            Set<CategoryEntity> ces = cge.getCategories();
            if (ces == null) {
                ces = new HashSet<>();
            }
            ces.add(ce);
            cge.setCategories(ces);
            categoryGroupDao.update(cge);
        } catch (Exception e) {
            logger.error("", e);
            throw new ServiceException();
        }
    }

    @Override
    @Transactional
    public void removeCategory(String categoryGroupId, String categoryId) throws ServiceException {
        if (StringTools.isEmpty(categoryGroupId) || StringTools.isEmpty(categoryId)) {
            throw new ServiceException("专家专业组或专业不能为空");
        }

        CategoryGroupEntity cge = null;

        try {
            cge = categoryGroupDao.getById(categoryGroupId);
        } catch (Exception e) {
            logger.error("", e);
            throw new ServiceException();
        }

        if (cge == null) {
            throw new ServiceException("专家专业组不存在");
        }

        CategoryEntity ce = null;
        try {
            ce =  categoryDao.getById(categoryId);
        } catch (Exception e) {
            logger.error("", e);
            throw new ServiceException();
        }
        if (ce == null) {
            throw new ServiceException("专业代码不能为空");
        }

        try {
            Set<CategoryEntity> ces = cge.getCategories();
            if (ces != null) {
                CategoryEntity deleted = null;
                for (CategoryEntity c : ces) {
                    if (categoryId.equals(c.getId())){
                        deleted = c;
                    }
                }
                ces.remove(deleted);
            }
            cge.setCategories(ces);
            categoryGroupDao.update(cge);
        } catch (Exception e) {
            logger.error("", e);
            throw new ServiceException();
        }
    }

    /**
     * 判断活动状态是否满足添加或修改
     *
     * @throws ServiceException
     */
    private ActivityEntity activityCheck(String activityId) throws ServiceException {
        ActivityEntity curAct = null;
        try {
            curAct = activityDao.current();
        } catch (Exception e) {
            logger.error("", e);
            throw new ServiceException();
        }
        if (curAct == null || !curAct.getId().equals(activityId)) {
            throw new ServiceException("当前活动不是正在进行的活动");
        }
        if (curAct.getStage() > ActivityStage.COMMIT.getValue()) {
            throw new ServiceException("当前活动已经开始提交项目，不可以配置专家专业组");
        }

        return curAct;
    }
}
