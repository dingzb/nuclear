package cc.idiary.nuclear.service.selection;

import cc.idiary.nuclear.dao.selection.CategoryDao;
import cc.idiary.nuclear.entity.selection.CategoryEntity;
import cc.idiary.nuclear.model.selection.CategoryModel;
import cc.idiary.nuclear.service.BaseServiceImpl;
import cc.idiary.nuclear.service.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service("categoryService")
public class CategoryServiceImpl extends BaseServiceImpl<CategoryEntity> implements CategoryService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    private final CategoryDao categoryDao;

    @Autowired
    public CategoryServiceImpl(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    @Override
    @Transactional
    public List<CategoryModel> list() throws ServiceException {

        List<CategoryModel> cms = new ArrayList<>();
        try {
            List<CategoryEntity> ces = categoryDao.getList();
            for (CategoryEntity ce : ces) {
                CategoryModel cm = new CategoryModel();
                BeanUtils.copyProperties(ce, cm);
                cms.add(cm);
            }
        } catch (Exception e) {
            logger.error("", e);
            throw new ServiceException();
        }
        return cms;
    }

    @Transactional
    @Override
    public List<CategoryModel> getByCategoryGroup(String cGroupId) throws ServiceException {
        List<CategoryModel> cms = new ArrayList<>();
        try {
            List<CategoryEntity> ces = categoryDao.getByCategoryGroup(cGroupId);
            for (CategoryEntity ce : ces) {
                CategoryModel cm = new CategoryModel();
                cm.setId(ce.getId());
                cm.setCode(ce.getCode());
                cm.setName(ce.getName());
                CategoryEntity parent = ce.getParent();
                if (parent != null) {
                    cm.setParentId(parent.getId());
                }
                cms.add(cm);
            }
        } catch (Exception e) {
            logger.error("", e);
            throw new ServiceException();
        }
        return cms;
    }
}
