package com.fanstatic.service.system;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.fanstatic.model.Account;
import com.fanstatic.model.Loginlog;
import com.fanstatic.model.ManagerFeature;
import com.fanstatic.model.Permission;
import com.fanstatic.model.Systemlog;
import com.fanstatic.model.User;
import com.fanstatic.repository.LoginlogRepository;
import com.fanstatic.repository.ManagerFeatureRepository;
import com.fanstatic.repository.PermissionRepository;
import com.fanstatic.repository.SystemlogRepository;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SystemService {

    @Autowired
    private HttpServletRequest request;
    private final SystemlogRepository systemlogRepository;
    private final ManagerFeatureRepository managerFeatureRepository;
    private final PermissionRepository permissionRepository;
    private final LoginlogRepository loginlogRepository;

    public void writeSystemLog(Object objectId, String objectName, String description) {

        String objId = String.valueOf(objectId);

        String currentUrl = request.getRequestURI();
        String[] urlPaths = currentUrl.split("/");

        User user = getUserLogin();

        String managerFeatureId = (urlPaths[2] + "_" + urlPaths[3]).toUpperCase();
        String permissionId = urlPaths[4].toUpperCase();

        ManagerFeature managerFeature = managerFeatureRepository.findById(managerFeatureId).orElse(null);
        Permission permission = permissionRepository.findById(permissionId).orElse(null);

        Systemlog systemlog = new Systemlog();
        systemlog.setAction(permission);
        systemlog.setObjectId(objId);
        systemlog.setObjectName(objectName);
        systemlog.setType(managerFeature);
        systemlog.setUser(user);
        systemlog.setActionAt(new Date());

        systemlogRepository.saveAndFlush(systemlog);

    }

    public void writeLoginLog(String token, User userLogin) {
        Loginlog loginlog = new Loginlog();
        loginlog.setAction(Loginlog.LOGIN);
        loginlog.setActionAt(new Date());
        loginlog.setToken(token);
        loginlog.setUser(userLogin);
        loginlogRepository.save(loginlog);
    }

    public User getUserLogin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Account account = (Account) authentication.getPrincipal();
            User user = account.getUser();
            return user;
        }
        return null;
    }
}
