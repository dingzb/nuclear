package cc.idiary.nuclear.controller.selection;

import cc.idiary.nuclear.controller.BaseController;
import cc.idiary.nuclear.model.Json;
import cc.idiary.nuclear.model.selection.ActivityModel;
import cc.idiary.nuclear.service.ServiceException;
import cc.idiary.nuclear.service.selection.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("activityController")
@RequestMapping("activity")
public class ActivityController extends BaseController {

    private final ActivityService activityService;

    @Autowired
    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }


    /**
     * 获取当前正在进行的活动，用来在活动子系统中判断是否要激活活动的一系列操作。
     *
     * @return
     */
    @RequestMapping("current")
    @ResponseBody
    public Json current() {
        try {
            return success(activityService.current());
        } catch (ServiceException e) {
            return fail(e);
        }
    }

    @RequestMapping(value = "current.js")
    @ResponseBody
    public String currentJs() {
        StringBuilder js = new StringBuilder("curAct = {};");
        Json current = current();
        if (current.getSuccess() && current.getData() != null) {
            ActivityModel curAct = ((ActivityModel)current().getData());
            js.append("\ncurAct.id = '").append(curAct.getId()).append("'");
        }
        return js.toString();
    }
}
