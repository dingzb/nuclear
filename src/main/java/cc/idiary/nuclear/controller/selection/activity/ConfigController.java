package cc.idiary.nuclear.controller.selection.activity;

import cc.idiary.nuclear.controller.BaseController;
import cc.idiary.nuclear.model.Json;
import cc.idiary.nuclear.model.selection.*;
import cc.idiary.nuclear.query.selection.AwardQuery;
import cc.idiary.nuclear.query.selection.CategoryGroupQuery;
import cc.idiary.nuclear.query.selection.CheckCriterionQuery;
import cc.idiary.nuclear.service.ServiceException;
import cc.idiary.nuclear.service.selection.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("ActConfigController")
@RequestMapping("activity/config")
public class ConfigController extends BaseController {
    private final ActivityService activityService;
    private final CheckCriterionService checkCriterionService;
    private final AwardService awardService;
    private final AwardTypeService awardTypeService;
    private final AwardCriterionService awardCriterionService;
    private final CategoryGroupService categoryGroupService;
    private final CategoryService categoryService;

    @Autowired
    public ConfigController(ActivityService activityService, CheckCriterionService checkCriterionService, AwardService awardService, AwardTypeService awardTypeService, AwardCriterionService awardCriterionService, CategoryGroupService categoryGroupService, CategoryService categoryService) {
        this.activityService = activityService;
        this.checkCriterionService = checkCriterionService;
        this.awardService = awardService;
        this.awardTypeService = awardTypeService;
        this.awardCriterionService = awardCriterionService;
        this.categoryGroupService = categoryGroupService;
        this.categoryService = categoryService;
    }

    //////// create activity

    /**
     * 创建活动
     *
     * @param activity
     * @return
     */
    @RequestMapping("create/create")
    @ResponseBody
    public Json createActivity(ActivityModel activity) {
        try {
            activityService.create(activity);
            return success("创建成功");
        } catch (ServiceException e) {
            return fail(e);
        }
    }

    @RequestMapping("create/list")
    @ResponseBody
    public Json listActivity() {
        try {
            return success(activityService.list());
        } catch (ServiceException e) {
            return fail(e);
        }
    }

    //奖项显示配置
    @RequestMapping("level/limit/get")
    @ResponseBody
    public Json getLevelLimit(String activityId) {
        try {
            return success(activityService.getLevelLimit(activityId));
        } catch (ServiceException e) {
            return fail(e);
        }
    }

    @RequestMapping("level/limit/edit")
    @ResponseBody
    public Json editLevelLimit(ActivityModel activity) {
        try {
            activityService.editLevelLimit(activity);
            return success("更新成功");
        } catch (ServiceException e) {
            return fail(e);
        }
    }

    //// 审核标准
    @RequestMapping("check/criterion/paging")
    @ResponseBody
    public Json pagingCheckCriterion(CheckCriterionQuery query) {
        try {
            return success(checkCriterionService.paging(query));
        } catch (ServiceException e) {
            return fail(e);
        }
    }

    @RequestMapping("check/criterion/add")
    @ResponseBody
    public Json addCheckCriterion(CheckCriterionModel checkCriterion) {
        try {
            checkCriterionService.add(checkCriterion);
            return success("添加成功");
        } catch (ServiceException e) {
            return fail(e);
        }
    }

    @RequestMapping("check/criterion/edit")
    @ResponseBody
    public Json editCheckCriterion(CheckCriterionModel checkCriterion) {
        try {
            checkCriterionService.edit(checkCriterion);
            return success("编辑成功");
        } catch (ServiceException e) {
            return fail(e);
        }
    }

    @RequestMapping("check/criterion/del")
    @ResponseBody
    public Json delCheckCriterion(@RequestParam("ids[]") String[] ids) {
        try {
            checkCriterionService.del(ids);
            return success("删除成功");
        } catch (ServiceException e) {
            return fail(e);
        }
    }

    @RequestMapping("check/criterion/exist")
    @ResponseBody
    public Json existByNameCriterion(String name) {
        try {
            return success(checkCriterionService.existByName(name));
        } catch (ServiceException e) {
            return fail();
        }
    }

    //////奖项管理
    @RequestMapping("award/paging")
    @ResponseBody
    public Json pagingAward(AwardQuery query) {
        try {
            return success(awardService.paging(query));
        } catch (ServiceException e) {
            return fail(e);
        }
    }

    @RequestMapping("award/add")
    @ResponseBody
    public Json addAward(AwardModel award) {
        try {
            awardService.add(award);
            return success("添加成功");
        } catch (ServiceException e) {
            return fail(e);
        }
    }

    @RequestMapping("award/edit")
    @ResponseBody
    public Json editAward(AwardModel award) {
        try {
            awardService.edit(award);
            return success("编辑成功");
        } catch (ServiceException e) {
            return fail(e);
        }
    }

    @RequestMapping("award/del")
    @ResponseBody
    public Json delAward(@RequestParam("ids[]") String[] ids) {
        try {
            awardService.del(ids);
            return success("删除成功");
        } catch (ServiceException e) {
            return fail(e);
        }
    }

    @RequestMapping("award/exist")
    @ResponseBody
    public Json existByNameAward(String name) {
        try {
            return success(awardService.existByName(name));
        } catch (ServiceException e) {
            return fail();
        }
    }

    @RequestMapping("award/type/list")
    @ResponseBody
    public Json listAwardType() {
        try {
            return success(awardTypeService.list());
        } catch (ServiceException e) {
            return fail();
        }
    }

    @RequestMapping("award/level/edit")
    @ResponseBody
    public Json editAwardLevel(AwardModel award) {
        try {
            awardService.updateLevel(award);
            return success("更新奖项等级成功");
        } catch (ServiceException e) {
            return fail(e);
        }
    }

    @RequestMapping("award/criterion/list")
    @ResponseBody
    public Json listAwardCriterion(@RequestParam("awardId") String awardId) {
        try {
            return success(awardCriterionService.listByAward(awardId));
        } catch (ServiceException e) {
            return fail(e);
        }
    }

    @RequestMapping("award/criterion/add")
    @ResponseBody
    public Json addAwardCriterion(AwardCriterionModel awardCriterion) {
        try {
            awardCriterionService.add(awardCriterion);
            return success("添加成功");
        } catch (ServiceException e) {
            return fail(e);
        }
    }

    @RequestMapping("award/criterion/edit")
    @ResponseBody
    public Json editAwardCriterion(AwardCriterionModel awardCriterion) {
        try {
            awardCriterionService.edit(awardCriterion);
            return success("编辑成功");
        } catch (ServiceException e) {
            return fail(e);
        }
    }

    @RequestMapping("award/criterion/del")
    @ResponseBody
    public Json delAwardCriterion(@RequestParam("id") String id) {
        try {
            awardCriterionService.del(id);
            return success("删除成功");
        } catch (ServiceException e) {
            return fail(e);
        }
    }

    ///专家 专业组配置
    @RequestMapping("category/group/paging")
    @ResponseBody
    public Json pagingCategoryGroup(CategoryGroupQuery query) {
        try {
           return success(categoryGroupService.paging(query));
        } catch (ServiceException e) {
            return fail(e);
        }
    }

    @RequestMapping("category/group/add")
    @ResponseBody
    public Json addCategoryGroup(CategoryGroupModel categoryGroup) {
        try {
            categoryGroupService.add(categoryGroup);
            return success("添加成功");
        } catch (ServiceException e) {
            return fail(e);
        }
    }

    @RequestMapping("category/group/edit")
    @ResponseBody
    public Json editCategoryGroup(CategoryGroupModel categoryGroup) {
        try {
            categoryGroupService.edit(categoryGroup);
            return success("编辑成功");
        } catch (ServiceException e) {
            return fail(e);
        }
    }

    @RequestMapping("category/group/del")
    @ResponseBody
    public Json delCategoryGroup(@RequestParam("ids[]") String[] ids) {
        try {
            categoryGroupService.del(ids);
            return success("删除成功");
        } catch (ServiceException e) {
            return fail(e);
        }
    }

    @RequestMapping("category/group/exist")
    @ResponseBody
    public Json existByNameCategoryGroup(String name) {
        try {
            return success(categoryGroupService.existByName(name));
        } catch (ServiceException e) {
            return fail();
        }
    }

    @RequestMapping("category/list")
    @ResponseBody
    public Json listCategory() {
        try {
            return success(categoryService.list());
        } catch (ServiceException e) {
            return fail();
        }
    }
}
