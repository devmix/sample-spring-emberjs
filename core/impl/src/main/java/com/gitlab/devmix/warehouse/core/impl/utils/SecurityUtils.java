package com.gitlab.devmix.warehouse.core.impl.utils;

import com.gitlab.devmix.warehouse.core.api.security.SysUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * @author Sergey Grachev
 */
public final class SecurityUtils {

    private SecurityUtils() {
    }

    @Nullable
    public static UUID userID() {
        final Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof SysUserDetails) {
            return ((SysUserDetails) principal).getId();
        }
        return null;
    }
}
