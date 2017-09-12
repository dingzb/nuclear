package cc.idiary.nuclear.service.business.tax;

import cc.idiary.nuclear.dao.business.tax.AgencyDao;
import cc.idiary.nuclear.dao.system.UserDao;
import cc.idiary.nuclear.entity.business.tax.AgencyEntity;
import cc.idiary.nuclear.entity.system.UserEntity;
import cc.idiary.nuclear.model.business.tax.AgencyModel;
import cc.idiary.nuclear.model.system.UserModel;
import cc.idiary.nuclear.query.business.tax.AgencyQuery;
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
 * Created by Neo on 2017/5/12.
 */

@Service
public class AgencyServiceImpl extends BaseServiceImpl<AgencyEntity> implements AgencyService {

    private final static Logger logger = LoggerFactory.getLogger(AgencyServiceImpl.class);

    @Autowired
    private AgencyDao agencyDao;

    @Autowired
    private UserDao userDao;

    @Override
    @Transactional
    public List<AgencyModel> list(String level) throws ServiceException {

        List<AgencyModel> ams = new ArrayList<>();

        try {
            List<AgencyEntity> aes = agencyDao.getList(level);

            for (AgencyEntity ae : aes) {
                AgencyModel am = new AgencyModel();
                BeanUtils.copyProperties(ae, am);
                //TODO 多级机构时如何在编辑时展示？，目前暂时如下解决
                if (ae.getParent() == null) {
                    am.setLevel(0);
                }
                ams.add(am);
            }
        } catch (Exception e) {
            logger.error("", e);
            throw new ServiceException();
        }

        return ams;
    }

    @Transactional
    @Override
    public List<UserModel> userList(AgencyQuery query) throws ServiceException {
        List<UserModel> result = new ArrayList<>();
        List<UserEntity> userEntities = new ArrayList<>();
        try {
            UserEntity current = userDao.getById(query.getUserId());
            if (current.getAgency().getChildren() != null && !current.getAgency().getChildren().isEmpty()){
                current.getAgency().getChildren().forEach(agencyEntity -> {
                    agencyEntity.getUsers().forEach(userEntity -> {
                        if (!userEntity.getAgencyBoss()){
                            userEntities.add(userEntity);
                        }
                    });
                });
            } else {
                current.getAgency().getUsers().forEach(userEntity -> {
                    if (!userEntity.getAgencyBoss()){
                        userEntities.add(userEntity);
                    }
                });
            }

            userEntities.forEach(userEntity -> {
                UserModel userModel = new UserModel();
                BeanUtils.copyProperties(userEntity, userModel);
                result.add(userModel);
            });
            return result;
        } catch (Exception e){
            logger.error("", e);
            throw new ServiceException();
        }
    }

    @Transactional
    @Override
    public AgencyModel current(AgencyQuery query) throws ServiceException {
        try {
            AgencyEntity agencyEntity = userDao.getById(query.getUserId()).getAgency();
            AgencyModel agencyModel = new AgencyModel();
            BeanUtils.copyProperties(agencyEntity, agencyModel);
            return agencyModel;
        } catch (Exception e){
            logger.error("",e);
            throw new ServiceException();
        }
    }
}
