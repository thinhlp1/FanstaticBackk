package com.fanstatic.repository;

import com.fanstatic.model.BrowserToken;
import com.fanstatic.model.User;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BrowserTokenRepository extends JpaRepository<BrowserToken, Integer> {

    public Optional<BrowserToken> findByUserAndToken(User user, String token);

    public List<BrowserToken> findByUser(User user);

}
