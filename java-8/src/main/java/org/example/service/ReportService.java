package org.example.service;

import org.example.domain.Order;
import org.example.domain.Report;

import java.util.List;

public interface ReportService {

    Report generateReport(List<Order> orders);
}
