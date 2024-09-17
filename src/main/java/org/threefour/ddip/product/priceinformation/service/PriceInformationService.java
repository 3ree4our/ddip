package org.threefour.ddip.product.priceinformation.service;

import org.threefour.ddip.product.domain.AutoDiscountRequest;
import org.threefour.ddip.product.domain.Product;

public interface PriceInformationService {
    void createPriceInformation(Product product, AutoDiscountRequest autoDiscountRequest);
}
