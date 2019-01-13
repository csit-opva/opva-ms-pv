package sg.gov.csit.opvamspv.interceptors;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Hardcoded to Tan Miam Miam
        request.setAttribute("pfNo", "11011");
        return super.preHandle(request, response, handler);
    }
}
