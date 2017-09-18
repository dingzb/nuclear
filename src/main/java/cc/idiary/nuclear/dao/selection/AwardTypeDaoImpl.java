package cc.idiary.nuclear.dao.selection;

import cc.idiary.nuclear.dao.BaseDaoImpl;
import cc.idiary.nuclear.entity.selection.AwardTypeEntity;
import cc.idiary.nuclear.query.BaseQuery;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository("awardTypeDao")
public class AwardTypeDaoImpl extends BaseDaoImpl<AwardTypeEntity> implements AwardTypeDao {
    public AwardTypeDaoImpl() {
        super(AwardTypeEntity.class);
    }

    @Override
    protected StringBuilder getExtensionSql(BaseQuery query, Map<String, Object> params) {
        //Not support yet.
        return null;
    }
}
