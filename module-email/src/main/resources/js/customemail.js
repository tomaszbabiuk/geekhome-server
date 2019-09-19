var E = {
	trustedRecipients: null,

	/* TRUSTED RECIPIENTS */
	GetTrustedRecipients: function() {
		if (this.trustedRecipients == null) {
			this.trustedRecipients = SortByDescriptiveName(ajaxInit('/config/trustedrecipients.json').Result);
		}
		return this.trustedRecipients;
	},

    FindTrustedRecipient: function(id) {
        return FindDescriptiveNameObject(id, E.GetTrustedRecipients());
    },

}
