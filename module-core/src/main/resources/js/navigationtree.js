var NT = {
    modules: null,

    GetModules: function () {
        if (this.modules == null) {
            this.modules = ajaxInit('/systeminfo/modules.json');
        }
        return this.modules;
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
