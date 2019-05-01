package sg.gov.csit.opvamspv.interceptors;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import sg.gov.csit.opvamspv.accesscontrollist.Acl;
import sg.gov.csit.opvamspv.accesscontrollist.Role;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {


        List<Acl> acls = new ArrayList() {{
           add(new Acl(1, Role.Admin, "IT"));
           add(new Acl(2, Role.AO, "HR"));
            add(new Acl(3, Role.AO, "Finance"));
        }};

        // Hardcoded to Tan Miam Miam
        request.setAttribute("pfNo", "11011");
        request.setAttribute("acls", acls);
        return super.preHandle(request, response, handler);
    }
}
