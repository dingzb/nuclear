package cc.idiary.nuclear.controller.selection;

import cc.idiary.nuclear.controller.BaseController;
import cc.idiary.nuclear.model.Json;
import cc.idiary.nuclear.model.selection.ActivityModel;
import cc.idiary.nuclear.model.selection.CheckCriterionModel;
import cc.idiary.nuclear.query.selection.CheckCriterionQuery;
import cc.idiary.nuclear.service.ServiceException;
import cc.idiary.nuclear.service.selection.ActivityService;
import cc.idiary.nuclear.service.selection.CheckCriterionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("ActConfigController")
@RequestMapping("activity/config")
public class ActConfigController extends BaseController {
    private final ActivityService activityService;
    private final CheckCriterionService checkCriterionService;

    @Autowired
    public ActConfigController(ActivityService activityService, CheckCriterionService checkCriterionService) {
        this.activityService = activityService;
        this.checkCriterionService = checkCriterionService;
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

    @RequestMapping("criterion/paging")
    @ResponseBody
    public Json checkCriterionPaging(CheckCriterionQuery query){
        try {
            return success(checkCriterionService.paging(query));
        } catch (ServiceException e) {
            return fail(e);
        }
    }

    @RequestMapping("criterion/add")
    @ResponseBody
    public Json addCheckCriterion(CheckCriterionModel checkCriterion){
        try {
            checkCriterionService.add(checkCriterion);
            return success("添加成功");
        } catch (ServiceException e) {
            return fail(e);
        }
    }

    @RequestMapping("criterion/edit")
    @ResponseBody
    public Json editCheckCriterion(CheckCriterionModel checkCriterion){
        try {
            checkCriterionService.edit(checkCriterion);
            return success("编辑成功");
        } catch (ServiceException e) {
            return fail(e);
        }
    }

    @RequestMapping("criterion/del")
    @ResponseBody
    public Json delCheckCriterion(@RequestParam("ids[]") String[] ids){
        try {
            checkCriterionService.del(ids);
            return success("删除成功");
        } catch (ServiceException e) {
            return fail(e);
        }
    }

    @RequestMapping("criterion/exist")
    @ResponseBody
    public Json criterionExistByName(String name){
        try {
            return success(checkCriterionService.existByName(name));
        } catch (ServiceException e) {
            return fail();
        }
    }
}
