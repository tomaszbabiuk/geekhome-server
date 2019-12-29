jQuery.metadata.setType("attr", "validate");
jQuery.ajaxSetup({
    cache: false
});

var PageTransfers = new Object();
function PageTransfer(page, args) {
    jQuery(page).trigger("pagetransfer", args);
    jQuery.mobile.changePage(page, { changeHash: true, reload: true });
    if (PageTransfers[page] != true) {
        PageTransfers[page] = true;
    }
    else {
        jQuery(page).trigger('create');
    }

    jQuery(page).trigger("pageaftertransfer", args);
}

function ajaxInit(jsonUrl, errorCallback) {
    var result = null;
    jQuery.ajax({
        type: 'GET',
        url: jsonUrl,
        dataType: 'json',
        success: function (data) { result = data; },
        error: function (xhr, ajaxOptions, thrownError) {
                if (xhr.status == 500) {
                    result = new Array();
                    if (errorCallback != null) {
                        errorCallback(xhr, ajaxOptions, thrownError);
                    } else {
                        GetLastServerError(function (data) {
                            if (data.Result != null) {
                                alert(data.Result);
                            } else {
                                alert("We're sorry. Unknown error happened!");
                            }
                        });
                    }
                }
        },
        async: false
    });

    return result;
}

function ajaxCallbackInit(callback, jsonUrl) {
    jQuery.ajax({
        type: 'GET',
        url: jsonUrl,
        dataType: 'json',
        success: callback,
        error: function (xhr, ajaxOptions, thrownError) {
            if (xhr.status == 500) {
                GetLastServerError(function (data) {
                    if (data.Result != null) {
                        alert(data.Result);
                    } else {
                        alert("We're sorry. Unknown error happened!");
                    }
                });
            }
        },
        async: true
    });
}

function GetPhotos(callback) {
    ajaxCallbackInit(callback, '/photos.json');
}

function GetLastServerError(callback) {
    if (callback == null) {
        return ajaxInit('/lastservererror.json');
    }

	ajaxCallbackInit(callback, '/lastservererror.json');
}

function AddLeadingZero(source) {
    if (source.toString().length == 1) {
        return '0' + source;
    }

    return source;
}

function FormatDateFromServer(jsondate) {
    return jsondate.Year + '-' + AddLeadingZero(jsondate.Month) + '-' + AddLeadingZero(jsondate.Day);
}

function FormatTimeFromServer(jsondate) {
    return AddLeadingZero(jsondate.Hour) + ':' + AddLeadingZero(jsondate.Minute);
}

function FindDescriptiveNameObject(id, list) {
    if (list != null) {
        for (var i in list) {
            dn = list[i];
            if (dn != null && dn.Name.UniqueId == id) {
                return dn;
            }
        }
    }

    return null;
}

function SortByPriority(array) {
    array.sort(function (a, b) { return Compare(parseFloat(b.Priority), parseFloat(a.Priority)); });
    return array;
}

function SortByDescriptiveName(array) {
    array.sort(function (a, b) { return Compare(b.Name.Name.toLowerCase(), a.Name.Name.toLowerCase()); });
    return array;
}

function SortByUniqueId(array) {
    array.sort(function (a, b) { return Compare(b.Name.UniqueId, a.Name.UniqueId); });
    return array;
}

function SortById(array) {
    array.sort(function (a, b) { return Compare(b.Id, a.Id); });
    return array;
}

function SortByVersionDesc(array) {
    array.sort(function (a, b) { return Compare(a.Version, b.Version); });
    return array;
}

function Compare(a, b) {
    if (a == b) {
        return 0;
    }
    else if (a < b) {
        return 1;
    }
    else return -1;
}

function IdsToNames(ids, findingFunction) {
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

        var namedObject = findingFunction(id);
        if (namedObject != null) {
            result += namedObject.Name.Name;
        }
    }

    return result;
}

function ExtractNonNegatedConditionId(id) {
    if (id.length > 0 && id[0] == '!') {
        return id.substr(1);
    }

    return id;
}

function DaysIdsToNames(ids) {
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

        var dayName = DaysNames[id];
        if (dayName != null) {
            result += dayName;
        }
    }

    return result;
}

function GetDependencies(id, callback) {
    ajaxCallbackInit(callback, '/dependencycheck.json?id=' + id);
}

function CheckModeEvaluation(id, callback) {
    ajaxCallbackInit(callback, '/checkmodeevaluation.json?id=' + id);
}

function CheckAlertEvaluation(id, callback) {
    ajaxCallbackInit(callback, '/checkalertevaluation.json?id=' + id);
}

function CheckDeviceEvaluation(id, callback) {
    var url = '/checkdeviceevaluation.json?id=' + id;
    if (callback == null) {
        return ajaxInit(url);
    }

    ajaxCallbackInit(callback, url);
}

function CheckBlockEvaluation(id, callback) {
    ajaxCallbackInit(callback, '/checkblockevaluation.json?id=' + id);
}

function CheckConditionEvaluation(id, callback) {
    ajaxCallbackInit(callback, '/checkconditionevaluation.json?id=' + id);
}

function ChangeDeviceState(id, value) {
    return ChangeDeviceState(id, value, '');
}

function ChangeDeviceState(id, value, code) {
    var result = null;
    jQuery.ajax({
        type: 'POST',
        url: '/changedevicestate.post',
		data: '?id=' + id + '&value=' + value + '&code=' + code,
        dataType: 'json',
        async: false,
        success: function(data) {
            result = data;
        }
    });

    return result.Result;
}

function SwitchToAutomatic(id) {
	jQuery.ajax({
        type: 'POST',
        url: '/switchtoautomatic.post',
		data: '?id=' + id,
        dataType: 'text',
        async: true
    });
}

function GetDeviceStates(id) {
    return ajaxInit('/getdevicestates.json?id=' + id).Result;
}

function GetBlockCategories(id) {
    return ajaxInit('/getblockcategories.json?id=' + id).Result;
}

function ChangeAutomationSetting(id, value) {
	jQuery.ajax({
        type: 'POST',
        url: '/changeautomationsetting.post',
		data: '?id=' + id + '&value=' + value,
        dataType: 'text',
        async: true
    });
}

function CheckAutomationSetting(id, callback) {
    if (callback == null) {
    	return ajaxInit('/checkautomationsetting.json?id=' + id);
    }
	ajaxCallbackInit(callback, '/checkautomationsetting.json?id=' + id);
}

function GetLastErrors(callback) {
    ajaxCallbackInit(callback, '/diagnostics/errors.json');
}

function GetLastActivities(callback) {
    ajaxCallbackInit(callback, '/diagnostics/activities.json');
}

function GetLastLoggedAlerts(callback) {
    ajaxCallbackInit(callback, '/diagnostics/alerts.json');
}

function ClearActivities() {
    ajaxInit('/diagnostics/clearactivities.json')
}

function GetDiagnosticInfo(callback) {
    	ajaxCallbackInit(callback, '/diagnosticinfo.json');
}

function GetConfigInfo(callback) {
    	ajaxCallbackInit(callback, '/configinfo.json');
}

function IsCodeValid(id, code) {
    return ajaxInit('/iscodevalid.json?id=' + id + '&code=' + code).Result;
}

function ToggleFavorite(id) {
	jQuery.ajax({
        type: 'POST',
        url: '/togglefavorite.post',
		data: '?id=' + id,
        dataType: 'text',
        async: true
    });
}

function IsFavorite(id) {
    return ajaxInit('/isfavorite.json?id=' + id).Result;
}

function GetFavoriteDevices(callback) {
    ajaxCallbackInit(callback, '/favoritedevices.json');
}

function GetDevicesGroupsByCategory(category) {
    return ajaxInit('/devicesgroupsbycategory.json?category=' + category);
}

function FormatInputValue(state) {
    return DigitalActivityNames[state];
}

function FormatDateToLocal(year, month, day, hour, minute, second, ms) {
    var date = new Date(year, month, day, hour, minute, second, ms);
    return date.toLocaleString() + "." + ms;
}

function FormatPortValue(value) {
    if (typeof value == "boolean") {
        return FormatInputValue(value);
    } else {
        return value;
    }
}