package org.threefour.ddip.image.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TargetType {
    MEMBER("회원"),
    PRODUCT("상품"),
    CHATTING("채팅"),
    ALARM("알림");

    private final String description;
}
