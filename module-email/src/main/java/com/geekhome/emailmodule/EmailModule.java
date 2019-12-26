package com.geekhome.emailmodule;

import com.geekhome.common.IMonitorable;
import com.geekhome.coremodule.commands.CommandsProcessor;
import com.geekhome.common.alerts.IAlertService;
import com.geekhome.http.Resource;
import com.geekhome.common.automation.MasterAutomation;
import com.geekhome.common.settings.AutomationSettings;
import com.geekhome.emailmodule.httpserver.EmailJsonRequestsDispatcher;
import com.geekhome.emailmodule.httpserver.EmailPostRequestsDispatcher;
import com.geekhome.http.IRequestsDispatcher;
import com.geekhome.httpserver.ICrudPostHandler;
import com.geekhome.http.ILocalizationProvider;
import com.geekhome.httpserver.modules.IUnit;
import com.geekhome.httpserver.modules.Module;
import com.geekhome.httpserver.modules.Unit;
import com.geekhome.httpserver.modules.UnitCategory;
import java.util.ArrayList;

public class EmailModule extends Module {
    public static final String TABLE_TRUSTED_RECIPIENTS = "TrustedRecipients";
    private EmailService _emailService;
    private EmailAlertAndCommandService _emailAlertService;
    private ILocalizationProvider _localizationProvider;
    private final MasterAutomation _masterAutomation;
    private AutomationSettings _automationSettings;
    private CommandsProcessor _commandsProcessor;

    public EmailModule(ILocalizationProvider localizationProvider,
                       MasterAutomation masterAutomation,
                       AutomationSettings automationSettings,
                       CommandsProcessor commandsProcessor) throws Exception {
        _localizationProvider = localizationProvider;
        _masterAutomation = masterAutomation;
        _automationSettings = automationSettings;
        _commandsProcessor = commandsProcessor;
    }

    @Override
    public void initialize() throws Exception{
        _emailService = new EmailService(_automationSettings, _localizationProvider);
        _emailAlertService = new EmailAlertAndCommandService(_emailService, _localizationProvider,
                _masterAutomation, _automationSettings, _commandsProcessor);
    }

    @Override
    public ArrayList<IUnit> createUnits() {
        ArrayList<IUnit> units = new ArrayList<>();
        units.add(new Unit(UnitCategory.ConfigurationSettings, _localizationProvider.getValue("E:EmailControl"), "letter", "/config/email.htm"));
        return units;
    }

    @Override
    public Resource[] getResources() {
        return new Resource[] {
                new Resource("E:AddTrustedRecipient", "Add trusted recipient", "Dodaj zaufanego odbiorcę"),
                new Resource("E:EmailControl", "Email control", "Sterowanie przez E-mail"),
                new Resource("E:EmailMessage", "Email message", "Wiadomość email"),
                new Resource("E:EmailAddress", "Email address", "Adres email"),
                new Resource("E:IMAPHost", "IMAP host", "Host IMAP"),
                new Resource("E:SendingEmailsSettings", "Senging e-mails settings", "Ustawienia wysyłania maili"),
                new Resource("E:SMTPHost", "SMTP host", "Host SMTP"),
                new Resource("E:TrustedRecipient", "Trusted recipient", "Zaufany odbiorca"),
                new Resource("E:TrustedRecipientDetails", "Trusted recipient details", "Szczegóły zaufanego odbiorcy"),
                new Resource("E:Re", "Re", "Odp"),
                new Resource("E:EmailAlreadyExists", "Trusted recipient with such email already exists!", "Zaufany odbiorca o takim adresie email już istnieje!")
        };
    }

    @Override
    public String getTextResourcesPrefix() {
        return "E";
    }

    @Override
    public void addAlertService(ArrayList<IAlertService> alertServices) throws Exception {
        alertServices.add(_emailAlertService);
    }

    @Override
    public void addMonitorable(ArrayList<IMonitorable> monitorables) {
        monitorables.add(_emailService);
    }

    @Override
    public ArrayList<IRequestsDispatcher> createDispatchers() {
        ArrayList<IRequestsDispatcher> dispatchers = new ArrayList<>();
        dispatchers.add(new EmailJsonRequestsDispatcher(_automationSettings));
        dispatchers.add(new EmailPostRequestsDispatcher(_automationSettings));
        return dispatchers;
    }

    @Override
    public ArrayList<ICrudPostHandler> createCrudPostHandlers() {
        ArrayList<ICrudPostHandler> handlers = new ArrayList<>();
        handlers.add(new TrustedRecipientsCrudHandler(_automationSettings, _localizationProvider));
        return handlers;
    }

    @Override
    public String[] listConsolidatedStyleSheets() {
        return new String[]
            {
                "CSS\\CUSTOMEMAIL.CSS",
            };
    }

    @Override
    public String[] listConsolidatedJavascripts() {
        return new String[]
            {
                "JS\\CUSTOMEMAIL.JS",
            };
    }
}