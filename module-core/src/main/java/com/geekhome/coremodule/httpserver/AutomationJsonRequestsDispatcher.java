package com.geekhome.coremodule.httpserver;

import com.geekhome.common.configuration.*;
import com.geekhome.common.State;
import com.geekhome.coremodule.*;
import com.geekhome.coremodule.automation.EvaluationResult;
import com.geekhome.coremodule.automation.IDeviceAutomationUnit;
import com.geekhome.coremodule.automation.MasterAutomation;
import com.geekhome.http.IHttpListenerRequest;
import com.geekhome.http.IResponse;
import com.geekhome.http.ILocalizationProvider;
import com.geekhome.http.jetty.JsonRequestsDispatcherBase;
import com.geekhome.httpserver.JsonResponse;
import com.geekhome.common.configuration.CollectorCollection;
import org.json.simple.JSONObject;

import java.util.Objects;


public class AutomationJsonRequestsDispatcher extends JsonRequestsDispatcherBase {
    private MasterConfiguration _masterConfiguration;
    private MasterAutomation _masterAutomation;
    private ILocalizationProvider _localizationProvider;

    public AutomationJsonRequestsDispatcher(MasterConfiguration masterConfiguration, MasterAutomation masterAutomation, ILocalizationProvider localizationProvider) {
        _masterConfiguration = masterConfiguration;
        _masterAutomation = masterAutomation;
        _localizationProvider = localizationProvider;
    }

    @Override
    public IResponse dispatch(IHttpListenerRequest request) throws Exception {
        String originalStringUppercased = request.getUrl().getUrlNoQuery().toUpperCase();
        if (originalStringUppercased.equals("/GETDEVICESTATES.JSON")) {
            String id = request.getUrl().getQueryString().getValues().getValue("id");
            MultistateDevice device = (MultistateDevice) _masterConfiguration.searchDevice(id);
            CollectorCollection<State> states = device.buildStates(_localizationProvider);
            return new JsonResponse(JsonResponse.createJSONResult(states), false);
        }

        if (originalStringUppercased.equals("/GETBLOCKCATEGORIES.JSON")) {
            String id = request.getUrl().getQueryString().getValues().getValue("id");
            IBlocksTarget device = (IBlocksTarget) _masterConfiguration.searchDevice(id);
            JSONArrayList<DescriptiveName> categories = device.buildBlockCategories(_localizationProvider);
            return new JsonResponse(JsonResponse.createJSONResult(categories), false);
        }

        if (originalStringUppercased.equals("/CHECKMODEEVALUATION.JSON")) {
            String id = request.getUrl().getQueryString().getValues().getValue("id");
            boolean passed = _masterAutomation.findModeUnit(id).isPassed();
            return new JsonResponse(JsonResponse.createJSONResult(passed), false);
        }

        if (originalStringUppercased.equals("/CHECKALERTEVALUATION.JSON")) {
            String id = request.getUrl().getQueryString().getValues().getValue("id");
            boolean passed = _masterAutomation.findAlertUnit(id).isPassed();
            return new JsonResponse(JsonResponse.createJSONResult(passed), false);
        }

        if (originalStringUppercased.equals("/CHECKCONDITIONEVALUATION.JSON")) {
            String id = request.getUrl().getQueryString().getValues().getValue("id");
            if (id.startsWith("!")) {
                String nonNegatedId = id.substring(1);
                boolean passed = !_masterAutomation.findConditionUnit(nonNegatedId).isPassed();
                return new JsonResponse(JsonResponse.createJSONResult(passed), false);
            }

            boolean passed = _masterAutomation.findConditionUnit(id).isPassed();
            return new JsonResponse(JsonResponse.createJSONResult(passed), false);
        }

        if (originalStringUppercased.equals("/CHECKBLOCKEVALUATION.JSON")) {
            String id = request.getUrl().getQueryString().getValues().getValue("id");
            boolean passed = _masterAutomation.findBlockUnit(id).isPassed();
            return new JsonResponse(JsonResponse.createJSONResult(passed), false);
        }

        if (originalStringUppercased.equals("/CHECKDEVICEEVALUATION.JSON")) {
            String id = request.getUrl().getQueryString().getValues().getValue("id");
            IDeviceAutomationUnit deviceUnit = _masterAutomation.findDeviceUnit(id);
            EvaluationResult evaluationResult = deviceUnit.buildEvaluationResult();
            return new JsonResponse(evaluationResult, false);
        }

        if (originalStringUppercased.equals(("/DEVICESGROUPSBYCATEGORY.JSON"))) {
            final String category = request.getUrl().getQueryString().getValues().getValue("category");
            CollectorCollection<DevicesGroup> groups = new CollectorCollection<>();
            for (CollectorCollection<? extends IDevice> collector : _masterConfiguration.getDevicesCollectors()) {
                for (IDevice device : collector.values()) {
                    if (Objects.equals(device.getCategory().toString(), category)) {
                        String groupUniqueId = device.getClass().getSimpleName();
                        DevicesGroup targetGroup = groups.find(groupUniqueId, false);
                        if (targetGroup == null) {
                            targetGroup = new DevicesGroup(new DescriptiveName(device.getGroupName(_localizationProvider), groupUniqueId));
                            groups.add(targetGroup);
                        }

                        targetGroup.getDevices().add(device);
                    }
                }
            }
            JSONObject json = JsonResponse.createJSONResult(groups);
            return new JsonResponse(json, false);
        }

        return null;
    }
}

