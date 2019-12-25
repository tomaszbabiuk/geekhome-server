package com.geekhome.common.configuration;

import com.geekhome.common.INamedObject;
import com.geekhome.http.ILocalizationProvider;

public interface IBlocksTarget extends INamedObject {
    JSONArrayList<DescriptiveName> buildBlockCategories(ILocalizationProvider localizationProvider);
}
