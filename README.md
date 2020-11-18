Welcome to geekhome-server
==========================

GeekHOME server is a complete home automation solution. The system can control lights, central heating, alarm, ventilation or any device at your house.

GeekHOME is an alternative to other automation systems like OpenHAB, Domoticz or HomeAssistant.

The system consists of a web page that's hosted on Raspberry Pi in the local network. The devices that controls the house must be connected to Raspberry Pi via USB adapters (like 1-wire to USB, RS485 to USB, etc.). 

Short history
=============
GeekHOME was created in 2009. In the beginning the system was running on .NET Microframework boards.
The code was written in C#. To render pages, a new framework called microASP was created (the remaining of this framework are still used in the project).
There was no OpenHAB or Raspberry pi back than.

On 2010 the first prototype came to a light. It was running on FEZ Cobra board from GHI electronics. Cobra was 10 times more expensive than the first Raspberry Pi and probably 100 times slower than the Pi.
The most important part was a "blocks based" automation system that proved itself to control my house for months without a break. I still keep that Cobra in my drawer to remember the "hard and expensive times".
The system consisted of FEZ Cobra board, connected to two 12-channel relay boards. I was using a bare IO lines on FEZ Cobra to read the signals from alarm sensors and DS18B20 sensor for temperature readings.
Everything was connected in the "star" topology. It was a massive amount of wires - totally not-maintainable.

On 2012 I run into a small company in Poland producing a complete set of automation boards based on 1-wire interface. It was a change to get rid of "star" topology and use a bus to reduce wiring. I decided to base my next prototype on 1-wire and 1-wire is still supported. It's the most reliable, elegant and still a cheap solution if you want to control your house using wires.
In the meantime Raspberry Pi came up and was able to run a full set of Java. Microsoft stopped supporting .NET Micro Framework and I it was no sense to continue project on dying micro controllers which were few times more expensive than the Pi.

On 2013 the system was rewritten to Java. The only things left was a microASP framework and already proved in the battle "block automation" core.

The years 2014-2016 was filled with testing and stabilization. I was working mostly on Android application that was able to connect to geekHOME server via Parse cloud.
The OpenHAB and similar solutions was becoming more and more popular. People started to notice the market of DIY Home Automation products. The IoT started to be "a thing".
I wanted to share my product with community on 2016 but that wasn't the best time for my family. I had to take care of them and "freeze" the project for better times.

To be continued...