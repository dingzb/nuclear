package cc.idiary.nuclear.aspect;

import cc.idiary.nuclear.config.AttrName;
import cc.idiary.nuclear.model.system.UserModel;
import cc.idiary.nuclear.model.BaseModel;
import cc.idiary.nuclear.query.BaseQuery;
import org.aspectj.lang.JoinPoint;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.WebUtils;

/**
 * Created by Dzb on 2017/3/6.<br/>
 * 为 BaseQuery BaseModel 对象添加当前用户id
 */
public class UserAspect {

    public void setUserToQuery(JoinPoint jp) {
        Object[] args = jp.getArgs();
        BaseQuery baseQuery = null;
        BaseModel baseModel = null;

        for (Object arg : args) {
            if (arg instanceof BaseQuery) {
                baseQuery = (BaseQuery) arg;
            } else if (arg instanceof BaseModel) {
                baseModel = (BaseModel) arg;
            }
        }

        if (baseQuery != null || baseModel != null) {
            UserModel user = (UserModel) WebUtils.getSessionAttribute(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest(), AttrName.SESSION_USER);
            if (user != null){
                if (baseQuery != null) {
                    baseQuery.setUserId(user.getId());
                }
                if (baseModel != null) {
                    baseModel.setUserId(user.getId());
                }
            }
        }

    }
}
