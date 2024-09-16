package org.threefour.ddip.image.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class AddImagesRequest {
    private String targetType;
    private Long targetId;
}
