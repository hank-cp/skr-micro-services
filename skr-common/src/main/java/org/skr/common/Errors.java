package org.skr.common;

import com.rits.cloning.Cloner;
import org.skr.config.json.StringValuedEnum;

import java.util.Objects;

public final class Errors {

    public enum ErrorLevel implements StringValuedEnum {
        WARNING("warn"), FAIL("fail");

        private final String value;

        ErrorLevel(String value) {
            this.value = value;
        }

        @Override
        public String value() {
            return value;
        }

        public static ErrorLevel parse(String value) {
            for (ErrorLevel item : ErrorLevel.values()) {
                if (!Objects.equals(item.value(), value)) continue;
                return item;
            }
            return FAIL;
        }
    }


    public int code;

    public String path;

    public ErrorLevel level;

    public String msg;

    public Errors(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Errors setMsg(String msg) {
        this.msg = msg;
        return Cloner.shared().shallowClone(this);
    }

    public Errors setPath(String path) {
        this.path = path;
        return Cloner.shared().shallowClone(this);
    }

    public Errors setLevel(ErrorLevel level) {
        this.level = level;
        return Cloner.shared().shallowClone(this);
    }

    //*************************************************************************
    // Common Errors Definition
    //*************************************************************************

    public static final Errors OK = new Errors(0, null);
    public static final Errors INTERNAL_SERVER_ERROR    = new Errors(1001, "Internal server error.");
    public static final Errors ENTITY_NOT_FOUND         = new Errors(1002, "Entity not found.");
    public static final Errors DELETION_RESTRICTED      = new Errors(1003, "Deletion restricted.");
    public static final Errors INVALID_SUBMITTED_DATA   = new Errors(1004, "Invalid submitted data.");
    public static final Errors INVALID_SERVER_DATA      = new Errors(1005, "Invalid server data.");
    public static final Errors SAVE_DATA_FAILED         = new Errors(1006, "Save data failed.");
    public static final Errors DUPLICATED_ENTITY        = new Errors(1007, "Duplicated entity.");
    public static final Errors AUTHENTICATION_REQUIRED  = new Errors(1008, "Authentication required.");
    public static final Errors PERMISSION_DENIED        = new Errors(1009, "Permission Denied.");
    public static final Errors MODEL_DEFINITION_ERROR   = new Errors(1010, "Model definition error.");

    public static final Errors NOT_AUTHENTICATED            = new Errors(1100, "Account is not authenticated.");
    public static final Errors ACCOUNT_NOT_BELONG_TO_ORG    = new Errors(1101, "Account is not belong to org.");
    public static final Errors USER_DISABLED                = new Errors(1102, "User is disabled.");
    public static final Errors USER_NEED_APPROVAL           = new Errors(1103, "User need to be approved.");
    public static final Errors USER_REJECTED                = new Errors(1104, "User joining get rejected.");
    public static final Errors ORGANIZATION_NOT_EXISTED     = new Errors(1105, "Organization is not existed.");
}
