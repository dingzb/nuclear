package cc.idiary.nuclear.controller.system;

import cc.idiary.nuclear.model.system.ActionModel;
import cc.idiary.nuclear.query.system.ActionQuery;
import cc.idiary.nuclear.controller.BaseController;
import cc.idiary.nuclear.model.Json;
import cc.idiary.nuclear.model.PagingModel;
import cc.idiary.nuclear.service.ServiceException;
import cc.idiary.nuclear.service.system.ActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Dzb on 2017/3/1.
 */

/**
 * Created by Dzb on 2017/3/1.
 */
@Controller("actionController")
@RequestMapping("system/action")
public class ActionController extends BaseController {
    @Autowired
    private ActionService actionService;
    /**
     *
     * @param action
     * @return
     */
    @RequestMapping("add")
    @ResponseBody
    public Json addAction(ActionModel action) {
        try {
            actionService.add(action);
            return success("添加成功");
        } catch (ServiceException e) {
            return fail(e.getMessage());
        }
    }

    /**
     * 删除state
     *
     * @param ids
     * @return
     */
    @RequestMapping("del")
    @ResponseBody
    public Json delAction(@RequestParam("ids[]") String ids[]) {
        try {
            actionService.delete(ids);
        } catch (ServiceException e) {
            return fail(e.getMessage());
        }
        return success("删除成功");
    }

    /**
     *
     * @param action
     * @return
     */
    @RequestMapping("edit")
    @ResponseBody
    public Json editAction(ActionModel action) {
        try {
            actionService.edit(action);
            return success("修改成功");
        } catch (ServiceException e) {
            return fail(e.getMessage());
        }
    }

    /**
     * 获取action分页列表
     *
     * @param query
     * @return
     */
    @RequestMapping("paging")
    @ResponseBody
    public Json getPagingAction(ActionQuery query) {
        try {
            PagingModel paging = actionService.getPaging(query);
            return success(paging);
        } catch (ServiceException e) {
            return fail(e.getMessage());
        }
    }
}
