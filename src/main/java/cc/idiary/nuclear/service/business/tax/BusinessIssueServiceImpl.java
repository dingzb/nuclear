package cc.idiary.nuclear.service.business.tax;

import cc.idiary.nuclear.dao.business.tax.BusinessIssueDao;
import cc.idiary.nuclear.entity.business.tax.BusIssueEntity;
import cc.idiary.nuclear.model.business.tax.BusIssueModel;
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
 * Created by Neo on 2017/5/11.
 */

@Service
public class BusinessIssueServiceImpl extends BaseServiceImpl<BusIssueEntity> implements BusinessIssueService {

    private static final Logger logger = LoggerFactory.getLogger(BusinessIssueServiceImpl.class);

    @Autowired
    private BusinessIssueDao businessIssueDao;

    @Override
    @Transactional
    public List<BusIssueModel> list() throws ServiceException {

        List<BusIssueModel> bims = new ArrayList<>();

        try{
            List<BusIssueEntity> bies = businessIssueDao.getList();
            for (BusIssueEntity bie : bies) {
                BusIssueModel bim = new BusIssueModel();
                BeanUtils.copyProperties(bie, bim);
                bims.add(bim);
            }
        }catch (Exception e){
            logger.error("", e);
            throw new ServiceException();
        }

        return bims;
    }
}
