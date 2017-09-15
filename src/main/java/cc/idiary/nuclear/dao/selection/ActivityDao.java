package cc.idiary.nuclear.dao.selection;

import cc.idiary.nuclear.dao.BaseDao;
import cc.idiary.nuclear.entity.selection.ActivityEntity;

public interface ActivityDao extends BaseDao<ActivityEntity> {
    Boolean existByUsername(String name);

    /**
     * 获取当前正在进行的活动
     * @return
     */
    ActivityEntity current();
}
