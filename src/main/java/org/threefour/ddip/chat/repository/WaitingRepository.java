package org.threefour.ddip.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.threefour.ddip.chat.domain.Waiting;

import java.util.Optional;

@Repository
public interface WaitingRepository extends JpaRepository<Waiting, Long> {

  Optional<Waiting> findByProductId(Long productId);

}
