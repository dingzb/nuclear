package cc.idiary.nuclear.controller.selection.activity;

import cc.idiary.nuclear.controller.BaseController;
import cc.idiary.nuclear.model.Json;
import cc.idiary.nuclear.query.selection.ExpertQuery;
import cc.idiary.nuclear.service.ServiceException;
import cc.idiary.nuclear.service.selection.ExpertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("ActUserController")
@RequestMapping("activity/user")
public class UserController extends BaseController {

    private final ExpertService expertService;

    @Autowired
    public UserController(ExpertService expertService) {
        this.expertService = expertService;
    }

    @RequestMapping("expert/paging")
    @ResponseBody
    public Json pagingExpert(ExpertQuery query){
        try {
            return success(expertService.paging(query));
        } catch (ServiceException e) {
            return fail(e);
        }
    }
}
