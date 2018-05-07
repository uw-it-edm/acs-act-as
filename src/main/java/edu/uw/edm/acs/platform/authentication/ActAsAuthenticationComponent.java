package edu.uw.edm.acs.platform.authentication;


import net.sf.acegisecurity.Authentication;

import org.alfresco.repo.security.authentication.AuthenticationComponentImpl;
import org.alfresco.repo.security.authentication.AuthenticationException;
import org.alfresco.service.cmr.security.AuthorityService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;


/**
 * @author Maxime Deravet
 * Date: 4/24/18
 */
public class ActAsAuthenticationComponent extends AuthenticationComponentImpl {

    private static final Log LOG = LogFactory.getLog(ActAsAuthenticationComponent.class);

    /**
     * Some custom properties
     */
    private String actAsHeader;
    private String apiUserGroup;

    private AuthorityService authorityService;


    public void authenticateImpl(String userName, char[] password) throws AuthenticationException {

        //This authentication relies on the default Alfresco authentication
        super.authenticateImpl(userName, password);


        Authentication currentAuthentication = getCurrentAuthentication();
        if (currentAuthentication != null && currentAuthentication.isAuthenticated()) {
            if (isApiUser(getCurrentUserName())) {


                String actAsHeader = getActAsHeaderValueFromRequest();

                if (StringUtils.isNotBlank(actAsHeader)) {

                    if (LOG.isDebugEnabled()) {
                        LOG.debug("API user " + userName + " trying to log as " + actAsHeader);
                    }
                    try {
                        //TODO should we check if the api user is trying to elevate privileges ( ie: log in as admin )

                        setCurrentUser(actAsHeader, UserNameValidationMode.CHECK);
                    } catch (Exception e) {
                        LOG.info("Unknown User " + actAsHeader);
                        clearCurrentSecurityContext();
                        throw new AuthenticationException(e.getMessage(), e);
                    }
                } else {
                    //NOOP, api User is acting as itself
                }

            }

        }
    }

    private boolean isApiUser(String userName) {
        Set<String> authoritiesForUser = authorityService.getAuthoritiesForUser(userName);

        return authoritiesForUser.contains(apiUserGroup);
    }


    private String getActAsHeaderValueFromRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();

        return request.getHeader(actAsHeader);
    }


    public void setActAsHeader(String actAsHeader) {
        this.actAsHeader = actAsHeader;
    }

    public void setApiUserGroup(String apiUserGroup) {
        this.apiUserGroup = apiUserGroup;
    }

    public void setAuthorityService(AuthorityService authorityService) {
        this.authorityService = authorityService;
    }
}
