package com.geekhome.httpserver;

import com.geekhome.common.Persistable;
import com.geekhome.common.json.JSONAwareBase;
import com.geekhome.common.logging.LoggingService;
import com.geekhome.common.logging.ILogger;
import com.geekhome.coremodule.DashboardAlertService;

public class DiagnosticInfo extends JSONAwareBase {
    private static ILogger _logger = LoggingService.getLogger();
    private DashboardAlertService _dashboardAlertService;

    public DiagnosticInfo(DashboardAlertService dashboardAlertService) {
        _dashboardAlertService = dashboardAlertService;
    }

    @Persistable(name="HasErrors")
    public boolean hasErrors() {
        return _logger.getErrorsCount() > 0;
    }

    @Persistable(name="HasAlerts")
    public boolean hasAlerts() {
        return _dashboardAlertService.getAlerts().size() > 0;
    }

    @Persistable(name="ErrorsCount")
    public int errorsCount() {
        return _logger.getErrorsCount();
    }
}