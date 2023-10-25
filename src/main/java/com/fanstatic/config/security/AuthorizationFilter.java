package com.fanstatic.config.security;

import java.io.IOException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.fanstatic.model.Account;
import com.fanstatic.service.model.RolePermissionService;
import com.fanstatic.util.SessionUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthorizationFilter extends OncePerRequestFilter {
    private final RolePermissionService rolePermissionService;
    private final SessionUtils sessionUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        Boolean jwtFilterValided = (Boolean) sessionUtils.get("jwtFiltetValided");
        String currentUrl = request.getRequestURI();
        String[] urlPaths = currentUrl.split("/");

        // khong tim thay token o jwt filter
        if (!jwtFilterValided || jwtFilterValided == null) {
            // kiem tra xem api co can check quyen khong
            if (currentUrl.contains("home") || currentUrl.contains("auth")) {
                filterChain.doFilter(request, response);
                return;

            } else {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
        } else {
            // lay thong tin user neu dang co dang nhap
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                Account account = (Account) authentication.getPrincipal();


                // kiem tra url api xem co hop le hay khong
                if (urlPaths[1].equals("api")) {
                    String managerFeatureId = (urlPaths[2] + "_" + urlPaths[3]).toUpperCase();
                    String permissionId = urlPaths[4].toUpperCase();
                    int roleId = account.getRole().getId();
                    boolean isAuthentization = rolePermissionService.checkUserRolePermission(roleId, managerFeatureId,
                            permissionId);
                       System.out.println(isAuthentization);
                    if (isAuthentization) {
                        filterChain.doFilter(request, response);
                        return;
                    } else {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                        return;
                    }
                }

                // Ví dụ: In ra tên người dùng
            }
        }

        filterChain.doFilter(request, response);
        return;
    }

}
