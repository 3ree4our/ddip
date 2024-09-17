package org.threefour.ddip.product.priceinformation.domain;

import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class GetPriceInformationListResponse {
    private List<GetPriceInformationResponse> getPriceInformationResponses;

    private GetPriceInformationListResponse(List<GetPriceInformationResponse> getPriceInformationResponses) {
        this.getPriceInformationResponses = getPriceInformationResponses;
    }

    public static GetPriceInformationListResponse from(List<PriceInformation> priceInformation) {
        List<GetPriceInformationResponse> getPriceInformationResponses = priceInformation.stream()
                .map(GetPriceInformationResponse::from)
                .collect(Collectors.toList());

        return new GetPriceInformationListResponse(getPriceInformationResponses);
    }
}
