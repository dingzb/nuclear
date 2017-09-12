package cc.idiary.nuclear.aspect;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import cc.idiary.nuclear.config.AttrName;
import cc.idiary.nuclear.entity.system.BusinessLogEntity;
import cc.idiary.nuclear.logger.BusinessLog;
import cc.idiary.nuclear.logger.LogOperationType;
import cc.idiary.nuclear.model.system.UserModel;
import cc.idiary.nuclear.service.system.BusinessLogService;
import cc.idiary.nuclear.utils.SoftKey;
import cc.idiary.nuclear.service.ServiceException;
import org.aspectj.lang.JoinPoint;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.WebUtils;

/**
 * 记录日志
 *
 */
public class LogAspect {

    private BusinessLogService businessLogService;

    public LogAspect(BusinessLogService businessLogService) {
        this.businessLogService = businessLogService;
    }

    public void doAfterReturning(JoinPoint jp, BusinessLog businessLog, Object obj) {
        addBusLog(getBusLog(jp, businessLog));
    }

    public void doAfterThrowing(JoinPoint jp, BusinessLog businessLog, Exception e) {
        addBusLog(getBusLog(jp, businessLog, e));
    }

    /**
     * 获取基本日志信息
     *
     * @param jp
     * @param businessLog
     * @return
     */
    private BusinessLogEntity getBusLog(JoinPoint jp, BusinessLog businessLog) {
        return getBusLog(jp, businessLog, null);
    }

    /**
     * 获取基本日志信息
     *
     * @param jp
     * @param businessLog
     * @param e
     * @return
     */
    private BusinessLogEntity getBusLog(JoinPoint jp,
                                        BusinessLog businessLog, Exception e) {
        SoftKey softKey = new SoftKey();
        String key = "66F63D5A57058CD8D11F8B5EF5AC524E";
        BusinessLogEntity busLog = new BusinessLogEntity();
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attr.getRequest();
        busLog.setOperation(businessLog.operation());

        if (businessLog.operation().equals(LogOperationType.LOGIN)) {
            busLog.setUsername(softKey.StrEnc((String) jp.getArgs()[0], key));
        } else {
            UserModel userModel = (UserModel) WebUtils.getSessionAttribute(
                    request, AttrName.SESSION_USER);
            if (userModel != null) {
                busLog.setUsername(softKey.StrEnc(userModel.getUsername(), key));
            }
        }
        busLog.setIpAddr(softKey.StrEnc(request.getRemoteAddr(), key));
        busLog.setUserAgent(softKey.StrEnc(request.getHeader("User-Agent"), key));
        busLog.setCreateTime(new Date());
        busLog.setContent(softKey.StrEnc(businessLog.content(), key));
        busLog.setSignature(softKey.StrEnc(jp.getSignature().toString(), key));
        StringBuilder argsSb = new StringBuilder("[");
        Object[] args = jp.getArgs();
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            if (arg != null) {
                argsSb.append("arg" + i + "=");
                argsSb.append(args[i].getClass().getSimpleName() + "(");
                argsSb.append(args[i].toString() + "), ");
            }
        }
        busLog.setArgs(softKey.StrEnc(argsSb.substring(0, argsSb.length() - 2) + "]", key));
        if (e != null) {
            busLog.setIsException(true);
            if (e.getMessage() != null) {
                busLog.setException(softKey.StrEnc(e.getMessage(), key));
            }
        } else {
            busLog.setIsException(false);
        }
        busLog.setDelFlag(false);
        return busLog;
    }

    /**
     * 添加日志记录
     *
     * @param busLog
     */
    private void addBusLog(BusinessLogEntity busLog) {
        try {
            businessLogService.add(busLog);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }
}
