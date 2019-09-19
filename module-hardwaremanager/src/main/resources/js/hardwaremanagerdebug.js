var HMD = {
    ReadInputPort: function(id) {
        return ajaxInit('/readdigitalinputport.json?id=' + id).Result;
    },

    ReadOutputPort: function(id) {
        return ajaxInit('/readdigitaloutputport.json?id=' + id).Result;
    },

    ReadTemperaturePort: function(id) {
        return ajaxInit('/readtemperatureport.json?id=' + id).Result;
    },

    ReadHumidityPort: function(id) {
        return ajaxInit('/readhumidityport.json?id=' + id).Result;
    },

    ReadLuminosityPort: function(id) {
        return ajaxInit('/readluminosityport.json?id=' + id).Result;
    },

    ChangeInputPort: function(id, value) {
        jQuery.ajax({
            type: 'POST',
            url: '/changeinputport.post',
            data: '?id=' + id + '&value=' + value,
            dataType: 'text',
            async: true
        });
    },

    ChangeTemperaturePort: function(id, value) {
        jQuery.ajax({
            type: 'POST',
            url: '/changetemperatureport.post',
            data: '?id=' + id + '&value=' + value,
            dataType: 'text',
            async: true
        });
    },

    ChangeHumidityPort: function(id, value) {
        jQuery.ajax({
            type: 'POST',
            url: '/changehumidityport.post',
            data: '?id=' + id + '&value=' + value,
            dataType: 'text',
            async: true
        });
    },

    ChangeLuminosityPort: function(id, value) {
        jQuery.ajax({
            type: 'POST',
            url: '/changeluminosityport.post',
            data: '?id=' + id + '&value=' + value,
            dataType: 'text',
            async: true
        });
    }
}

