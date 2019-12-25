package com.geekhome.coremodule;

import com.geekhome.common.KeyValue;
import com.geekhome.common.configuration.JSONArrayList;

import java.io.IOException;

public interface IImagesLister
{
    JSONArrayList<KeyValue> listImages() throws IOException;
}