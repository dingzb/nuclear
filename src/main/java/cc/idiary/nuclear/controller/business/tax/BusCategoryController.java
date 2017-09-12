package cc.idiary.nuclear.controller.business.tax;

import cc.idiary.nuclear.service.business.tax.BusinessCategoryService;
import cc.idiary.nuclear.service.business.tax.BusinessCategoryTypeService;
import cc.idiary.nuclear.controller.BaseController;
import cc.idiary.nuclear.model.Json;
import cc.idiary.nuclear.service.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Neo on 2017/5/10.
 */
@Controller
@RequestMapping("tax/business/category")
public class BusCategoryController extends BaseController {

    @Autowired
    private BusinessCategoryService businessCategoryService;

    @Autowired
    private BusinessCategoryTypeService businessCategoryTypeService;

    @RequestMapping("list")
    @ResponseBody
    public Json list(@RequestParam(value = "typeId", required = false) String typeId) {
        try {
            return success(businessCategoryService.list(typeId));
        } catch (ServiceException e) {
            return fail(e);
        }
    }

    @RequestMapping("list/all")
    @ResponseBody
    public Json listWithType(){
        try {
            return success(businessCategoryTypeService.listDetail());
        } catch (ServiceException e){
            return fail(e);
        }
    }
}
