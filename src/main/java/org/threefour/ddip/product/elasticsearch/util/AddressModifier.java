package org.threefour.ddip.product.elasticsearch.util;

import org.threefour.ddip.product.elasticsearch.domain.ProductDocument;

public class AddressModifier {
    public static void joinLongRoadAddress(String[] dividedAddress, StringBuilder roadAddressBuilder) {
        for (int i = 3; i < dividedAddress.length; i++) {
            roadAddressBuilder.append(" ");
            roadAddressBuilder.append(dividedAddress[i]);
        }
    }

    public static String joinAddress(ProductDocument productDocument) {
        StringBuilder addressBuilder = new StringBuilder(productDocument.getCity());
        addressBuilder.append(" ");
        addressBuilder.append(productDocument.getDistrict());
        addressBuilder.append(" ");
        addressBuilder.append(productDocument.getRoadAddress());

        return addressBuilder.toString();
    }
}
