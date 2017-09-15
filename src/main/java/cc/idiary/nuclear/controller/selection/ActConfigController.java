package cc.idiary.nuclear.controller.selection;

import cc.idiary.nuclear.controller.BaseController;
import cc.idiary.nuclear.model.Json;
import cc.idiary.nuclear.model.selection.ActivityModel;
import cc.idiary.nuclear.service.ServiceException;
import cc.idiary.nuclear.service.selection.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("ActConfigController")
@RequestMapping("activity/config")
public class ActConfigController extends BaseController {
    private final ActivityService activityService;

    @Autowired
    public ActConfigController(ActivityService activityService) {
        this.activityService = activityService;
    }

    /**
     * 创建活动
     *
     * @param activity
     * @return
     */
    @RequestMapping("create/create")
    @ResponseBody
    public Json create(ActivityModel activity) {
        try {
            activityService.create(activity);
            return success("创建成功");
        } catch (ServiceException e) {
            return fail(e);
        }
    }

    @RequestMapping("create/list")
    @ResponseBody
    public Json list() {
        try {
            return success(activityService.list());
        } catch (ServiceException e) {
            return fail(e);
        }
    }
}
