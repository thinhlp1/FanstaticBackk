package com.fanstatic.config.security;

import com.fanstatic.config.constants.ApplicationConst;
import com.fanstatic.config.system.IpConfig;
import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.ResponseListDataDTO;
import com.fanstatic.model.Account;
import com.fanstatic.service.model.RolePermissionService;
import com.fanstatic.service.system.SystemConfigService;
import com.fanstatic.service.system.SystemService;
import com.fanstatic.util.SessionUtils;
import com.twilio.rest.api.v2010.account.Application;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class IpAddressFilter extends OncePerRequestFilter {
    private final RolePermissionService rolePermissionService;
    private final SessionUtils sessionUtils;
    private final SystemConfigService systemConfigService;

    private List<IpConfig> ipConfigs;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (ipConfigs == null) {
            ipConfigs = systemConfigService.getListIpConfig();
        }

        // String remoteAddr = request.getRemoteAddr();
        // System.out.println("REMOTE ADDR: " + remoteAddr);
        // if (remoteAddr != null) {
        //     Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //     if (authentication != null && authentication.isAuthenticated()) {
        //         Account account = (Account) authentication.getPrincipal();
        //         Integer roleId = account.getRole().getId();
        //         // CUSTOMER DONT NEED CHECK IP
        //         if (roleId == (ApplicationConst.CUSTOMER_ROLE_ID)) {
        //             filterChain.doFilter(request, response);
        //             return;
        //             //MANAGE AND ADMIN DONT NEED CHECK IP
        //         } else if (roleId == ApplicationConst.ADNIN_ROLE_ID || roleId == ApplicationConst.MANAGER_ROLE_ID) {
        //             filterChain.doFilter(request, response);
        //             return;
        //         } else {
        //             for (IpConfig ipConfig : ipConfigs) {
        //                 System.out.println("IPCONFIG: " + ipConfig.getIpAddress());
        //                 System.out.println("REMOTE ADDR: " + remoteAddr);
        //                 if (ipConfig.getIpAddress().equals(remoteAddr) || remoteAddr.equals("0:0:0:0:0:0:0:1")) {
        //                     filterChain.doFilter(request, response);    
        //                     return;
        //                 }
        //             }

        //             response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        //             response.sendError(HttpServletResponse.SC_FORBIDDEN);
        //             return;
        //         }
        //     }

        // }

        filterChain.doFilter(request, response);
        return;
    }

}
