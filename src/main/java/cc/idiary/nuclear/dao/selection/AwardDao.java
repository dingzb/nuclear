package cc.idiary.nuclear.dao.selection;

import cc.idiary.nuclear.dao.BaseDao;
import cc.idiary.nuclear.entity.selection.AwardEntity;
import cc.idiary.nuclear.query.selection.AwardQuery;

import java.util.List;

public interface AwardDao extends BaseDao<AwardEntity> {
    /**
     *
     * @param query
     * @return
     */
    List<AwardEntity> paging(AwardQuery query);

    /**
     *
     * @param name
     * @return
     */
    boolean existByName(String name);
}
