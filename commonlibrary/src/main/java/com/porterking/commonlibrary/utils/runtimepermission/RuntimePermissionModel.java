package com.porterking.commonlibrary.utils.runtimepermission;


/**
 *  Created by ms on 17-9-18.
 */

public class RuntimePermissionModel {

    public PermissionRequestManager.OnPermissionRequestResultCallback callback;
    public String[] permissions;

    RuntimePermissionModel(PermissionRequestManager.OnPermissionRequestResultCallback callback, String[] permissions) {
        this.callback = callback;
        this.permissions = permissions;
    }

    public boolean isValid() {
        return callback != null && permissions != null && permissions.length > 0;
    }
}
