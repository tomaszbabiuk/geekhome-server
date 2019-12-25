package com.geekhome.httpserver;

import com.geekhome.common.OperationMode;

public interface IOperationModeChangedListener {
    void onChanged(OperationMode operationMode) throws Exception;
}
