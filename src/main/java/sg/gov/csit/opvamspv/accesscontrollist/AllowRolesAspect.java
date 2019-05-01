package sg.gov.csit.opvamspv.accesscontrollist;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import sg.gov.csit.opvamspv.officer.OfficerController;

import java.lang.reflect.Method;
import java.util.List;

@Aspect
@Component
public class AllowRolesAspect {

    private static final Logger log = LoggerFactory.getLogger(AllowRolesAspect.class);

    @Around(value = "@annotation(anno)", argNames = "jp, anno")
    public Object handle(ProceedingJoinPoint joinPoint, AllowRoles allowRoles) throws Throwable {
//        Object obj = joinPoint.getThis();
//
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();

        List<Acl> acls  = (List <Acl>) RequestContextHolder.currentRequestAttributes().getAttribute("acls", RequestAttributes.SCOPE_REQUEST);

//        @RequestAttribute("acls") List<Acl> acls;

        acls.stream().forEach(acl -> log.info("Acls of officer: " + acl.getRole().value()));

        for(Role role : allowRoles.roles()) {
            System.out.println("ASPECT: " + role.value());

        }

        return joinPoint.proceed();
    }
}
