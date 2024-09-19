package org.threefour.ddip.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.threefour.ddip.member.domain.Refresh;

public interface RefreshRepository extends JpaRepository<Refresh, Long> {
  Boolean existsByRefreshToken(String refresh);

  @Transactional
  void deleteByRefreshToken(String refresh);
}