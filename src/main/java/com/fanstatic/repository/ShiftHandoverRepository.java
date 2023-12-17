package com.fanstatic.repository;

import com.fanstatic.model.ShiftHandover;
import com.fanstatic.model.User;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ShiftHandoverRepository extends JpaRepository<ShiftHandover, Integer> {
        @Query("SELECT COUNT(sh) > 0 FROM ShiftHandover sh " +
                        "WHERE sh.startShiftTime IS NOT NULL " +
                        "AND sh.startShiftTime >= :twentyFourHoursAgo " +
                        "ORDER BY sh.createAt DESC " +
                        "LIMIT 1")
        boolean hasUserStartedShiftWithin24Hours(@Param("twentyFourHoursAgo") Date twentyFourHoursAgo);

        @Query("SELECT sh FROM ShiftHandover sh " +
                        "WHERE sh.startShiftTime >= :twentyFourHoursAgo " +
                        "AND sh.createBy = :user " +
                        "ORDER BY sh.createAt DESC " +
                        "LIMIT 1")
        Optional<ShiftHandover> findLatestShiftHandover(@Param("user") User user,
                        @Param("twentyFourHoursAgo") Date twentyFourHoursAgo);

        // @Query("SELECT COUNT(sh) > 0 FROM ShiftHandover sh " +
        // "WHERE sh.createBy = :user " +
        // "AND sh.startShiftTime IS NOT NULL " +
        // "AND sh.endShiftTime IS NULL ")
        // boolean checkUserHasStartShift(@Param("user") User user,
        // @Param("twentyFourHoursAgo") Date twentyFourHoursAgo);

}
