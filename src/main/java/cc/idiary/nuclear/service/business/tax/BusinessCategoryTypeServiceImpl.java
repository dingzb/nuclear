package cc.idiary.nuclear.service.business.tax;

import cc.idiary.nuclear.dao.business.tax.BusinessCategoryTypeDao;
import cc.idiary.nuclear.entity.business.tax.BusCategoryTypeEntity;
import cc.idiary.nuclear.model.business.tax.BusCategoryMode;
import cc.idiary.nuclear.model.business.tax.BusCategoryTypeModel;
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
public class BusinessCategoryTypeServiceImpl extends BaseServiceImpl<BusCategoryTypeEntity> implements BusinessCategoryTypeService {

    private static final Logger logger = LoggerFactory.getLogger(BusinessCategoryTypeServiceImpl.class);

    @Autowired
    private BusinessCategoryTypeDao businessCategoryTypeDao;

    @Override
    @Transactional
    public List<BusCategoryTypeModel> list() throws ServiceException {

        List<BusCategoryTypeModel> bcms = new ArrayList<>();

        try {
            List<BusCategoryTypeEntity> bces = businessCategoryTypeDao.getList();

            for (BusCategoryTypeEntity bce : bces) {
                BusCategoryTypeModel bcm = new BusCategoryTypeModel();
                BeanUtils.copyProperties(bce, bcm);
                bcms.add(bcm);
            }
        } catch (Exception e) {
            logger.error("",e);
            throw new ServiceException();
        }
        return bcms;
    }

    @Transactional
    @Override
    public List<BusCategoryTypeModel> listDetail() throws ServiceException {
        List<BusCategoryTypeModel> result =new ArrayList<>();
        try {
            List<BusCategoryTypeEntity> categoryTypeEntities = businessCategoryTypeDao.getList();

            categoryTypeEntities.forEach(busCategoryTypeEntity -> {
                BusCategoryTypeModel busCategoryTypeModel = new BusCategoryTypeModel();
                BeanUtils.copyProperties(busCategoryTypeEntity, busCategoryTypeModel);
                List<BusCategoryMode> busCategoryModes = new ArrayList<>();
                busCategoryTypeEntity.getCategories().forEach(busCategoryEntity -> {
                    BusCategoryMode busCategoryMode = new BusCategoryMode();
                    BeanUtils.copyProperties(busCategoryEntity, busCategoryMode);
                    busCategoryModes.add(busCategoryMode);
                });
                busCategoryTypeModel.setCategories(busCategoryModes);
                result.add(busCategoryTypeModel);
            });
        } catch (Exception e){
            logger.error("", e);
            throw new ServiceException();
        }

        return  result;
    }
}
