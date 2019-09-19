var VENT = {
	recuperators: null,

	/* RECUPERATORS */
	GetRecuperators: function() {
		if (this.recuperators == null) {
			this.recuperators = SortByDescriptiveName(ajaxInit('/config/recuperators.json').Result);
		}
		return this.recuperators;
	},

	FindRecuperator: function(id) {
		return FindDescriptiveNameObject(id, VENT.GetRecuperators());
	},

	CanAddRecuperators: function() {
		return ajaxInit('/config/canaddrecuperators.json').Result;
	},
}
