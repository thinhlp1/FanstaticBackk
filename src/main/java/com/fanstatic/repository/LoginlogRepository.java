package com.fanstatic.repository;

import com.fanstatic.model.Loginlog;
import com.fanstatic.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface LoginlogRepository extends JpaRepository<Loginlog, Integer> {
    Optional<Loginlog> findByToken(String token);

    @Query("SELECT ll FROM Loginlog ll " +
            "WHERE ll.user IN :userList " +
            "AND ll.user != user " +
            "AND ll.logoutAt IS NULL " +
            "AND ll.actionAt >= :twentyFourHoursAgo")
    List<Loginlog> findLoginLogsWithin24HoursForUsers(@Param("userList") List<User> userList, @Param("user") User user,
            @Param("twentyFourHoursAgo") Date twentyFourHoursAgo);
}
