package com.geekhome.coremodule;

import com.geekhome.common.configuration.DescriptiveName;
import com.geekhome.common.INamedObject;
import com.geekhome.common.configuration.JSONArrayList;
import com.geekhome.http.ILocalizationProvider;

public interface IBlocksTarget extends INamedObject {
    JSONArrayList<DescriptiveName> buildBlockCategories(ILocalizationProvider localizationProvider);
}
