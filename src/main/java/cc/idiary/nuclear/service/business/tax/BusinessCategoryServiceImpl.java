package cc.idiary.nuclear.service.business.tax;

import cc.idiary.nuclear.dao.business.tax.BusinessCategoryDao;
import cc.idiary.nuclear.entity.business.tax.BusCategoryEntity;
import cc.idiary.nuclear.model.business.tax.BusCategoryMode;
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

/**
 * Created by Neo on 2017/5/10.
 */

@Service
public class BusinessCategoryServiceImpl extends BaseServiceImpl<BusCategoryEntity> implements BusinessCategoryService {

    private static final Logger logger = LoggerFactory.getLogger(BusinessCategoryServiceImpl.class);

    @Autowired
    private BusinessCategoryDao businessCategoryDao;

    @Override
    @Transactional
    public List<BusCategoryMode> list(String typeId) throws ServiceException {

        List<BusCategoryMode> bcms = new ArrayList<>();

        try {
            List<BusCategoryEntity> bces = businessCategoryDao.list(typeId);
            for (BusCategoryEntity bce : bces) {
                BusCategoryMode bcm = new BusCategoryMode();
                BeanUtils.copyProperties(bce, bcm);
                bcm.setTypeId(bce.getType().getId());
                bcms.add(bcm);
            }
        } catch (Exception e) {
            logger.error("", e);
            throw new ServiceException();
        }
        return bcms;
    }
}
