package com.geekhome.httpserver;

import com.geekhome.common.CrudAction;
import com.geekhome.http.INameValueSet;

public interface CrudModificationFunctionDelegate {
    void execute(CrudAction action, INameValueSet values);
}
