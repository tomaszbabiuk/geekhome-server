var ALARM = {
	alarmsensors: null,
	magneticdetectors: null,
	movementdetectors: null,
	alarmzones: null,
	codelocks: null,
	signalizators: null,
	presenceconditions: null,
	allalarmsensors: null,
	alllocks: null,
    gates: null,

	/* ALL ALARM SENSORS */
	GetAllAlarmSensors: function() {
		if (this.allalarmsensors == null) {
			this.allalarmsensors = SortByDescriptiveName(ajaxInit('/config/allalarmsensors.json').Result);
		}
		return this.allalarmsensors;
	},

	FindInAllAlarmSensors: function(id) {
		return FindDescriptiveNameObject(id, ALARM.GetAllAlarmSensors());
	},

	AlarmSensorsIdsToNames: function(alarmSensorsIds) {
		return IdsToNames(alarmSensorsIds, ALARM.FindInAllAlarmSensors);
	},

	/* ALL LOCKS */
	GetAllLocks: function() {
		if (this.alllocks == null) {
			this.alllocks = SortByDescriptiveName(ajaxInit('/config/alllocks.json').Result);
		}
		return this.alllocks;
	},

    IsLockInUse: function(checkedLockId, zoneId) {
        var lookIsInUse = false;
        for (var iZone in ALARM.GetAlarmZones()) {
            var zone = ALARM.GetAlarmZones()[iZone];
            if (zone.Name.UniqueId != zoneId) {
            var locksIds = zone.LocksIds.split(',');
                for (var iZoneLockId in locksIds) {
                    var zoneLockId = locksIds[iZoneLockId];
                    if (zoneLockId == checkedLockId) {
                        lookIsInUse = true;
                        break;
                    }
                }
            }
        }

        return lookIsInUse;
    },

	FindLock: function(id) {
		return FindDescriptiveNameObject(id, ALARM.GetAllLocks());
	},
	
	LocksIdsToNames: function(locksIds) {
		return IdsToNames(locksIds, ALARM.FindLock);
	},


	/* PRESENCE CONDITIONS */
	GetPresenceConditions: function() {
		if (this.presenceconditions == null) {
			this.presenceconditions = SortByDescriptiveName(ajaxInit('/config/presenceconditions.json').Result);
		}
		return this.presenceconditions;
	},

	FindPresenceCondition: function(id) {
		return FindDescriptiveNameObject(id, ALARM.GetPresenceConditions());
	},

	CanAddPresenceConditions: function() {
		return ajaxInit('/config/canaddpresenceconditions.json').Result;
	},

	/* ALARM SENSORS */
	GetAlarmSensors: function() {
		if (this.alarmsensors == null) {
			this.alarmsensors = SortByDescriptiveName(ajaxInit('/config/alarmsensors.json').Result);
		}
		return this.alarmsensors;
	},

	FindAlarmSensor: function(id) {
		return FindDescriptiveNameObject(id, ALARM.GetAlarmSensors());
	},

	/* CODE LOCKS */
	GetCodeLocks: function() {
		if (this.codelocks == null) {
			this.codelocks = SortByDescriptiveName(ajaxInit('/config/codelocks.json').Result);
		}
		return this.codelocks;
	},

	FindCodeLock: function(id) {
		return FindDescriptiveNameObject(id, ALARM.GetCodeLocks());
	},

	/* GATES */
	GetGates: function() {
		if (this.gates == null) {
			this.gates = SortByDescriptiveName(ajaxInit('/config/gates.json').Result);
		}
		return this.gates;
	},

	FindGate: function(id) {
		return FindDescriptiveNameObject(id, ALARM.GetGates());
	},


	/* ALARM ZONES */
	GetAlarmZones: function() {
		if (this.alarmzones == null) {
			this.alarmzones = SortByDescriptiveName(ajaxInit('/config/alarmzones.json').Result);
		}
		return this.alarmzones;
	},

	FindAlarmZone: function(id) {
		return FindDescriptiveNameObject(id, ALARM.GetAlarmZones());
	},

	CanAddAlarmZones: function() {
		return ajaxInit('/config/canaddalarmzones.json').Result;
	},

	/* MOVEMENT DETECTORS DEVICES */
	GetMovementDetectors: function() {
		if (this.movementdetectors == null) {
			this.movementdetectors = SortByDescriptiveName(ajaxInit('/config/movementdetectors.json').Result);
		}
		return this.movementdetectors;
	},

	FindMovementDetector: function(id) {
		return FindDescriptiveNameObject(id, ALARM.GetMovementDetectors());
	},

	MovementDetectorsIdsToNames: function(movementDetectorsIds) {
		return IdsToNames(movementDetectorsIds, ALARM.FindMovementDetector);
	},


	/* MAGNETIC DETECTORS */
	GetMagneticDetectors: function() {
		if (this.magneticdetectors == null) {
			this.magneticdetectors = SortByDescriptiveName(ajaxInit('/config/magneticdetectors.json').Result);
		}
		return this.magneticdetectors;
	},

	FindMagneticDetector: function(id) {
		return FindDescriptiveNameObject(id, ALARM.GetMagneticDetectors());
	},


	/* SIGNALIZATORS */
	GetSignalizators: function() {
		if (this.signalizators == null) {
			this.signalizators = SortByDescriptiveName(ajaxInit('/config/signalizators.json').Result);
		}
		return this.signalizators;
	},

	FindSignalizator: function(id) {
		return FindDescriptiveNameObject(id, ALARM.GetSignalizators());
	},
}
