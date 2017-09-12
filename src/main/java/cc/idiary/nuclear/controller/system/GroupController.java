package cc.idiary.nuclear.controller.system;

import cc.idiary.nuclear.config.AttrName;
import cc.idiary.nuclear.query.system.GroupQuery;
import cc.idiary.nuclear.service.system.GroupService;
import cc.idiary.nuclear.controller.BaseController;
import cc.idiary.nuclear.model.Json;
import cc.idiary.nuclear.model.PagingModel;
import cc.idiary.nuclear.model.system.GroupModel;
import cc.idiary.nuclear.model.system.UserModel;
import cc.idiary.nuclear.service.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Dzb
 */
@Controller("groupController")
@RequestMapping("system/group")
public class GroupController extends BaseController {

    @Autowired
    private GroupService groupService;

    /**
     * 获取组列表
     * @param query
     * @param request
     * @return
     */
    @RequestMapping("paging")
    @ResponseBody
    public Json getPaging(GroupQuery query, HttpServletRequest request) {
        try {
            PagingModel paging = groupService.getPaging(query);
            return success(paging);
        } catch (ServiceException e) {
            return fail(e.getMessage());
        }
    }

    /**
     * 添加组
     *
     * @since 2015-11-9
     * @param groupModel
     * @return
     */
    @RequestMapping("add")
    @ResponseBody
    public Json addGroup(GroupModel groupModel, HttpServletRequest request) {
        if (groupModel == null)
            return fail("添加失败");
        try {
            groupService.addGroup(groupModel);
        } catch (Exception e) {
            return fail(e.getMessage());
        }
        return success("添加成功！");
    }

    /**
     * 根据组名称判断组是否存在
     *
     * @author Neo
     * @date 2015-12-07
     * @param name
     * @return
     */
    @RequestMapping("exist")
    @ResponseBody
    public Json existGroupByName(String name) {
        try {
            return success(groupService.existByName(name));
        } catch (ServiceException e) {
            return fail(e);
        }
    }

    /**
     * 编辑组
     *
     * @since 2015-11-9
     * @param groupModel
     * @return
     */
    @RequestMapping("edit")
    @ResponseBody
    public Json editGroup(GroupModel groupModel) {
        if (groupModel == null)
            return fail("编辑失败");
        try {
            groupService.editGroup(groupModel);
        } catch (Exception e) {
            return fail(e.getMessage());
        }
        return success("编辑成功！");
    }

    /**
     * 删除组
     *
     * @since 2015-11-9
     * @param ids
     * @return
     */
    @RequestMapping("del")
    @ResponseBody
    public Json deleteGroups(@RequestParam("ids[]") String[] ids) {
        try {
            groupService.deleteGroups(ids);
        } catch (ServiceException e) {
            return fail(e.getMessage());
        }
        return success("删除成功！");
    }

    @RequestMapping("types")
    @ResponseBody
    public Json getTypes(HttpServletRequest request) {
        try {
            return success(groupService.getTypes(((UserModel) WebUtils
                    .getSessionAttribute(request, AttrName.SESSION_USER))));
        } catch (ServiceException e) {
            return fail(e);
        }
    }
}
