package org.example.service;

import org.example.domain.Permission;

import java.util.List;

public interface PermissionService {

    List<Permission> getPermissionsById(List<String> ids);
}
