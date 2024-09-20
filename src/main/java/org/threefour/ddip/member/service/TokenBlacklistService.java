package org.threefour.ddip.member.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class TokenBlacklistService {
  private final ConcurrentHashMap<String, Long> blacklist = new ConcurrentHashMap<>();
  private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

  public TokenBlacklistService() {
    scheduler.scheduleAtFixedRate(this::cleanupExpiredTokens, 0, 1, TimeUnit.HOURS);
  }

  public void blacklistToken(String token) {
    blacklist.put(token, System.currentTimeMillis() + 3600000);
  }

  public boolean isBlacklisted(String token) {
    return blacklist.containsKey(token);
  }

  private void cleanupExpiredTokens() {
    long now = System.currentTimeMillis();
    blacklist.entrySet().removeIf(entry -> entry.getValue() <= now);
  }
}