var SI = {
    modules: null,
    isDateReliable: null,
    alertServices: null,

    GetStatus: function () {
        return ajaxInit('/systeminfo/status.json').Result;
    },

    SetOperationMode: function(mode, callback) {
    	jQuery.ajax({
            type: 'POST',
            url: '/changeoperationmode.post',
    		data: '?mode=' + mode,
            dataType: 'text',
            success: callback,
            async: true
        });
    },

    GetModules: function () {
        if (this.modules == null) {
            this.modules = ajaxInit('/systeminfo/modules.json');
        }
        return this.modules;
    },

    GetAlertServices: function () {
        if (this.alertServices == null) {
            this.alertServices = ajaxInit('/systeminfo/alertservices.json');
        }
        return this.alertServices;
    },

    GetMonitorables: function() {
        return ajaxInit('/systeminfo/monitorables.json');
    },

    FindAlertService: function(id) {
        return FindDescriptiveNameObject(id, SI.GetAlertServices());
    },

    AlertServicesIdsToNames: function (alertServicesIds) {
        return IdsToNames(alertServicesIds, SI.FindAlertService);
    },

    IsDateReliable: function () {
        if (this.isDateReliable == null) {
            this.isDateReliable = ajaxInit('/systeminfo/isdatereliable.json').Result;
        }
        return this.isDateReliable;
    },

    Reboot: function(callback) {
    	jQuery.ajax({
            type: 'POST',
            url: '/reboot.post',
            dataType: 'text',
            success: callback,
            async: true
        });
    },

    Shutdown: function(callback) {
    	jQuery.ajax({
            type: 'POST',
            url: '/shutdown.post',
            dataType: 'text',
            success: callback,
            async: true
        });
    },

	GetUnits: function(category) {
		var result = [];
		for (var iModule in this.GetModules()) {
			var module = this.GetModules()[iModule];
            for (var iUnit in module.Units) {
				var unit = module.Units[iUnit];
                if (unit.Category == category) {
					result.push(unit);
                }
            }
		}

		return result;
	}
}
