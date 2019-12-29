var B = {
	backups: null,

	/* BACKUPS */
	GetBackups: function(errorCallback) {
		if (this.backups == null) {
			this.backups = ajaxInit('/config/backups.json', errorCallback).Result;
		}
		return this.backups;
	},

    FindBackup: function(id) {
        return FindDescriptiveNameObject(id, B.GetBackups());
    },
}
