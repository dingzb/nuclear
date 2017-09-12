package cc.idiary.nuclear.controller;

import cc.idiary.nuclear.config.AttrName;
import cc.idiary.nuclear.config.HttpMethods;
import cc.idiary.nuclear.config.UserType;
import cc.idiary.nuclear.model.Json;
import cc.idiary.nuclear.model.system.ActionModel;
import cc.idiary.nuclear.model.system.AuthenticateModel;
import cc.idiary.nuclear.model.system.StateModel;
import cc.idiary.nuclear.model.system.UserModel;
import cc.idiary.nuclear.service.ServiceException;
import cc.idiary.nuclear.service.system.ActionService;
import cc.idiary.nuclear.service.system.StateService;
import cc.idiary.nuclear.service.system.UserService;
import cc.idiary.nuclear.utils.VerifyCodeTools;
import cc.idiary.utils.common.StringTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Dzb
 */
@Controller("mainController")
public class MainController extends BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private StateService stateService;
    @Autowired
    private ActionService actionService;
    @Autowired
    private MessageSource messageSource;

    // 验证码
    private static final String VERIFY_CODE_IMG = "VERIFY_CODE_IMG";

    @RequestMapping("root")
    @ResponseBody
    public Json root(HttpServletRequest request) {
        return success(request.getServletContext().getContextPath());
    }

    /**
     * 权限信息
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "authorization", method = RequestMethod.POST)
    @ResponseBody
    public Json authorization(HttpServletRequest request,
                              HttpServletResponse response) {

        UserModel user = (UserModel) WebUtils.getSessionAttribute(request,
                AttrName.SESSION_USER);
        if (user == null)
            return fail("没有登录，或者登录超时");

        return authorization(request, user);
    }

    /**
     * 获取必要的权限信息
     *
     * @param request
     * @param user
     * @return
     */
    private Json authorization(HttpServletRequest request, UserModel user) {

        AuthenticateModel authorization = new AuthenticateModel();

        // 存储actionCode,返回到angularjs
        List<String> codes = new ArrayList<String>();
        // 存储action，存储到session，供权限转发器使用
        Map<String, String> actions = new HashMap<String, String>();
        // 存储statCode,返回到angularjs，存储到session，用于获取目录
        List<String> states = new ArrayList<String>();

        List<StateModel> stateModels = null;
        List<ActionModel> actionModels = null;

        // 内置用户
        if (UserType.BUILD_IN.toString().equals(user.getType())) {
            try {
                stateModels = stateService.getList();
                actionModels = actionService.getList();
            } catch (ServiceException e) {
                return fail();
            }
            // 常规用户
        } else {
            try {
                stateModels = stateService.getByUserId(user.getId());
                actionModels = actionService.getByUserId(user.getId());
            } catch (ServiceException e) {
                return fail();
            }
        }

        for (StateModel stateModel : stateModels) {
            states.add(stateModel.getCode());
        }

        for (ActionModel actionModel : actionModels) {
            if (!StringTools.isEmpty(actionModel.getUrl())) {
                actions.put(actionModel.getUrl(),
                        HttpMethods.valueOfint(actionModel.getMethod()));
            }
            if (!StringTools.isEmpty(actionModel.getCode())) {
                codes.add(actionModel.getCode());
            }
        }

        authorization.setUser(user);
        authorization.setPermCodes(codes);
        authorization.setPermStates(states);

        WebUtils.setSessionAttribute(request, AttrName.SESSION_ACTIONS, actions);
        WebUtils.setSessionAttribute(request, AttrName.SESSION_STATES,
                stateModels);

        return success(authorization);
    }

    /**
     * 登录
     *
     * @param username
     * @param password
     * @param verify
     * @param request
     * @return
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    public Json login(@RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestParam("verify") String verify,
                      HttpServletRequest request) {
        String innerCode = (String) request.getSession().getAttribute(VERIFY_CODE_IMG);
        if (innerCode == null || !innerCode.equalsIgnoreCase(verify)) {
            return fail("验证码错误");
        }

        UserModel user;
//        if (BaseInfo.USER_SUPREME.getUsername().equals(username)) {
//            if (BaseInfo.USER_SUPREME.getPassword().equals(
//                    StringTools.Md5(password))) {
//                user = BaseInfo.USER_SUPREME;
//            } else {
//                return fail("内置用户登录失败");
//            }
//        } else {
        try {
            user = userService.login(username, password);
        } catch (ServiceException e) {
            return fail(e.getMessage());
        }
//        }

        WebUtils.setSessionAttribute(request, AttrName.SESSION_USER, user);
        return authorization(request, user);
    }

    @RequestMapping(value = "logout", method = RequestMethod.POST)
    @ResponseBody
    public Json logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return success();
    }

    @RequestMapping("error/{code:\\d{3}}")
    public String error(@PathVariable("code") String code) {
        System.out.println(code);
        return "error/404";
    }

    @RequestMapping("verifyCode/verify")
    @ResponseBody
    public Json verifyCode(HttpServletRequest request, @RequestParam("code") String code) {
        try {
            String innerCode = (String) request.getSession().getAttribute(VERIFY_CODE_IMG);
            if (innerCode != null && innerCode.equalsIgnoreCase(code)) {
                return success();
            }
        } catch (Exception e) {
            return fail();
        }
        return fail();
    }

    /**
     * @param request
     * @param response
     */
    @RequestMapping("verifyCode/image")
    public void verifyImage(HttpServletRequest request, HttpServletResponse response) {

        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");

        String verifyCode = null;
        try {
            verifyCode = VerifyCodeTools.generateVerifyCode(4);
            WebUtils.setSessionAttribute(request, VERIFY_CODE_IMG, verifyCode);
            VerifyCodeTools.outputImage(200, 80, response.getOutputStream(), verifyCode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
