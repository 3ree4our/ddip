package org.threefour.ddip.image.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class DesignageRepresentativeImageRequest {
    private String id;
    private String targetType;
    private String targetId;
}
