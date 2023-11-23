package com.fanstatic.config.security;

import com.fanstatic.model.Account;
import com.fanstatic.service.model.RolePermissionService;
import com.fanstatic.util.SessionUtils;
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
import java.util.Arrays;

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

        String[] allowedPaths = { "home", "auth", "u", "handle-checkout", "cancel-checkout", "notification" };

        for (String path : urlPaths) {
            // Sử dụng phương thức Arrays.asList để chuyển mảng thành danh sách và sử dụng
            // contains để kiểm tra
            if (path.equals("handle-checkout") || path.equals("cancel-checkout")) {

                filterChain.doFilter(request, response);
                return;
            }
        }

        for (String path : urlPaths) {
            // Sử dụng phương thức Arrays.asList để chuyển mảng thành danh sách và sử dụng
            // contains để kiểm tra
            if (Arrays.asList(allowedPaths).contains(path)) {
                filterChain.doFilter(request, response);
                return;
            }
        }

        // khong tim thay token o jwt filter
        if (!jwtFilterValided || jwtFilterValided == null) {

            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;

        } else {
            // lay thong tin user neu dang co dang nhap
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                Account account = (Account) authentication.getPrincipal();

                // api for user, dont need authrization

                // kiem tra url api xem co hop le hay khong
                if (urlPaths[1].equals("api") || urlPaths[1].equals("ws")) {
                    String managerFeatureId = (urlPaths[2] + "_" + urlPaths[3]).toUpperCase();
                    String permissionId = urlPaths[4].toUpperCase();
                    int roleId = account.getRole().getId();

                    boolean isAuthentization = rolePermissionService.checkUserRolePermission(roleId, managerFeatureId,
                            permissionId);
                    if (isAuthentization) {
                        filterChain.doFilter(request, response);
                        return;
                    } else {

                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                        return;
                    }
                } else {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }

                // Ví dụ: In ra tên người dùng
            }
        }

        filterChain.doFilter(request, response);
        return;
    }

}
