package cc.idiary.nuclear.dao.selection;

import cc.idiary.nuclear.dao.BaseDao;
import cc.idiary.nuclear.entity.selection.ExpertEntity;
import cc.idiary.nuclear.query.selection.ExpertQuery;

import java.util.List;

public interface ExpertDao extends BaseDao<ExpertEntity>{
    List<ExpertEntity> paging(ExpertQuery query);
}
