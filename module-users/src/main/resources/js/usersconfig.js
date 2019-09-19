var U = {
	users: null,

	GetUsers: function() {
		if (this.users == null) {
			this.users = ajaxInit('/config/users.json').Result;
		}
		return this.users;
	},

	FindUser: function(id) {
		return FindDescriptiveNameObject(id, U.GetUsers());
	},
}
