package cc.idiary.nuclear.controller.system;

import cc.idiary.nuclear.config.AttrName;
import cc.idiary.nuclear.config.BaseInfo;
import cc.idiary.nuclear.controller.BaseController;
import cc.idiary.nuclear.model.Json;
import cc.idiary.nuclear.model.system.UserModel;
import cc.idiary.nuclear.query.system.UserQuery;
import cc.idiary.nuclear.service.ServiceException;
import cc.idiary.nuclear.service.system.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Dzb on 2017/2/28.
 */
@Controller("userController")
@RequestMapping("system/user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    /**
     * 初始化用户列表
     *
     * @param query
     * @return
     */
    @RequestMapping("paging")
    @ResponseBody
    public Json getPaging(UserQuery query) {

        try {
            return success(userService.getPaging(query, null));
        } catch (ServiceException e) {
            return fail(e);
        }
    }

    /**
     * 添加用户
     *
     * @param userModel
     * @return
     */
    @RequestMapping("add")
    @ResponseBody
    public Json addUser(UserModel userModel) {
        if (userModel == null)
            return fail("保存失败");
        try {
            userService.add(userModel);
            return success("保存成功！默认密码为：" + BaseInfo.PASSWORD_DEF);
        } catch (ServiceException e) {
            return fail(e);
        }
    }

    @RequestMapping("types")
    @ResponseBody
    public Json getUserTypes(HttpServletRequest request) {
        try {
            return success(userService.getTypes(((UserModel) WebUtils
                    .getSessionAttribute(request, AttrName.SESSION_USER))));
        } catch (ServiceException e) {
            return fail(e);
        }
    }

    /**
     * 编辑用户
     *
     * @param userModel
     * @return
     */
    @RequestMapping("edit")
    @ResponseBody
    public Json editUserModel(UserModel userModel) {
        if (userModel == null)
            return fail("编辑失败");
        try {
            userService.editUserModel(userModel);
        } catch (ServiceException e) {
            return fail(e.getMessage());
        }
        return success("编辑成功！");
    }

    /**
     * <h2>功能描述</h2>
     * <p>
     * 删除用户
     * </p>
     *
     * @param
     * @return
     * @author lcuwang
     * @since 2015-07-25
     */
    @RequestMapping("del")
    @ResponseBody
    public Json deleteUsers(String userId) {
        try {
            userService.removeUser(userId);
        } catch (ServiceException e) {
            return fail(e.getMessage());
        }
        return success("删除成功！");
    }

    @RequestMapping("group/add")
    @ResponseBody
    public Json userAddGroups(@RequestParam("id") String id, @RequestParam("ids[]") String[] ids) {
        try {
            userService.addGroups(id, ids);
            return success();
        } catch (ServiceException e) {
            return fail(e);
        }
    }

    @RequestMapping("group/remove")
    @ResponseBody
    public Json userRemoveGroups(@RequestParam("id") String id, @RequestParam("ids[]") String[] ids) {
        try {
            userService.removeGroups(id, ids);
            return success();
        } catch (ServiceException e) {
            return fail(e);
        }
    }

    @RequestMapping("role/add")
    @ResponseBody
    public Json userAddRoles(@RequestParam("id") String id, @RequestParam("ids[]") String[] ids) {
        try {
            userService.addRoles(id, ids);
            return success();
        } catch (ServiceException e) {
            return fail(e);
        }
    }

    @RequestMapping("role/remove")
    @ResponseBody
    public Json userRemoveRoles(@RequestParam("id") String id, @RequestParam("ids[]") String[] ids) {
        try {
            userService.removeRoles(id, ids);
            return success();
        } catch (ServiceException e) {
            return fail(e);
        }
    }


    /**
     * 获取用户信息
     *
     * @param userQuery
     */
    @RequestMapping("info")
    @ResponseBody
    public Json getInfo(UserQuery userQuery) {
        UserModel userModel;
        try {
            userModel = userService.getById(userQuery.getUserId());
        } catch (ServiceException e) {
            return fail(e.getMessage());
        }
        return success(userModel);
    }

    /**
     * 修改密码
     *
     * @param userQuery
     * @return
     */
    @RequestMapping("password/modify")
    @ResponseBody
    public Json modPassWord(HttpServletRequest request, UserQuery userQuery) {
        try {
            String newPasswd = request.getParameter("newPasswd");
            String confirmPasswd = request.getParameter("confirmPasswd");
            userService.modPassWord(userQuery.getUserId(), userQuery.getPassword(), newPasswd, confirmPasswd);
        } catch (ServiceException e) {
            return fail(e);
        }
        return success("修改成功！");
    }


    /**
     * 重置密码
     *
     * @param request
     * @return
     * @Since 2015-07-07
     */
    @RequestMapping("password/reset")
    @ResponseBody
    public Json initPassWord(HttpServletRequest request) {
        //TODO
        return success("修改成功！");
    }
}
