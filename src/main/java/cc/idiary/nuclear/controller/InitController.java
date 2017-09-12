package cc.idiary.nuclear.controller;

import cc.idiary.nuclear.service.InitService;
import cc.idiary.nuclear.service.InitServiceImpl;
import cc.idiary.nuclear.controller.BaseController;
import cc.idiary.nuclear.model.Json;
import cc.idiary.nuclear.service.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller("initController")
@RequestMapping("init")
public class InitController extends BaseController {

	@Autowired
	private InitService initService;

	@RequestMapping("")
	public String index() {
		// TODO 判断数据库中是否有数据
		return "init/index";
	}

	@RequestMapping("init")
	@ResponseBody
	public Json init(HttpServletRequest req) {
		InitServiceImpl.InitProcess process = new InitServiceImpl.InitProcess();
		req.getServletContext().setAttribute("INIT_PROCESS", process);
		try {
			initService.init(process);
			return success("Init success!");
		} catch (ServiceException e) {
			return fail(e);
		}
	}

	@RequestMapping("process")
	@ResponseBody
	public Json initProcess (HttpServletRequest req) {
		InitServiceImpl.InitProcess process = (InitServiceImpl.InitProcess) req.getServletContext().getAttribute("INIT_PROCESS");
		return success(process);
	}

}
