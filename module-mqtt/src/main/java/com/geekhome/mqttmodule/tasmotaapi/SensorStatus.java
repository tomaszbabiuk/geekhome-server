package com.geekhome.mqttmodule.tasmotaapi;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class SensorStatus {
    public class StatusExtractionException extends Exception {

    }

    public class TempAndHum {

        @SerializedName("Temperature")
        private double _temperature;

        @SerializedName("Humidity")
        private double _humidity;

        public double getTemperature() {
            return _temperature;
        }

        public double getHumidity() {
            return _humidity;
        }
    }

    public class Temp {

        @SerializedName("Temperature")
        private double _temperature;

        public double getTemperature() {
            return _temperature;
        }
    }

    @SerializedName("DHT11")
    private TempAndHum _dht11;

    @SerializedName("AM2301")
    private TempAndHum _am2301;

    @SerializedName("HTU21")
    private TempAndHum _htu21;

    @SerializedName("DS18B20")
    private Temp _ds18B20;

    @SerializedName("SHT3X")
    private TempAndHum _sht3X;

    @SerializedName("Temperature")
    private double _temperature;

    @SerializedName("Humidity")
    private double _humidity;

    @SerializedName("TempUnit")
    private TemperatureUnit _temperatureUnit;

    @SerializedName("Noise")
    private double _noise;

    @SerializedName("AirQuality")
    private double _airQuality;

    @SerializedName("Light")
    private double _light;

    public TempAndHum getDht11() {
        return _dht11;
    }

    public TempAndHum getAm2301() {
        return _am2301;
    }

    public TempAndHum getHtu21() {
        return _htu21;
    }

    public TempAndHum getSht3X() {
        return _sht3X;
    }

    public Temp getDs18B20() {
        return _ds18B20;
    }

    public double getTemperature() {
        return _temperature;
    }

    public double getHumidity() {
        return _humidity;
    }

    public TemperatureUnit getTemperatureUnit() {
        return _temperatureUnit;
    }

    public boolean hasTemperatureData() {
        return _temperatureUnit != TemperatureUnit.Unknown;
    }

    public double getNoise() {
        return _noise;
    }

    public double getAirQuality() {
        return _airQuality;
    }

    public double getLight() {
        return _light;
    }

    public double extractTemperature() throws StatusExtractionException {
        if (getAm2301() != null) {
            return getAm2301().getTemperature();
        }

        if (getDht11() != null) {
            return getDht11().getTemperature();
        }

        if (getDs18B20() != null) {
            return getDs18B20().getTemperature();
        }

        if (getHtu21() != null) {
            return getHtu21().getTemperature();
        }

        if (getSht3X() != null) {
            return getSht3X().getTemperature();
        }

        if (isSonoffSC()) {
            return getTemperature();
        }

        throw new StatusExtractionException();
    }

    public double extractHumidity() throws StatusExtractionException {
        if (getAm2301() != null) {
            return getAm2301().getHumidity();
        }

        if (getDht11() != null) {
            return getDht11().getHumidity();
        }

        if (getHtu21() != null) {
            return getHtu21().getHumidity();
        }

        if (getSht3X() != null) {
            return getSht3X().getHumidity();
        }

        if (isSonoffSC()) {
            return getHumidity();
        }

        throw new StatusExtractionException();
    }

    private boolean isSonoffSC() {
        return getNoise() != 0.0 &&
                getLight() != 0.0 &&
                getAirQuality() != 0.0 &&
                getTemperature() != 0.0 &&
                getHumidity() != 0.0;
    }


    /*
    {
       "StatusSNS":{
          "Time":"2018.02.01 21:29:40",
          "TempUnit":"C",
          "ENERGY":{
             "Total":3.185,
             "Yesterday":3.058,
             "Today":0.127,
             "Power":0,
             "Factor":0.00,
             "Voltage":221,
             "Current":0.000
          },
          "DHT11":{
             "Temperature":12.0,
             "Humidity":42.0
          },
          "AM2301":{
             "Temperature":15.5,
             "Humidity":50.6
          },
          "BMP280":{
             "Temperature":80.9,
             "Pressure":984.4
          },
          "DS18B20":{
             "Temperature":19.7
          },
          "SHT3X":{
             "Temperature":74.8,
             "Humidity":18.9
          },
          "Temperature":25,
          "Light":10,
          "Noise":20,
          "AirQuality":100,
          "HTU21":{
             "Temperature":24.7,
             "Humidity":32.1
          },
          "PMS5003":{
             "CF1":1,
             "CF2.5":2,
             "CF10":2,
             "PM1":1,
             "PM2.5":2,
             "PM10":2,
             "PB0.3":423,
             "PB0.5":116,
             "PB1":17,
             "PB2.5":1,
             "PB5":0,
             "PB10":0
          }
       }
    }
  */
}
