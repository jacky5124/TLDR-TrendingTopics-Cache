package com.jackist.tldr.trendingtopics.cache.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jackist.tldr.trendingtopics.cache.pojo.azure.ClientPrincipal;
import com.jackist.tldr.trendingtopics.cache.pojo.azure.ClientPrincipalClaim;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.Base64;

public class CacheRequestHeaderAuthenticationFilter extends RequestHeaderAuthenticationFilter {
    private final String cloudRole;

    public CacheRequestHeaderAuthenticationFilter(
            String requestHeader,
            String cloudRole,
            AuthenticationManager authenticationManager
    ) {
        Assert.hasText(cloudRole, "cloudRole cannot be null or empty");
        this.cloudRole = cloudRole;
        this.setPrincipalRequestHeader(requestHeader);
        this.setExceptionIfHeaderMissing(false);
        this.setAuthenticationManager(authenticationManager);
        this.afterPropertiesSet();
    }

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        String principal = (String) super.getPreAuthenticatedPrincipal(request);
        if (principal == null) {
            return null;
        }
        byte[] decodedPrincipal = Base64.getDecoder().decode(principal);
        ObjectMapper mapper = new ObjectMapper();
        ClientPrincipal clientPrincipal;
        try {
            clientPrincipal = mapper.readValue(decodedPrincipal, ClientPrincipal.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (ClientPrincipalClaim claim : clientPrincipal.claims()) {
            if (claim.typ().equals("roles") && claim.val().equals(cloudRole)) {
                return claim.val();
            }
        }
        return null;
    }
}
