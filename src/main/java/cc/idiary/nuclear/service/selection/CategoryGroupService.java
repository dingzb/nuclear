package cc.idiary.nuclear.service.selection;

import cc.idiary.nuclear.entity.selection.CategoryGroupEntity;
import cc.idiary.nuclear.model.PagingModel;
import cc.idiary.nuclear.model.selection.CategoryGroupModel;
import cc.idiary.nuclear.query.selection.CategoryGroupQuery;
import cc.idiary.nuclear.service.BaseService;
import cc.idiary.nuclear.service.ServiceException;

public interface CategoryGroupService extends BaseService<CategoryGroupEntity> {

    PagingModel paging(CategoryGroupQuery query) throws ServiceException;
    void add(CategoryGroupModel categoryGroup) throws ServiceException;
    void edit(CategoryGroupModel categoryGroup) throws ServiceException;
    void del(String[] ids) throws ServiceException;
    boolean existByName(String name) throws ServiceException;
}
