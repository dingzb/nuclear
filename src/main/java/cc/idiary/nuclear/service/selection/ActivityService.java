package cc.idiary.nuclear.service.selection;

import cc.idiary.nuclear.entity.selection.ActivityEntity;
import cc.idiary.nuclear.model.selection.ActivityModel;
import cc.idiary.nuclear.service.BaseService;
import cc.idiary.nuclear.service.ServiceException;

import java.util.List;

public interface ActivityService extends BaseService<ActivityEntity> {

    /**
     * 创建活动
     *
     * @param activity
     * @return
     * @throws ServiceException
     */
    boolean create(ActivityModel activity) throws ServiceException;

    /**
     * 获取当前正在进行的活动
     *
     * @return
     * @throws ServiceException
     */
    ActivityModel current() throws ServiceException;

    /**
     * 获取所有活动列表
     *
     * @return
     * @throws ServiceException
     */
    List<ActivityModel> list() throws ServiceException;
//    List<StageModel> stages();

}
