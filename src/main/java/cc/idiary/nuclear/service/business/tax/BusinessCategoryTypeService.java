package cc.idiary.nuclear.service.business.tax;

import cc.idiary.nuclear.entity.business.tax.BusCategoryTypeEntity;
import cc.idiary.nuclear.model.business.tax.BusCategoryTypeModel;
import cc.idiary.nuclear.service.BaseService;
import cc.idiary.nuclear.service.ServiceException;

import java.util.List;

/**
 * Created by Neo on 2017/5/10.
 */
public interface BusinessCategoryTypeService extends BaseService<BusCategoryTypeEntity> {
    List<BusCategoryTypeModel> list() throws ServiceException;
    List<BusCategoryTypeModel> listDetail() throws ServiceException;
}
