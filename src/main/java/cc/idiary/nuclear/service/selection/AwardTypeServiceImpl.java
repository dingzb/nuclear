package cc.idiary.nuclear.service.selection;

import cc.idiary.nuclear.dao.selection.AwardTypeDao;
import cc.idiary.nuclear.entity.selection.AwardTypeEntity;
import cc.idiary.nuclear.model.selection.AwardTypeModel;
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

@Service("awardTypeService")
public class AwardTypeServiceImpl extends BaseServiceImpl<AwardTypeEntity> implements AwardTypeService {

    private static final Logger logger = LoggerFactory.getLogger(AwardTypeServiceImpl.class);

    private final AwardTypeDao awardTypeDao;

    @Autowired
    public AwardTypeServiceImpl(AwardTypeDao awardTypeDao) {
        this.awardTypeDao = awardTypeDao;
    }

    @Override
    @Transactional
    public List<AwardTypeModel> list() throws ServiceException {
        List<AwardTypeModel> atms = new ArrayList<>();
        try{
            List <AwardTypeEntity> ates = awardTypeDao.getList();
            for (AwardTypeEntity ate : ates) {
                AwardTypeModel atm = new AwardTypeModel();
                BeanUtils.copyProperties(ate, atm);
                atms.add(atm);
            }
        } catch (Exception e) {
            logger.error("", e);
            throw new ServiceException();
        }
        return atms;
    }
}
