var EXF = {
    extaFreeBlinds: null,
    
    /* BLINDS */
	GetExtaFreeBlinds: function() {
		if (this.extaFreeBlinds == null) {
			this.extaFreeBlinds = SortByDescriptiveName(ajaxInit('/config/extafreeblinds.json').Result);
		}
		return this.extaFreeBlinds;
	},

	FindExtaFreeBlind: function(id) {
		return FindDescriptiveNameObject(id, EXF.GetExtaFreeBlinds());
	},

	GetXtaaFreePorts: function() {
		var ports = new Array();
		for(var iPort in HM.GetTogglePorts()) {
			var port = HM.GetTogglePorts()[iPort];
			if (port.Id.substring(0,9) == 'ExtaFree-') {
				ports.push(port);
			}
		}
		return ports;
	},
}

