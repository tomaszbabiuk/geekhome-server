package com.geekhome.coremodule;

import com.geekhome.common.DescriptiveName;
import com.geekhome.common.commands.AlertServiceBase;
import com.geekhome.http.ILocalizationProvider;
import com.geekhome.httpserver.modules.CollectorCollection;

public class DashboardAlertService extends AlertServiceBase {
    private static final String ALERT_SYSTEM_RESTART = "system_restart";
    private static final String ALERT_SYSTEM_SHADOW = "system_shadow";
    private final ILocalizationProvider _localizationProvider;
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

    public void clearAlert(String alertId) {
        _alerts.remove(alertId);
    }

    public DashboardAlertService(ILocalizationProvider localizationProvider) {
        super(new DescriptiveName(localizationProvider.getValue("C:Dashboard"), "dashboard"), true);
        _localizationProvider = localizationProvider;
    }

    public void raiseShadowPortsInUse() {
        DescriptiveName alertName = new DescriptiveName(_localizationProvider.getValue("C:ShadowPortsInUse"), ALERT_SYSTEM_SHADOW);
        Alert alert = new Alert(alertName, "");
        raiseAlert(alert);
    }

    public void clearShadowPortsInUse() {
        clearAlert(ALERT_SYSTEM_SHADOW);
    }

    public void raiseRestartAlert() {
        DescriptiveName restartAlertName = new DescriptiveName(_localizationProvider.getValue("C:SystemRestartScheduled"), ALERT_SYSTEM_RESTART);
        Alert restartAlert = new Alert(restartAlertName, "");
        raiseAlert(restartAlert);
    }


}
