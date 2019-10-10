var AUTO = {
	onOffDevices: null,
	impulseSwitches: null,
	nfcConditions: null,
    intensityDevices: null,
    powerMeters: null,

	/* ONOFF DEVICES */
	GetOnOffDevices: function() {
		if (this.onOffDevices == null) {
			this.onOffDevices = SortByDescriptiveName(ajaxInit('/config/onoffdevices.json').Result);
		}
		return this.onOffDevices;
	},

	FindOnOffDevice: function(id) {
		return FindDescriptiveNameObject(id, AUTO.GetOnOffDevices());
	},

    /* IMPULSE SWITCHES */
    GetImpulseSwitches: function() {
		if (this.impulseSwitches == null) {
			this.impulseSwitches = SortByDescriptiveName(ajaxInit('/config/impulseswitches.json').Result);
		}
		return this.impulseSwitches;
	},

	FindImpulseSwitch: function(id) {
		return FindDescriptiveNameObject(id, AUTO.GetImpulseSwitches());
	},

	/* NFC CONDITIONS */
	GetNfcConditions: function() {
		if (this.nfcConditions == null) {
			this.nfcConditions = SortByDescriptiveName(ajaxInit('/config/nfcconditions.json').Result);
		}
		return this.nfcConditions;
	},

	FindNfcCondition: function(id) {
		return FindDescriptiveNameObject(id, AUTO.GetNfcConditions());
	},
	
    /* INTENSITY DEVICES */
    GetIntensityDevices: function() {
        if (this.intensityDevices == null) {
            this.intensityDevices = SortByDescriptiveName(ajaxInit('/config/intensitydevices.json').Result);
        }
        return this.intensityDevices;
    },

    FindIntensityDevice: function(id) {
        return FindDescriptiveNameObject(id, AUTO.GetIntensityDevices());
    },
    
    /* POWER METERS */
    GetPowerMeters: function() {
        if (this.powerMeters == null) {
            this.powerMeters = SortByDescriptiveName(ajaxInit('/config/powermeters.json').Result);
        }
        return this.powerMeters;
    },

    FindPowerMeter: function(id) {
        return FindDescriptiveNameObject(id, AUTO.GetPowerMeters());
    },
}

