package org.threefour.ddip.deal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.threefour.ddip.deal.domain.Deal;
import org.threefour.ddip.member.domain.Member;
import org.threefour.ddip.product.domain.Product;

import java.util.Optional;

public interface DealRepository extends JpaRepository<Deal, Long> {
    int countByProductAndSellerAndDeleteYnFalse(Product product, Member buyer);

    Optional<Deal> findByProductIdAndBuyerIdAndDeleteYnFalse(Long productId, Long memberId);

    int countByProductIdAndDeleteYnFalse(Long productId);
}
