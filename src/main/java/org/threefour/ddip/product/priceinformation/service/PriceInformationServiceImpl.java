package org.threefour.ddip.product.priceinformation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.threefour.ddip.product.domain.AutoDiscountRequest;
import org.threefour.ddip.product.domain.Product;
import org.threefour.ddip.product.priceinformation.domain.PriceInformation;
import org.threefour.ddip.product.priceinformation.repository.PriceInformationRepository;

@Service
@RequiredArgsConstructor
public class PriceInformationServiceImpl implements PriceInformationService {
    private final PriceInformationRepository priceInformationRepository;

    @Override
    public void createPriceInformation(Product product, AutoDiscountRequest autoDiscountRequest) {
        priceInformationRepository.save(PriceInformation.from(product, autoDiscountRequest));
    }
}
