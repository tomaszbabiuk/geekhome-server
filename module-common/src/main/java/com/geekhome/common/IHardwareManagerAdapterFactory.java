package com.geekhome.common;

import com.geekhome.common.hardwaremanager.IHardwareManagerAdapter;

import java.util.ArrayList;

public interface IHardwareManagerAdapterFactory {
    ArrayList<? extends IHardwareManagerAdapter> createAdapters();
}
