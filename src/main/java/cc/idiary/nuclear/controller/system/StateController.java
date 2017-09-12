package cc.idiary.nuclear.controller.system;

import cc.idiary.nuclear.config.AttrName;
import cc.idiary.nuclear.model.system.UserModel;
import cc.idiary.nuclear.query.system.StateQuery;
import cc.idiary.nuclear.controller.BaseController;
import cc.idiary.nuclear.model.Json;
import cc.idiary.nuclear.model.PagingModel;
import cc.idiary.nuclear.service.ServiceException;
import cc.idiary.nuclear.model.system.StateModel;
import cc.idiary.nuclear.service.system.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Dzb on 2017/3/1.
 */
@Controller("stateController")
@RequestMapping("system/state")
public class StateController extends BaseController {

    @Autowired
    private StateService stateService;

    /**
     * 获取state分页列表
     *
     * @param query
     * @return
     */
    @RequestMapping("paging")
    @ResponseBody
    public Json getPagingState(StateQuery query) {
        try {
            PagingModel paging = stateService.getPaging(query);
            return success(paging);
        } catch (ServiceException e) {
            return fail(e.getMessage());
        }
    }

    /**
     * 添加state
     *
     * @param state
     * @return
     */
    @RequestMapping("add")
    @ResponseBody
    public Json addState(StateModel state) {
        try {
            stateService.add(state);
        } catch (ServiceException e) {
            return fail(e.getMessage());
        }
        return success("添加成功");
    }

    /**
     * 编辑state
     *
     * @param state
     * @return
     */
    @RequestMapping("edit")
    @ResponseBody
    public Json editState(StateModel state) {
        try {
            stateService.edit(state);
        } catch (ServiceException e) {
            return fail(e.getMessage());
        }
        return success();
    }

    /**
     * 删除state
     *
     * @param ids
     * @return
     */
    @RequestMapping("del")
    @ResponseBody
    public Json delState(@RequestParam("ids[]") String ids[]) {
        try {
            stateService.delete(ids);
        } catch (ServiceException e) {
            return fail(e.getMessage());
        }
        return success("删除成功");
    }

    /**
     * 获取state树形结构
     *
     * @return
     */
    @RequestMapping("tree")
    @ResponseBody
    public Json getStateTree(@RequestParam("roleId") String roleId, HttpServletRequest request) {
        List<StateModel> states = null;

        UserModel user = (UserModel) WebUtils.getSessionAttribute(request,
                AttrName.SESSION_USER);
        try {
            states = stateService.treeWithRoleUser(roleId, user.getId());
//            states = stateService.filterByRoleId(roleId, user);
        } catch (ServiceException e) {
            return fail(e.getMessage());
        }
        return success(states);
    }

    /**
     * 获取系统子系统
     *
     * @param request
     * @return
     */
    @RequestMapping("subsystem")
    @ResponseBody
    public Json subsystem(HttpServletRequest request) {

        @SuppressWarnings("unchecked")
        List<StateModel> states = (List<StateModel>) WebUtils
                .getSessionAttribute(request, AttrName.SESSION_STATES);
        /*
		 * 此处采取在session中根据state.type判断方式获取字系统，也可以直接读取数据库
		 */
        List<StateModel> subsystem = new ArrayList<StateModel>();
        for (StateModel state : states) {
            // type==2 为子系统
            if (state.getType() != null && state.getType().equals(2))
                subsystem.add(state);
        }
        Collections.sort(subsystem);
        return success(subsystem);
    }

    @RequestMapping("menus")
    @ResponseBody
    public Json menus(HttpServletRequest request, @RequestParam("code") String code) {

        // 添加主视图前缀
        code = "main." + code;
        @SuppressWarnings("unchecked")
        List<StateModel> states = (List<StateModel>) WebUtils
                .getSessionAttribute(request, AttrName.SESSION_STATES);

        List<StateModel> groups = new ArrayList<StateModel>();

        for (StateModel state : states) {
            // type==1为目录组
            if (state.getCode().contains(code) && state.getType().equals(1)) {
                List<StateModel> stateChildre = new ArrayList<StateModel>();
                for (StateModel inner : states) {
                    // type==0为普通视图stat
                    if (inner.getCode().contains(state.getCode())
                            && inner.getType().equals(0)) {
                        stateChildre.add(inner);
                    }
                }
                Collections.sort(stateChildre);
                state.setChildren(stateChildre);
                groups.add(state);
            }
        }
        Collections.sort(groups);
        return success(groups);
    }

    /**
     * 根据Id获取最高至子系统的父节点列表
     *
     * @param id
     * @return
     */
    @RequestMapping("hierarchysubsystem")
    @ResponseBody
    public Json getHierarchySubsystemById(String id) {
        List<StateModel> stateList = null;
        try {
            stateList = stateService.getHierarchySubsystemById(id);
        } catch (ServiceException e) {
            return fail(e.getMessage());
        }
        return success(stateList);
    }
}
