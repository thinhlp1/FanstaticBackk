package com.fanstatic.config.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fanstatic.service.system.SystemService;
import com.fanstatic.util.CookieUtils;
import com.fanstatic.util.JwtUtil;
import com.fanstatic.util.ResponseUtils;
import com.fanstatic.util.SessionUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final SessionUtils sessionUtils;
    private final CookieUtils cookieUtils;
    private final SystemService systemService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        /*
         * jwtFiltetValided : kiem tra xem cac request da co token hop le hay chua
         * dung cho cac filter phia sau kiem tra, neu can
         */

        /*
         * username == number Phone trong co so du lieu va model
         */

        String token = cookieUtils.getValue("token");
        String jwt;
        String username;

        // check token exits
        if (token == null || token.equals("")) {
            // request.getSession().setAttribute("jwtFiltetValided", false);
            sessionUtils.set("jwtFiltetValided", false);
            filterChain.doFilter(request, response);
            return;
        }

        jwt = token;

        try {
            username = jwtUtil.extractUsername(jwt);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // check user has login
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails;
            try {
                userDetails = this.userDetailsService.loadUserByUsername(username);
            } catch (Exception e) {
                ResponseUtils.setResponseDTOToHttpResponse(response,
                        ResponseUtils.fail(498, "User không tồn tại", null));
                return;
            }

            try {

                if (systemService.checkUserLogout(token)) {
                    System.out.println(token);
                    System.out.println("HRERE");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }

                if (jwtUtil.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    // request.getSession().setAttribute("jwtFiltetValided", true);
                    sessionUtils.set("jwtFiltetValided", true);

                }
            } catch (Exception e) {
                // ResponseUtils.setResponseDTOToHttpResponse(response,
                // ResponseUtils.fail(498, "Token không hơp lệ", null));
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }
        filterChain.doFilter(request, response);

    }

}