package org.threefour.ddip.image.domain;

import lombok.NoArgsConstructor;
import org.threefour.ddip.audit.BaseGeneralEntity;

import javax.persistence.Column;
import javax.persistence.Entity;

import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
public class Image extends BaseGeneralEntity {
    @Column(nullable = false, length = 20)
    private TargetType targetType;

    @Column(nullable = false)
    private Long targetId;

    @Column(name = "s3_url", nullable = false, length = 500)
    private String s3Url;

    private Image(TargetType targetType, Long targetId, String s3Url) {
        this.targetType = targetType;
        this.targetId = targetId;
        this.s3Url = s3Url;
    }

    public static Image of(TargetType targetType, Long targetId, String s3Url) {
        return new Image(targetType, targetId, s3Url);
    }

    public String getS3Url() {
        return s3Url;
    }

    public void delete() {
        deleteEntity();
    }
}
