package cc.idiary.nuclear.interceptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cc.idiary.nuclear.config.AttrName;
import cc.idiary.nuclear.config.UserType;
import cc.idiary.nuclear.config.HttpMethods;
import cc.idiary.nuclear.model.system.UserModel;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

public class SecurityInterceptor implements HandlerInterceptor {

    private List<Pattern> noSessionUrlPatterns = new ArrayList<Pattern>();
    private List<Pattern> noPermissionUrlPatterns = new ArrayList<Pattern>();

    public void setNoSessionUrls(List<String> urls) {
        for (String url : urls) {
            noSessionUrlPatterns.add(Pattern.compile(url));
        }
    }

    public void setNoPermissionUrls(List<String> urls) {
        for (String url : urls) {
            noPermissionUrlPatterns.add(Pattern.compile(url));
        }
    }

    /**
     * 无需登录列表正则验证
     *
     * @param url
     * @return
     */
    private boolean noSessionMatch(String url) {
        for (Pattern pattern : noSessionUrlPatterns) {
            if (pattern.matcher(url).find())
                return true;
        }
        return false;
    }

    /**
     * 无需权限列表正则验证
     *
     * @param url
     * @return
     */
    private boolean noPermissionMatch(String url) {
        for (Pattern pattern : noPermissionUrlPatterns) {
            if (pattern.matcher(url).find())
                return true;
        }
        return false;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        String requestUri = request.getRequestURI();
        String contextPath = request.getContextPath();
        String url = requestUri.substring(contextPath.length() + 1);
        String method = request.getMethod();

        if (noSessionMatch(url)) {
            return true;
        }
        UserModel user = (UserModel) WebUtils.getSessionAttribute(request,
                AttrName.SESSION_USER);

        if (user == null) { // 未登录
            response.sendError(401);
            return false;
        }

        // 对内置用户放行
        if (UserType.BUILD_IN.toString().equals(user.getType())) {
            return true;
        }

        @SuppressWarnings("unchecked")
        Map<String, String> actions = (Map<String, String>) WebUtils.getSessionAttribute(request, AttrName.SESSION_ACTIONS);

        if (!noPermissionMatch(url) && (actions.get(url) == null || !HttpMethods.match(method, actions.get(url)))) {
            response.sendError(403); // 没有权限
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
    }

}
