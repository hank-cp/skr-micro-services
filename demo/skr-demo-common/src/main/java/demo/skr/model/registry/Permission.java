/*
 * Copyright (C) 2019-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package demo.skr.model.registry;

import demo.skr.SimpleJwtPrincipal;
import demo.skr.model.CodeBasedEntity;
import lombok.Getter;
import org.skr.common.exception.ConfException;
import org.skr.common.exception.ErrorInfo;
import org.skr.registry.PermissionRegistry;
import org.skr.registry.RealmRegistry;
import org.skr.security.JwtPrincipal;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

/**
 * @author <a href="https://github.com/hank-cp">Hank CP</a>
 */
@MappedSuperclass
@Getter
public class Permission extends CodeBasedEntity implements PermissionRegistry {

    @NotNull
    @Transient
    public String realmCode;

    @NotNull
    public String name;

    public int vipLevel;

    @Column(updatable = false)
    public long bit1 = 1;

    @Column(updatable = false)
    public long bit2 = 1;

    @Column(updatable = false)
    public long bit3 = 1;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public RealmRegistry getRealm() {
        Realm realm = new Realm();
        realm.code = realmCode;
        return realm;
    }

    public static Permission of(@NotNull String code,
                                @NotNull String name) {
        Permission permission = new Permission();
        permission.code = code;
        permission.name = name;
        return permission;
    }

    //*************************************************************************
    // Domain Methods
    //*************************************************************************

    @Override
    public PermissionResult checkAuthorization(JwtPrincipal principal) {
        SimpleJwtPrincipal jwtPrincipal;
        if (principal instanceof SimpleJwtPrincipal) {
            jwtPrincipal = (SimpleJwtPrincipal) principal;
        } else {
            throw new ConfException(ErrorInfo.NOT_SUPPORT_AUTH_PRINCIPAL
                    .setMsg("Not support auth principal %s", principal.getClass().getName()));
        }
        boolean granted = (jwtPrincipal.getPermissionBit1() & bit1) != 0
                && (jwtPrincipal.getPermissionBit2() & bit2) != 0
                && (jwtPrincipal.getPermissionBit3() & bit3) != 0;
        if (!granted) return PermissionResult.PERMISSION_DENIED;

        if (jwtPrincipal.getVipLevel() < getVipLevel())
            return PermissionResult.PERMISSION_LIMITATION;

        return PermissionResult.PERMISSION_GRANTED;
    }
}
