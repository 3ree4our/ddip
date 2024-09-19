package org.threefour.ddip.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class PageableGenerator {
    public static Pageable createPageable(String paged, String pageNumber, String size, String sort) {
        return FormatConverter.parseToBoolean(paged)
                ? PageRequest.of(
                FormatConverter.parseToInt(pageNumber),
                FormatConverter.parseToInt(size),
                FormatConverter.parseSortString(sort)
        )
                : Pageable.unpaged();
    }
}
