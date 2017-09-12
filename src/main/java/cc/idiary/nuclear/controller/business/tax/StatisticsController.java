package cc.idiary.nuclear.controller.business.tax;

import cc.idiary.nuclear.query.business.tax.FenjuQuery;
import cc.idiary.nuclear.query.business.tax.StatisticsQuery;
import cc.idiary.nuclear.query.business.tax.XianjuQuery;
import cc.idiary.nuclear.service.business.tax.BusinessService;
import cc.idiary.nuclear.controller.BaseController;
import cc.idiary.nuclear.model.Json;
import cc.idiary.nuclear.service.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Neo on 2017/5/11.
 * 统计
 */

@Controller
@RequestMapping("tax/statistics")
public class StatisticsController extends BaseController{

    @Autowired
    private BusinessService businessService;

    @ResponseBody
    @RequestMapping("xianju")
    public Json xianju(XianjuQuery query){
        try {
            return success(businessService.xianju(query));
        } catch (ServiceException e) {
            return fail();
        }
    }

    @ResponseBody
    @RequestMapping("fenju")
    public Json fenju(FenjuQuery query){
        try {
            return success(businessService.fenju(query));
        } catch (ServiceException e) {
            return fail();
        }
    }

    @ResponseBody
    @RequestMapping("person")
    public Json person(StatisticsQuery query){
        try {
            return success(businessService.person(query));
        } catch (ServiceException e) {
            return fail();
        }
    }
}
