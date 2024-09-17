package org.threefour.ddip.product.priceinformation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.threefour.ddip.product.priceinformation.domain.PriceInformation;

public interface PriceInformationRepository extends JpaRepository<PriceInformation, Long> {
}
