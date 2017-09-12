package cc.idiary.nuclear.controller.business.tax;

import cc.idiary.nuclear.controller.BaseController;
import cc.idiary.nuclear.model.Json;
import cc.idiary.nuclear.service.ServiceException;
import cc.idiary.nuclear.service.business.tax.BusinessCategoryTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Neo on 2017/5/10.
 */
@Controller
@RequestMapping("tax/business/category/type")
public class BusCategoryTypeController extends BaseController {

    @Autowired
    private BusinessCategoryTypeService businessCategoryTypeService;

    @RequestMapping("list")
    @ResponseBody
    public Json list(){
        try {
            return success(businessCategoryTypeService.list());
        } catch (ServiceException e) {
            return fail();
        }
    }
}
