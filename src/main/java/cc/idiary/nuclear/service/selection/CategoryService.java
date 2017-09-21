package cc.idiary.nuclear.service.selection;

import cc.idiary.nuclear.entity.selection.CategoryEntity;
import cc.idiary.nuclear.model.selection.CategoryModel;
import cc.idiary.nuclear.service.BaseService;
import cc.idiary.nuclear.service.ServiceException;

import java.util.List;

public interface CategoryService extends BaseService<CategoryEntity> {

    List<CategoryModel> list() throws ServiceException;

    List<CategoryModel> getByCategoryGroup(String cGroupId) throws ServiceException;
}
