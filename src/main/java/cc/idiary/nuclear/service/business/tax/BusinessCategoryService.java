package cc.idiary.nuclear.service.business.tax;

import cc.idiary.nuclear.entity.business.tax.BusCategoryEntity;
import cc.idiary.nuclear.model.business.tax.BusCategoryMode;
import cc.idiary.nuclear.service.BaseService;
import cc.idiary.nuclear.service.ServiceException;

import java.util.List;

/**
 * Created by Neo on 2017/5/10.
 */
public interface BusinessCategoryService extends BaseService<BusCategoryEntity> {
    List<BusCategoryMode> list(String typeId) throws ServiceException;
}
