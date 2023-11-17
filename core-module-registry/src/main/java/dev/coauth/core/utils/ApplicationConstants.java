package dev.coauth.core.utils;

import java.util.List;
import java.util.Set;

public class ApplicationConstants {

    public static final String STATUS_PENDING="P";
    public static final String STATUS_SUCCESS="S";
    public static final String STATUS_ACTIVE="A";
    public static final String STATUS_DISABLED="D";

    public static final String MODULE_RECONFIRM="RECONFIRM";
    public static final String MODULE_TOTP="TOTP";

    public static final Set<String> COAUTH_VALID_MODULES= Set.of(MODULE_RECONFIRM, MODULE_TOTP);

    public static final Set<String> AVAILABLE_MODULES= Set.of(MODULE_RECONFIRM, MODULE_TOTP);

}
