package com.geekhome.coremodule;

import com.geekhome.common.DescriptiveName;
import com.geekhome.common.commands.AlertServiceBase;
import com.geekhome.http.ILocalizationProvider;
import com.geekhome.httpserver.modules.CollectorCollection;

public class DashboardAlertService extends AlertServiceBase {
    private CollectorCollection<Alert> _alerts = new CollectorCollection<>();

    public CollectorCollection<Alert> getAlerts() {
        return _alerts;
    }

    @Override
    public void raiseAlert(Alert alert) {
        _alerts.add(alert);
    }

    public void clearAllAlerts() {
        _alerts.clear();
    }

    @Override
    public void clearAlert(Alert alert) {
        _alerts.remove(alert.getName().getUniqueId());
    }

    public DashboardAlertService(ILocalizationProvider localizationProvider) {
        super(new DescriptiveName(localizationProvider.getValue("C:Dashboard"), "dashboard"), true);
    }
}
