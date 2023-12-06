package org.example;

import org.example.domain.Order;
import org.example.domain.Organization;
import org.example.domain.Permission;
import org.example.domain.Report;
import org.example.domain.User;
import org.example.service.OrderService;
import org.example.service.OrganizationService;
import org.example.service.PermissionService;
import org.example.service.ReportService;
import org.example.service.UserService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Sandbox {

    private static final String GENERATE_REPORT_PERMISSION = "GENERATE_REPORT";

    private OrderService orderService;
    private OrganizationService organizationService;
    private UserService userService;
    private PermissionService permissionService;
    private ReportService reportService;


    public Report generateReportForCurrentDate(String userId) {
        Report report = null;
        User user = userService.getUserById(userId);

        if (user != null) {
            Organization organization = organizationService.getOrganizationById(user.getOrganizationId());

            if (organization != null && organization.getPermissions() != null &&
                !organization.getPermissions().isEmpty()) {
                List<Permission> permissions = permissionService.getPermissionsById(organization.getPermissions());
                Permission generateReportPermission = null;

                for (Permission permission : permissions) {
                    if (GENERATE_REPORT_PERMISSION.equals(permission.getAlias())) {
                        generateReportPermission = permission;
                        break;
                    }
                }

                List<Order> ordersForReport = new ArrayList<>();
                if (generateReportPermission != null && generateReportPermission.getAssignedTo() != null &&
                    generateReportPermission.getAssignedTo().contains(userId)) {
                    List<Order> orders = orderService.getOrdersByOrganizationId(organization.getId());

                    Map<String, List<Order>> orderDateToOrderMap = new HashMap<>();

                    orders.forEach(order -> {
                        List<Order> ordersForDate = orderDateToOrderMap.get(order.getDate());
                        if (ordersForDate != null) {
                            ordersForDate.add(order);
                        } else {
                            orderDateToOrderMap.put(order.getDate(), new ArrayList<>(List.of(order)));
                        }
                    });

                    List<Order> ordersForDate = orderDateToOrderMap.get(LocalDate.now().toString());
                    ordersForReport.addAll(ordersForDate);
                }

                if (!ordersForReport.isEmpty()) {
                    report = reportService.generateReport(ordersForReport);
                }
            }
        }
        return report;
    }
}
