package com.geekhome.common;

import java.util.ArrayList;

public interface IHardwareManagerAdapterFactory {
    ArrayList<? extends IHardwareManagerAdapter> createAdapters();
}
