package cc.idiary.nuclear.dao.selection;

import cc.idiary.nuclear.dao.BaseDaoImpl;
import cc.idiary.nuclear.entity.selection.ExpertEntity;
import cc.idiary.nuclear.query.BaseQuery;
import cc.idiary.nuclear.query.selection.ExpertQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("expertDao")
public class ExpertDaoImpl extends BaseDaoImpl<ExpertEntity> implements ExpertDao {
    public ExpertDaoImpl() {
        super(ExpertEntity.class);
    }

    @Override
    protected StringBuilder getExtensionSql(BaseQuery query, Map<String, Object> params) {
        return null;
    }

    @Override
    public List<ExpertEntity> paging(ExpertQuery query) {
        return null;
    }
}
