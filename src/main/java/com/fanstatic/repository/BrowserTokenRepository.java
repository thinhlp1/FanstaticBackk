package com.fanstatic.repository;

import com.fanstatic.model.BrowserToken;
import com.fanstatic.model.User;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BrowserTokenRepository extends JpaRepository<BrowserToken, Integer> {

    public Optional<BrowserToken> findByUserIdAndToken(Integer userId, String token);

    // public List<BrowserToken> findByUser(User user);

    // public Optional<List<BrowserToken>> findByUserId(int userId);

    @Query("SELECT bt FROM BrowserToken bt WHERE bt.userId = :userId")
    public Optional<List<BrowserToken>> findByUserId(@Param("userId") int userId);
}
