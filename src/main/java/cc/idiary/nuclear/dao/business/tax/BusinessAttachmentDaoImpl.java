package cc.idiary.nuclear.dao.business.tax;

import cc.idiary.nuclear.dao.BaseDaoImpl;
import cc.idiary.nuclear.entity.business.tax.BusAttachmentEntity;
import cc.idiary.nuclear.query.BaseQuery;
import cc.idiary.nuclear.service.ServiceException;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * Created by Neo on 2017/6/1.
 */

@Repository
public class BusinessAttachmentDaoImpl extends BaseDaoImpl<BusAttachmentEntity> implements BusinessAttachmentDao {

    public BusinessAttachmentDaoImpl() {
        super(BusAttachmentEntity.class);
    }

    @Override
    protected StringBuilder getExtensionSql(BaseQuery query, Map<String, Object> params) {
        return null;
    }
}
