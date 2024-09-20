package org.threefour.ddip.util;

import javax.servlet.http.HttpSession;

public class AuthenticationValidator {
    public static boolean isAuthorized(String memberId, HttpSession httpSession) {
        return memberId != null && memberId.equals(httpSession.getAttribute("memberId").toString());
    }
}
