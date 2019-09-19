var CORE = {
	allConditions: null,
	allDevices: null,
	allMultistateDevices: null,
	allValueDevices: null,
	allNonReadonlyMultistateDevices: null,
	floors: null,
	groupConditions: null,
	modes: null,
	alerts: null,
	geofences: null,
	blocks: null,
	multistateConditions: null,
	valueConditions: null,
	deltaConditions: null,
	timeConditions: null,
	dateConditions: null,
	keySwitches: null,
    allBlocksTargetDevices: null,
    readValueCommands: null,
    changeStateCommands: null,
    geofenceConditions: null,

    /* CONDITIONS */
	GetAllConditions: function() {
		if (this.allConditions == null) {
			this.allConditions = ajaxInit('/config/allconditions.json').Result;
		}
		return this.allConditions;
	},

	FindCondition: function(id) {
		return FindDescriptiveNameObject(id, CORE.GetAllConditions());
	},


	/* DEVICES */
	GetAllDevices: function() {
		if (this.allDevices == null) {
			this.allDevices = ajaxInit('/config/alldevices.json').Result;
		}
		return SortByDescriptiveName(this.allDevices);
	},

    DevicesIdsToNames: function(devicesIds) {
        return IdsToNames(devicesIds, CORE.FindDevice);
    },

	GetAllMultistateDevices: function() {
		if (this.allMultistateDevices == null) {
			this.allMultistateDevices = ajaxInit('/config/allmultistatedevices.json').Result;
		}
		return this.allMultistateDevices;
	},

	GetAllValueDevices: function() {
		if (this.allValueDevices == null) {
			this.allValueDevices = ajaxInit('/config/allvaluedevices.json').Result;
		}
		return this.allValueDevices;
	},

	GetAllNonReadonlyMultistateDevices: function() {
	    var multistateDevices = CORE.GetAllMultistateDevices();
	    var nonReadonlyMultistateDevices = new Array();
	    for (var iMultistateDevice in multistateDevices) {
	        var device = multistateDevices[iMultistateDevice];
	        if (device.HasNonReadonlyStates) {
	            nonReadonlyMultistateDevices.push(device);
	        }
	    }
		return SortByDescriptiveName(nonReadonlyMultistateDevices);
	},

	GetAllBlocksTargetDevices: function() {
		if (this.allBlocksTargetDevices == null) {
			this.allBlocksTargetDevices = ajaxInit('/config/allblockstargetdevices.json').Result;
		}
		return this.allBlocksTargetDevices;
	},

	CountAllMulticontrollers: function() {
		var count = 0;
		for (var iDevice in this.GetAllDevices()) {
			var device = this.GetAllDevices()[iDevice];
			if (device.ControlType == 2) {
				count++;
				}
		}

		return count;
	},

	GetAllDevicesInRoom: function(id, callback) {
		ajaxCallbackInit(callback, '/config/alldevicesinroom.json?id=' + id);
	},

	GetAllOnOffDevicesInRoom: function(id, callback) {
		ajaxCallbackInit(callback, '/config/allonoffdevicesinroom.json?id=' + id);
	},

    FindDevice: function(id, callback) {
        var url = '/config/finddevice.json?id=' + id;
        if (callback == null) {
            return ajaxInit(url);
        }

        ajaxCallbackInit(callback, url);
    },

    CanAddDevices: function() {
        return ajaxInit('/config/canadddevices.json').Result;
    },

	StatesIdsToNames: function(deviceId, statesIds) {
        var states = GetDeviceStates(deviceId);
        var findingFunction = function(id) {
            return FindDescriptiveNameObject(id, states);
        }

	    return IdsToNames(statesIds, findingFunction);
	},

    /* FLOORS */
	GetFloors: function() {
		if (this.floors == null) {
			this.floors = SortByDescriptiveName(ajaxInit('/config/floors.json').Result);
		}
		return this.floors;
	},

	FindFloor: function(id) {
		return FindDescriptiveNameObject(id, CORE.GetFloors());
	},

	FindFloorByRoomId: function(id) {
		if (CORE.GetFloors() != null) {
			for (var i in CORE.GetFloors()) {
				floor = CORE.GetFloors()[i];
				var room = FindDescriptiveNameObject(id, floor.Rooms);
				if (room != null) {
					return floor;
				}
			}
		}

		return null;
	},

	FindRoom: function(id) {
		if (CORE.GetFloors() != null) {
			for (var i in CORE.GetFloors()) {
				floor = CORE.GetFloors()[i];
				var room = FindDescriptiveNameObject(id, floor.Rooms);
				if (room != null) {
					return room;
				}
			}
		}

		return null;
	},

	RoomsIdsToNames: function (roomsIds) {
		return IdsToNames(roomsIds, CORE.FindRoom);
	},


	/* GROUP CONDITIONS */
	GetGroupConditions: function() {
		if (this.groupConditions == null) {
			this.groupConditions = SortByDescriptiveName(ajaxInit('/config/groupConditions.json').Result);
		}
		return this.groupConditions;
	},

	FindGroupCondition: function (id) {
		return FindDescriptiveNameObject(id, CORE.GetGroupConditions());
	},

	CanAddGroupConditions: function() {
		return ajaxInit('/config/canaddgroupconditions.json').Result;
	},

    /* MODES */
	GetModes: function() {
		if (this.modes == null) {
			this.modes = SortByDescriptiveName(ajaxInit('/config/modes.json').Result);
		}
		return this.modes;
	},

	FindMode: function(id) {
		return FindDescriptiveNameObject(id, CORE.GetModes());
	},

    ModesIdsToNames: function(modesIds) {
        return IdsToNames(modesIds, this.FindMode);
    },


    /* ALERTS */
	GetAlerts: function() {
		if (this.alerts == null) {
			this.alerts = SortByDescriptiveName(ajaxInit('/config/alerts.json').Result);
		}
		return this.alerts;
	},

	FindAlert: function(id) {
		return FindDescriptiveNameObject(id, CORE.GetAlerts());
	},


    /* BLOCKS */
	GetBlocks: function() {
		if (this.blocks == null) {
			this.blocks = ajaxInit('/config/blocks.json').Result;
		}
		return this.blocks;
	},

	FindBlocksByTarget: function(id) {
		var result = [];
		for (var iBlock in CORE.GetBlocks()) {
			var block = CORE.GetBlocks()[iBlock];
			if (block.TargetId == id) {
				result.push(block);
			}
		}

		return result;
	},

	FindBlock: function(id) {
		return FindDescriptiveNameObject(id, CORE.GetBlocks());
	},


    /* TIME CONDITIONS */
	GetTimeConditions: function() {
		if (this.timeConditions == null) {
			this.timeConditions = SortByDescriptiveName(ajaxInit('/config/timeConditions.json').Result);
		}
		return this.timeConditions;
	},

	FindTimeCondition: function(id) {
		return FindDescriptiveNameObject(id, CORE.GetTimeConditions());
	},


    /* DATE CONDITIONS */
	GetDateConditions: function() {
		if (this.dateConditions == null) {
			this.dateConditions = SortByDescriptiveName(ajaxInit('/config/dateConditions.json').Result);
		}
		return this.dateConditions;
	},

	FindDateCondition: function(id) {
		return FindDescriptiveNameObject(id, CORE.GetDateConditions());
	},


    /* MULTISTATE CONDITIONS */
	GetMultistateConditions: function() {
		if (this.multistateConditions == null) {
			this.multistateConditions = SortByDescriptiveName(ajaxInit('/config/multistateConditions.json').Result);
		}
		return this.multistateConditions;
	},

	FindMultistateCondition: function(id) {
		return FindDescriptiveNameObject(id, CORE.GetMultistateConditions());
	},

    CanAddMultistateConditions: function() {
        return ajaxInit('/config/canaddmultistateconditions.json').Result;
    },


    /* VALUE CONDITIONS */
	GetValueConditions: function() {
		if (this.valueConditions == null) {
			this.valueConditions = SortByDescriptiveName(ajaxInit('/config/valueConditions.json').Result);
		}
		return this.valueConditions;
	},

	FindValueCondition: function(id) {
		return FindDescriptiveNameObject(id, CORE.GetValueConditions());
	},

	CanAddValueConditions: function() {
		return ajaxInit('/config/canaddvalueconditions.json').Result;
	},


    /* DELTA CONDITIONS */
	GetDeltaConditions: function() {
		if (this.deltaConditions == null) {
			this.deltaConditions = SortByDescriptiveName(ajaxInit('/config/deltaConditions.json').Result);
		}
		return this.deltaConditions;
	},

	FindDeltaCondition: function(id) {
		return FindDescriptiveNameObject(id, CORE.GetDeltaConditions());
	},

	CanAddDeltaConditions: function() {
		return ajaxInit('/config/canadddeltaconditions.json').Result;
	},


    /* KEY SWITCHES */
	GetKeySwitches: function() {
		if (this.keySwitches == null) {
			this.keySwitches = SortByDescriptiveName(ajaxInit('/config/keySwitches.json').Result);		
		}
		return this.keySwitches;
	},

	FindKeySwitch: function(id) {
		return FindDescriptiveNameObject(id, CORE.GetKeySwitches());
	},


    /* READ VALUE COMMANDS */
	GetReadValueCommands: function() {
		if (this.readValueCommands == null) {
			this.readValueCommands = SortByDescriptiveName(ajaxInit('/config/readvaluecommands.json').Result);		
		}
		return this.readValueCommands;
	},

	FindReadValueCommand: function(id) {
		return FindDescriptiveNameObject(id, CORE.GetReadValueCommands());
	},


    /* CHANGE STATE COMMANDS */
	GetChangeStateCommands: function() {
		if (this.changeStateCommands == null) {
			this.changeStateCommands = SortByDescriptiveName(ajaxInit('/config/changestatecommands.json').Result);
		}
		return this.changeStateCommands;
	},

	FindChangeStateCommand: function(id) {
		return FindDescriptiveNameObject(id, CORE.GetChangeStateCommands());
	},

    CanAddChangeStateCommands: function() {
        return ajaxInit('/config/canaddchangestatecommands.json').Result;
    },


    /* GEOFENCES */
    GetGeofences: function() {
        if (this.geofences == null) {
            this.geofences = SortByDescriptiveName(ajaxInit('/config/geofences.json').Result);
        }
        return this.geofences;
    },

    FindGeofence: function(id) {
        return FindDescriptiveNameObject(id, CORE.GetGeofences());
    },


    /* GEOFENCE CONDITIONS */
    GetGeofenceConditions: function() {
        if (this.geofenceConditions == null) {
            this.geofenceConditions = SortByDescriptiveName(ajaxInit('/config/geofenceConditions.json').Result);
        }
        return this.geofenceConditions;
    },

    FindGeofenceCondition: function(id) {
        return FindDescriptiveNameObject(id, CORE.GetGeofenceConditions());
    },

    GeofencesIdsToNames: function(geofencesIds) {
        return IdsToNames(geofencesIds, CORE.FindGeofence);
    },

	ConditionsIdsToNames: function(conditionsIds) {
		return IdsToNames(conditionsIds, CORE.FindCondition);
	},

    CanAddGeofenceConditions: function() {
        return ajaxInit('/config/canaddgeofenceconditions.json').Result;
    },
}
