package com.e_r_platform.controller.Authorization;

import com.e_r_platform.controller.UserController;
import com.e_r_platform.model.User;
import com.e_r_platform.service.UserService;
import com.e_r_platform.service.impl.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.authorization.AuthorizationDecision;

import java.util.function.Supplier;

@Component
public class CustomAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    @Autowired
    private JwtHandler jwtHandler;
    @Autowired
    private UserServiceImpl userService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext context) {
        logger.info("", context);

        String id = context.getVariables().get("id");
        User user = userService.getOne(Integer.parseInt(id));
        Authentication auth = authentication.get();
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("admin"));
        boolean userMatch = jwtHandler.checkUser(auth, user.getEmail());
        return new AuthorizationDecision(isAdmin || userMatch);
//        return new AuthorizationDecision(jwtHandler.checkUser(auth, user.getEmail()));
    }
}
