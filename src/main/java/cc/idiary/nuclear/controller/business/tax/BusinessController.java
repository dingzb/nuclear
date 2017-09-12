package cc.idiary.nuclear.controller.business.tax;

import cc.idiary.nuclear.model.FileInputResponseBuilder;
import cc.idiary.nuclear.controller.BaseController;
import cc.idiary.nuclear.model.Json;
import cc.idiary.nuclear.model.business.tax.BusAttachmentModel;
import cc.idiary.nuclear.model.business.tax.BusinessModel;
import cc.idiary.nuclear.query.business.tax.BusinessQuery;
import cc.idiary.nuclear.service.ServiceException;
import cc.idiary.nuclear.service.business.tax.BusinessAttachmentService;
import cc.idiary.nuclear.service.business.tax.BusinessService;
import cc.idiary.utils.common.StringTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by Neo on 2017/5/10.
 */

@Controller("businessController")
@RequestMapping("tax/business")
public class BusinessController extends BaseController {

    @Autowired
    private BusinessService businessService;
    @Autowired
    private BusinessAttachmentService businessAttachmentService;

    @RequestMapping("paging/created")
    @ResponseBody
    public Json pagingCreated(BusinessQuery query) {
        try {
            return success(businessService.pagingCreated(query));
        } catch (ServiceException e) {
            return fail(e);
        }
    }

    @RequestMapping("paging/all")
    @ResponseBody
    public Json pagingAll(BusinessQuery query) {
        try {
            return success(businessService.pagingBaseUser(query));
        } catch (ServiceException e) {
            return fail(e);
        }
    }

    @RequestMapping("paging/amendment")
    @ResponseBody
    public Json pagingAmendment(BusinessQuery query) {
        try {
            return success(businessService.pagingAmendment(query));
        } catch (ServiceException e) {
            return fail(e);
        }
    }

    @RequestMapping("paging/error")
    @ResponseBody
    public Json pagingError(BusinessQuery query) {
//        query.setStatus(BUS_STATUS.HAS_ISSUE);
//        if (query.getAmendmentIssue() != null && query.getAmendmentIssue()) { //当查询的是 整改后的业务时，这里自动添加 hasIssue = true 条件 以限定为【有错误的完成状态业务】
//            query.setStatus(BUS_STATUS.FINISH);
//            query.setHasIssue(true);
//        } else {
//            query.setIncludeStatus(new Integer[]{BUS_STATUS.HAS_ISSUE, BUS_STATUS.FINISH});  // 修改为包含已经整改的业务，这里添加状态 5 但是由于query中同时指定了 第一次、第二次或第三次检查是必须包含错误，这样就避免了把没有错误的 处于完成状态的业务也统计进来
//        }
        try {
            if (query.getHasIssue() == null) {  // 查询已经整改的情况
                query.setIncAmendmentCode(new Integer[]{7, 6, 5, 4, 3, 2, 1});  //只要整改状态码不为 0 既是进行过整改操作
                return success(businessService.paging(query));
            } else {
                return success(businessService.pagingError(query));
            }
        } catch (ServiceException e) {
            return fail(e);
        }
    }

    @RequestMapping("add")
    @ResponseBody
    public Json add(BusinessModel business) {
        try {
            businessService.add(business);
        } catch (ServiceException e) {
            return fail(e);
        }
        return success("保存成功");
    }

    @RequestMapping("edit")
    @ResponseBody
    public Json edit(BusinessModel business) {
        try {
            businessService.edit(business);
        } catch (ServiceException e) {
            return fail(e);
        }
        return success("编辑成功");
    }

    @RequestMapping("attachment/upload")
    @ResponseBody
    public Map<String, Object> attachmentUpload(@RequestParam(value = "attachment", required = false) MultipartFile file,
                                                @RequestParam("busId") String busId, HttpServletRequest request) {
        FileInputResponseBuilder builder = new FileInputResponseBuilder();
        if (file == null) {
            return builder.build();
        }
        String newAttId = null;
        try {
            newAttId = businessAttachmentService.add(busId, path -> request.getServletContext().getRealPath(path), file.getOriginalFilename(), file.getInputStream());
        } catch (ServiceException | IOException e) {
            builder.setError(e.getMessage());
            return builder.build();
        }
        try {
            BusAttachmentModel attachmentModel =businessAttachmentService.getById(newAttId);
            builder.addPreviewItem(attachmentModel);
        } catch (ServiceException e) {
            builder.setError(e.getMessage());
            return builder.build();
        }


        return builder.build();
    }

    @RequestMapping("attachment/list")
    @ResponseBody
    public Json attachmentList(@RequestParam("busId") String busId) {
        FileInputResponseBuilder builder = new FileInputResponseBuilder();
        try {
            List<BusAttachmentModel> attachmentModels = businessService.listAttachment(busId);
            attachmentModels.forEach(builder::addPreviewItem);
            return success(builder.build());
        } catch (ServiceException e) {
            return fail(e);
        }
    }

    @RequestMapping("attachment/del")
    @ResponseBody
    public Map<String, Object> attachmentDel(@RequestParam(value = "id[]", required = false) String[] ids,
                                             @RequestParam(value = "key", required = false) String id, HttpServletRequest request) {
        FileInputResponseBuilder builder = new FileInputResponseBuilder();
        try {
            if (!StringTools.isEmpty(ids)) {
                businessAttachmentService.del(ids);
            } else if (!StringTools.isEmpty(id)) {
                businessAttachmentService.del(id, path -> request.getServletContext().getRealPath(path));
            } else {
                builder.setError("没有指定任何删除内容");
                return builder.build();
            }
        } catch (ServiceException e) {
            builder.setError(e.getMessage());
            return builder.build();
        }
        return builder.build();
    }

    @RequestMapping("del")
    @ResponseBody
    public Json del(@RequestParam("ids[]") String[] ids, HttpServletRequest request) {
        Integer result = null;
        try {
            result = businessService.del(ids, path -> request.getServletContext().getRealPath(path));
        } catch (ServiceException e) {
            return fail(e.getMessage());
        }
        return success("成功删除" + result + "条数据");
    }

    @RequestMapping("commit")
    @ResponseBody
    public Json commit(@RequestParam("ids[]") String[] ids) {
        try {
            businessService.commit(ids);
            return success("提交成功");
        } catch (ServiceException e) {
            return fail(e);
        }
    }

    @RequestMapping("commit/amendment")
    @ResponseBody
    public Json commitAmendment(@RequestParam("ids[]") String[] ids) {
        try {
            businessService.commitAmendment(ids);
            return success("提交成功");
        } catch (ServiceException e) {
            return fail(e);
        }
    }


    @RequestMapping("examine/detail")
    @ResponseBody
    public Json examineDetail(String busId, String step) {
        try {
            return success(businessService.examineDetail(busId, step));
        } catch (ServiceException e) {
            return fail(e);
        }
    }

}
