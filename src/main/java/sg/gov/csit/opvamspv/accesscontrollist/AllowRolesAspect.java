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
import sg.gov.csit.opvamspv.exception.UnauthorizeException;
import sg.gov.csit.opvamspv.officer.OfficerController;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Aspect
@Component
public class AllowRolesAspect {

    private static final Logger log = LoggerFactory.getLogger(AllowRolesAspect.class);

    @Around(value = "@annotation(anno)", argNames = "jp, anno")
    public Object handle(ProceedingJoinPoint joinPoint, AllowRoles allowRoles) throws Throwable {
//        Object obj = joinPoint.getThis();
//
//        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();

        Boolean isAuthorized = false;

        List<Acl> aclList  = (List <Acl>) RequestContextHolder.currentRequestAttributes().getAttribute("acls", RequestAttributes.SCOPE_REQUEST);
        String pfNo = (String) RequestContextHolder.currentRequestAttributes().getAttribute("pfNo", RequestAttributes.SCOPE_REQUEST);
        String txnId = (String) RequestContextHolder.currentRequestAttributes().getAttribute("txnId", RequestAttributes.SCOPE_REQUEST);

        try {
            log.info(txnId + "Authorization check start: for pf: " + pfNo + "performing action: " + allowRoles.action());

            Set <Role> roleSet = aclList.stream().map( acl -> {
                return acl.getRole();
            }).collect(Collectors.toSet());

            aclList.stream().forEach(acl -> log.info("Acl List of officer: " + acl.getRole().value()));
            roleSet.stream().forEach(role -> log.info("Role Set of officer: " + role.value()));

            for(Role role : allowRoles.roles()) {
                System.out.println("ASPECT: " + role.value());
                if( roleSet.contains(role))
                    isAuthorized = true;
            }

            if(isAuthorized) {
                return joinPoint.proceed();
            } else {
                throw new UnauthorizeException(allowRoles.action());
            }
        } finally {
            log.info(txnId + "Authorization check Ended: for pf: " + pfNo + "performing action: " + allowRoles.action());
        }
    }
}
