package com.geekhome.hardwaremanagermodule;

import com.geekhome.common.IHardwareManagerAdapter;
import com.geekhome.common.IHardwareManagerAdapterFactory;
import com.geekhome.http.ILocalizationProvider;

import java.util.ArrayList;

class MockedAdapterFactory implements IHardwareManagerAdapterFactory {

    private final ILocalizationProvider _localizationProvider;

    MockedAdapterFactory(ILocalizationProvider localizationProvider) {
        _localizationProvider = localizationProvider;
    }

    @Override
    public ArrayList<? extends IHardwareManagerAdapter> createAdapters() {
        ArrayList<IHardwareManagerAdapter> result = new ArrayList<>();
        MockedAdapter mockedAdapter = new MockedAdapter(_localizationProvider);
        result.add(mockedAdapter);

        return result;
    }
}