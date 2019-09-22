var HM = {
	digitalOutputPorts: null,
	digitalInputPorts: null,
	temperaturePorts: null,
	humidityPorts: null,
	luminosityPorts: null,
    powerInputsPorts: null,
    powerOutputPorts: null,
    togglePorts: null,

	GetPowerOutputPorts: function() {
		if (this.powerOutputPorts == null) {
			this.powerOutputPorts = SortById(ajaxInit('/poweroutputports.json').Result);
		}
		return this.powerOutputPorts;
	},

	GetPowerInputPorts: function() {
		if (this.powerInputPorts == null) {
			this.powerInputPorts = SortById(ajaxInit('/powerinputports.json').Result);
		}
		return this.powerInputPorts;
	},

	GetDigitalOutputPorts: function() {
		if (this.digitalOutputPorts == null) {
			this.digitalOutputPorts = SortById(ajaxInit('/digitaloutputports.json').Result);
		}
		return this.digitalOutputPorts;
	},

	GetDigitalInputPorts: function() {
		if (this.digitalInputPorts == null) {
			this.digitalInputPorts = SortById(ajaxInit('/digitalinputports.json').Result);
		}
		return this.digitalInputPorts;
	},

	GetTemperaturePorts: function () {
		if (this.temperaturePorts == null) {
			this.temperaturePorts = SortById(ajaxInit('/temperatureports.json').Result);
		}
		return this.temperaturePorts;
	},

	GetHumidityPorts: function () {
		if (this.humidityPorts == null) {
			this.humidityPorts = SortById(ajaxInit('/humidityports.json').Result);
		}
		return this.humidityPorts;
	},

	GetLuminosityPorts: function () {
		if (this.luminosityPorts == null) {
			this.luminosityPorts = SortById(ajaxInit('/luminosityports.json').Result);
		}
		return this.luminosityPorts;
	},

	GetTogglePorts: function () {
		if (this.togglePorts == null) {
			this.togglePorts = SortById(ajaxInit('/toggleports.json').Result);
		}
		return this.togglePorts;
	},

	HasPowerOutputPorts: function() {
	    return HM.GetPowerOutputPorts().length > 0;
	},

	HasPowerInputPorts: function() {
	    return HM.GetPowerInputPorts().length > 0;
	},

	HasDigitalOutputPorts: function() {
	    return HM.GetDigitalOutputPorts().length > 0;
	},

	HasDigitalInputPorts: function() {
	    return HM.GetDigitalInputPorts().length > 0;
	},

	HasTemperaturePorts: function() {
	    return HM.GetTemperaturePorts().length > 0;
	},

	HasHumidityPorts: function() {
	    return HM.GetHumidityPorts().length > 0;
	},

	HasLuminosityPorts: function() {
	    return HM.GetLuminosityPorts().length > 0;
	},

	HasTogglePorts: function() {
	    return HM.GetTogglePorts().length > 0;
	}
}