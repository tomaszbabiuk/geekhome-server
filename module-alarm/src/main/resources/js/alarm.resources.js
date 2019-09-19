var PresenceTypeNames = new Array();
PresenceTypeNames[0] = "<!-- @RES:ALARM:PresenceType0.Presence -->";
PresenceTypeNames[1] = "<!-- @RES:ALARM:PresenceType1.Absence -->";

var AlarmTypeNames = new Array();
AlarmTypeNames[0] = "<!-- @RES:ALARM:AlarmType0.Burglary -->";
AlarmTypeNames[1] = "<!-- @RES:ALARM:AlarmType1.Silent -->";
AlarmTypeNames[2] = "<!-- @RES:ALARM:AlarmType2.Fire -->";
AlarmTypeNames[3] = "<!-- @RES:ALARM:AlarmType3.Flood -->";
AlarmTypeNames[4] = "<!-- @RES:ALARM:AlarmType4.Medical -->";
AlarmTypeNames[5] = "<!-- @RES:ALARM:AlarmType5.Prealarm -->";
AlarmTypeNames[6] = "<!-- @RES:ALARM:AlarmType6.Sabotage -->";
AlarmTypeNames[7] = "<!-- @RES:ALARM:AlarmType7.SecurityCall -->";
AlarmTypeNames[100] = "<!-- @RES:ALARM:AlarmType100.Other -->";

function AlarmTypesToNames(ids) {
    var result = '';
    var splits = ids.split(',');
    for (var idIndex in splits) {
        var id = splits[idIndex];

        if (result.length > 0) {
            result += ', ';
        }

        if (id.length > 0 && id[0] == '!') {
            result += '!';
            id = id.substr(1);
        }

        result += AlarmTypeNames[id];
    }

    return result;
}
