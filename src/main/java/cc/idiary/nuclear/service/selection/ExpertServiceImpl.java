package cc.idiary.nuclear.service.selection;

import cc.idiary.nuclear.dao.selection.ExpertDao;
import cc.idiary.nuclear.entity.selection.ExpertEntity;
import cc.idiary.nuclear.model.PagingModel;
import cc.idiary.nuclear.query.selection.ExpertQuery;
import cc.idiary.nuclear.service.BaseServiceImpl;
import cc.idiary.nuclear.service.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("expertService")
public class ExpertServiceImpl extends BaseServiceImpl<ExpertEntity> implements ExpertService {
    private static final Logger logger = LoggerFactory.getLogger(ExpertServiceImpl.class);

    private final ExpertDao expertDao;

    @Autowired
    public ExpertServiceImpl(ExpertDao expertDao) {
        this.expertDao = expertDao;
    }

    @Override
    public PagingModel paging(ExpertQuery query) throws ServiceException {
        expertDao.paging(query);
        return null;
    }
}
