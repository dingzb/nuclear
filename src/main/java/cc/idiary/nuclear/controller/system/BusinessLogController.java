package cc.idiary.nuclear.controller.system;

import cc.idiary.nuclear.controller.BaseController;
import cc.idiary.nuclear.model.Json;
import cc.idiary.nuclear.service.ServiceException;
import cc.idiary.nuclear.query.system.BusinessLogQuery;
import cc.idiary.nuclear.service.system.BusinessLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("logController")
@RequestMapping("system/log/business")
public class BusinessLogController extends BaseController {

	@Autowired
	private BusinessLogService businessLogService;

	/**
	 * 获取分页数据
	 * 
	 * @param query
	 * @return
	 */
	@RequestMapping("paging")
	@ResponseBody
	public Json getPaging(BusinessLogQuery query) {
		try {
			return success(businessLogService.getPaging(query));
		} catch (ServiceException e) {
			return fail(e.getMessage());
		}
	}
	
	/**
	 * 逻辑删除
	 *
	 * @return
	 */
	@RequestMapping("del")
	@ResponseBody
	public Json logicalDelete(@RequestParam("param[]") String param[]) {
		try {
			businessLogService.logicalDelete(param);
		} catch (ServiceException e) {
			e.printStackTrace();
			return fail(e.getMessage());
		}
		return success("删除成功！");
	}

	@RequestMapping("time/set")
	@ResponseBody
	public Json setTime(@RequestParam("time") String time) {
		try {
			businessLogService.setTime(time);
			return success();
		} catch (ServiceException e) {
			return fail(e);
		}
	}

	@RequestMapping("time/get")
	@ResponseBody
	public Json getTime(){
		try {
			return success (businessLogService.getTime(),"");
		} catch (ServiceException e) {
			return fail(e);
		}
	}
}
