package com.geekhome.usersmodule;

import com.geekhome.common.localization.Resource;
import com.geekhome.coremodule.httpserver.IRequestsDispatcher;
import com.geekhome.coremodule.httpserver.ICrudPostHandler;
import com.geekhome.common.localization.ILocalizationProvider;
import com.geekhome.coremodule.modules.IUnit;
import com.geekhome.coremodule.modules.Module;
import com.geekhome.coremodule.modules.Unit;
import com.geekhome.coremodule.modules.UnitCategory;
import com.geekhome.usersmodule.httpserver.UsersJsonRequestsDispatcher;

import java.util.ArrayList;

public class UsersModule extends Module {
    private final JettyRealmPersister _usersPersister;
    private ILocalizationProvider _localizationProvider;

    public UsersModule(ILocalizationProvider localizationProvider) throws Exception {
        _localizationProvider = localizationProvider;
        _usersPersister = new JettyRealmPersister();
    }

    @Override
    public ArrayList<IUnit> createUnits() {
        ArrayList<IUnit> units = new ArrayList<>();
        units.add(new Unit(UnitCategory.ConfigurationSettings, _localizationProvider.getValue("U:WWWUsers"), "people", "/config/users.htm"));
        return units;
    }

    @Override
    public Resource[] getResources() {
        return new Resource[] {
                new Resource("U:AddUser", "Add user", "Dodaj użytkownika"),
                new Resource("U:DefaultUserWillBeCreated",
                        "If last user is deleted a default user (name: user, password: user) will be recreated!",
                        "Jeżeli ostatni użytkownik zostanie usunięty, system automatycznie odtworzy domyślnego użytkownika (nazwa: user, hasło: user)!"),
                new Resource("U:Icon.Boy", "Boy", "Chłopiec"),
                new Resource("U:Icon.Girl", "Girl", "Dziewczyna"),
                new Resource("U:Icon.Man", "Man", "Mężczyzna"),
                new Resource("U:Icon.Woman", "Woman", "Kobieta"),
                new Resource("U:NewPassword", "New password", "Nowe hasło"),
                new Resource("U:User", "User", "Użytkownik"),
                new Resource("U:UserDetails", "User details", "Szczegóły użytkownika"),
                new Resource("U:WWWUsers", "WWW Users", "Użytkownicy www"),
                new Resource("U:UsersModificationWarning", "The changes of passwords/user accounts require system restart.", "Zmiana haseł/kont użytkowników wymaga restartu systemu!"),
                new Resource("U:UserAlreadyExists", "User with such name already exists!", "Użytkownik o takiej samej nazwie już istnieje!")
        };
    }

    @Override
    public String getTextResourcesPrefix() {
        return "U";
    }

    @Override
    public ArrayList<IRequestsDispatcher> createDispatchers() {
        ArrayList<IRequestsDispatcher> dispatchers = new ArrayList<>();
        dispatchers.add(new UsersJsonRequestsDispatcher(_usersPersister));
        return dispatchers;
    }

    @Override
    public String[] listConsolidatedJavascripts() {
        return new String[] {
                    "JS\\USERSCONFIG.JS",
            };
    }

    @Override
    public String[] listConsolidatedStyleSheets() {
        return new String[] {
                    "CSS\\USERSNAVIGATION.CSS",
            };
    }

    @Override
    public ArrayList<ICrudPostHandler> createCrudPostHandlers() {
        ArrayList<ICrudPostHandler> handlers = new ArrayList<>();
        handlers.add(new UsersCrudHandler(_usersPersister, _localizationProvider));
        return handlers;
    }
}
