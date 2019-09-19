var LIGHTS = {
    twilightConditions: null,
	impulseSwitches: null,	
    blinds: null,
    rgbLamps: null,
    sevenColorLamps: null,
    luxmeters: null,

	/* BLINDS */
	GetBlinds: function() {
		if (this.blinds == null) {
			this.blinds = SortByDescriptiveName(ajaxInit('/config/blinds.json').Result);
		}
		return this.blinds;
	},

	FindBlind: function(id) {
		return FindDescriptiveNameObject(id, LIGHTS.GetBlinds());
	},

	/* RGB LAMPS */
	GetRgbLamps: function() {
		if (this.rgbLamps == null) {
			this.rgbLamps = SortByDescriptiveName(ajaxInit('/config/rgblamps.json').Result);
		}
		return this.rgbLamps;
	},

	FindRgbLamp: function(id) {
		return FindDescriptiveNameObject(id, LIGHTS.GetRgbLamps());
	},

    /* 7-COLOR LAMPS */
    Get7ColorLamps: function() {
        if (this.sevenColorLamps == null) {
            this.sevenColorLamps = SortByDescriptiveName(ajaxInit('/config/7colorlamps.json').Result);
        }
        return this.sevenColorLamps;
    },

    Find7ColorLamp: function(id) {
        return FindDescriptiveNameObject(id, LIGHTS.Get7ColorLamps());
    },

	/* LUXMETERS */
	GetLuxmeters: function() {
		if (this.luxmeters == null) {
			this.luxmeters = SortByDescriptiveName(ajaxInit('/config/luxmeters.json').Result);
		}
		return this.luxmeters;
	},

	FindLuxmeter: function(id) {
		return FindDescriptiveNameObject(id, LIGHTS.GetLuxmeters());
	},

    /* TWILIGHT CONDITIONS */
   	GetTwilightConditions: function() {
		if (this.twilightConditions == null) {
			this.twilightConditions = SortByDescriptiveName(ajaxInit('/config/twilightConditions.json').Result);
		}
		return this.twilightConditions;
	},

	FindTwilightCondition: function(id) {
		return FindDescriptiveNameObject(id, LIGHTS.GetTwilightConditions());
	},
}

