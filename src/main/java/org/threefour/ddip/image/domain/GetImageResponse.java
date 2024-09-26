package org.threefour.ddip.image.domain;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class GetImageResponse implements Serializable {
    private static final long serialVersionUID = 5L;

    private Long id;
    private String url;

    private GetImageResponse(Long id, String url) {
        this.id = id;
        this.url = url;
    }

    public static GetImageResponse from(Image image) {
        return new GetImageResponse(image.getId(), image.getS3Url());
    }
}
