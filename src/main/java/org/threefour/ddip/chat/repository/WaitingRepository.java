package org.threefour.ddip.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.threefour.ddip.chat.domain.Waiting;
import org.threefour.ddip.chat.domain.WaitingStatus;
import org.threefour.ddip.member.domain.Member;
import org.threefour.ddip.product.domain.Product;

import java.util.List;
import java.util.Optional;

@Repository
public interface WaitingRepository extends JpaRepository<Waiting, Long> {

  // 특정 상품에 대한 대기 목록 조회
  List<Waiting> findByProductOrderByCreatedAtAsc(Product product);

  // 특정 상품과 사용자에 대한 대기 정보 조회
  Optional<Waiting> findByProductAndSender(Product product, Member sender);

  // 특정 상품의 진행 중인 채팅 조회
  Optional<Waiting> findByProductAndStatus(Product product, WaitingStatus status);

  // 특정 사용자의 대기 목록 조회
  List<Waiting> findBySenderOrderByCreatedAtDesc(Member sender);

  // 특정 상태의 대기 목록 조회
  List<Waiting> findByStatus(WaitingStatus status);

  // 특정 상품의 대기 수 조회
  Long countByProductAndStatus(Product product, WaitingStatus status);


}
