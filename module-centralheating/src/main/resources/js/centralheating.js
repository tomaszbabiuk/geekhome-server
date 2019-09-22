var CH = {
	thermostatConditions: null,
	temperatureControllers: null,
	thermometers: null,
	hygrometers: null,
	averagingThermometers: null,
	comfortmeters: null,
	heatingManifolds: null,
	airConditioners: null,
	circulationPumps: null,
	radiators: null,
	underfloorCircuits: null,
	rtlCircuits: null,
	allCircuits: null,
	allThermometers: null,


	/* CIRCULATION PUMPS */
	GetCirculationPumps: function() {
		if (this.circulationPumps == null) {
			this.circulationPumps = SortByDescriptiveName(ajaxInit('/config/circulationpumps.json').Result);
		}
		return this.circulationPumps;
	},

	FindCirculationPump: function(id) {
		return FindDescriptiveNameObject(id, CH.GetCirculationPumps());
	},

	CanAddCirculationPumps: function () {
		return ajaxInit('/config/canaddcirculationPumps.json').Result;
	},


	/* THERMOSTAT CONDITIONS */
	GetThermostatConditions: function() {
		if (this.thermostatConditions == null) {
			this.thermostatConditions = SortByDescriptiveName(ajaxInit('/config/thermostatconditions.json').Result);
		}
		return this.thermostatConditions;
	},

	FindThermostatCondition: function(id) {
		return FindDescriptiveNameObject(id, CH.GetThermostatConditions());
	},

	CanAddThermostatConditions: function() {
		return ajaxInit('/config/canaddthermostatconditions.json').Result;
	},


	/* TEMPERATURE CONTROLLERS */
	GetTemperatureControllers: function() {
		if (this.temperatureControllers == null) {
			this.temperatureControllers = SortByDescriptiveName(ajaxInit('/config/temperaturecontrollers.json').Result);
		}
		return this.temperatureControllers;
	},

	FindTemperatureController: function (id) {
		return FindDescriptiveNameObject(id, CH.GetTemperatureControllers());
	},


	/* THERMOMETERS */
	GetThermometers: function() {
		if (this.thermometers == null) {
			this.thermometers = SortByDescriptiveName(ajaxInit('/config/thermometers.json').Result);
		}
		return this.thermometers;
	},

	FindThermometer: function(id) {
		return FindDescriptiveNameObject(id, CH.GetThermometers());
	},


	/* HYGROMETERS */
	GetHygrometers: function() {
		if (this.hygrometers == null) {
			this.hygrometers = SortByDescriptiveName(ajaxInit('/config/hygrometers.json').Result);
		}
		return this.hygrometers;
	},

	FindHygrometer: function(id) {
		return FindDescriptiveNameObject(id, CH.GetHygrometers());
	},


	/* COMFORTMETERS */
	GetComfortmeters: function() {
		if (this.comfortmeters == null) {
			this.comfortmeters = SortByDescriptiveName(ajaxInit('/config/comfortmeters.json').Result);
		}
		return this.comfortmeters;
	},

	FindComfortmeter: function(id) {
		return FindDescriptiveNameObject(id, CH.GetComfortmeters());
	},


	/* AVERAGING THERMOMETERS */
	GetAveragingThermometers: function() {
		if (this.averagingThermometers == null) {
			this.averagingThermometers = SortByDescriptiveName(ajaxInit('/config/averagingthermometers.json').Result);
		}
		return this.averagingThermometers;
	},

	FindAveragingThermometer: function(id) {
		return FindDescriptiveNameObject(id, CH.GetAveragingThermometers());
	},

	CanAddAveragingThermometers: function() {
		return ajaxInit('/config/canaddaveragingthermometers.json').Result;
	},

	ThermometersIdsToNames: function(thermometersIds) {
		return IdsToNames(thermometersIds, CH.FindThermometer);
	},


	/* HEATING MANIFOLDS */
	GetHeatingManifolds: function() {
		if (this.heatingManifolds == null) {
			this.heatingManifolds = SortByDescriptiveName(ajaxInit('/config/heatingmanifolds.json').Result);
		}
		return this.heatingManifolds;
	},

	FindHeatingManifold: function(id) {
		return FindDescriptiveNameObject(id, CH.GetHeatingManifolds());
	},

	CanAddHeatingManifolds: function() {
		return ajaxInit('/config/canaddheatingmanifolds.json').Result;
	},


	/* AIR CONDITIONERS */
	GetAirConditioners: function() {
		if (this.airConditioners == null) {
			this.airConditioners = SortByDescriptiveName(ajaxInit('/config/airconditioners.json').Result);
		}
		return this.airConditioners;
	},

	FindAirConditioner: function(id) {
		return FindDescriptiveNameObject(id, CH.GetAirConditioners());
	},

	CanAddAirConditioners: function() {
		return ajaxInit('/config/canaddairconditioners.json').Result;
	},

	/* RADIATORS */
	GetRadiators: function () {
		if (this.radiators == null) {
			this.radiators = SortByDescriptiveName(ajaxInit('/config/radiators.json').Result);
		}
		return this.radiators;
	},

	FindRadiator: function(id) {
		return FindDescriptiveNameObject(id, CH.GetRadiators());
	},


	/* UNDERFLOOR CIRCUITS */
	GetUnderfloorCircuits: function () {
		if (this.underfloorCircuits == null) {
			this.underfloorCircuits = SortByDescriptiveName(ajaxInit('/config/underfloorcircuits.json').Result);
		}
		return this.underfloorCircuits;
	},

	FindUnderfloorCircuit: function(id) {
		return FindDescriptiveNameObject(id, CH.GetUnderfloorCircuits());
	},


	/* RTL CIRCUITS */
	GetRtlCircuits: function () {
		if (this.rtlCircuits == null) {
			this.rtlCircuits = SortByDescriptiveName(ajaxInit('/config/rtlcircuits.json').Result);
		}
		return this.rtlCircuits;
	},

	FindRtlCircuit: function(id) {
		return FindDescriptiveNameObject(id, CH.GetRtlCircuits());
	},

	CanAddCircuits: function() {
		return ajaxInit('/config/canaddcircuits.json').Result;
	},


	/* ALL THERMOMETERS */
	GetAllThermometers: function () {
		if (this.allThermometers == null) {
			this.allThermometers = SortByDescriptiveName(ajaxInit('/config/allthermometers.json').Result);
		}
		return this.allThermometers;
	},

	FindAnyThermometer: function(id) {
		return FindDescriptiveNameObject(id, CH.GetAllThermometers());
	},


	/* ALL CIRCUITS */
	GetAllCircuits: function () {
		if (this.allCircuits == null) {
			this.allCircuits = SortByDescriptiveName(ajaxInit('/config/allcircuits.json').Result);
		}
		return this.allCircuits;
	},

	FindCircuitsByTemperatureControllerId: function(temperatureControllerId) {
	    var result = new Array();
        for (var iCircuit in CH.GetAllCircuits()) {
            var circuit = CH.GetAllCircuits()[iCircuit];
            if (circuit.TemperatureControllerId == temperatureControllerId) {
                result.push(circuit);
            }
        }
	    return result;
	},

	FindAnyCircuit: function(id) {
		return FindDescriptiveNameObject(id, CH.GetAllCircuits());
	},

    CircuitsIdsToNames: function(circuitsIds) {
        return IdsToNames(circuitsIds, CH.FindAnyCircuit);
    },
}
