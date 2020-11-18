Welcome to geekhome-server
==========================

GeekHOME server is a complete home automation solution. The system can control lights, central heating, alarm, ventilation or any device at your house.

GeekHOME is an alternative to other automation systems like OpenHAB, Domoticz or HomeAssistant.

The system consists of a web page that's hosted on Raspberry Pi in the local network. The devices that controls the house must be connected to Raspberry Pi via USB adapters (like 1-wire to USB, RS485 to USB, etc.). 

Installation on Raspberry Pi
============================
GeekHOME Can run everywhere when java can be installed. The recommended board is Raspberry Pi Zero/Zero W.
The setup below assumes you have a fresh installation of Raspbian/Ubuntu and you can connect to the board with SSH:
```bash
ssh pi@x.x.x.x
```
Where x.x.x.x is IP number of your Pi.

# Installing java
```bash
sudo apt update
sudo apt install openjdk-8-jdk
java -version
```

### Java troubleshooting.
If you have problem running java version like:
```
Error occurred during initialization of VM
Server VM is only supported on ARMv7+ VFP
```
run
```bash 
sudo update-alternatives --config java
```
and manually select java 8

# Installing geekHOME
```bash
cd /home/pi
mkdir geekhome
cd geekhome
wget https://github.com/tomaszbabiuk/geekhome-server/releases/download/v1.0.0/geekhome-server-1.0.0.tar.xz
tar xf geekhome-server-1.0.0.tar.xz
rm -rf geekhome-server-1.0.0.tar.xz
sudo ./install_on_raspberry_pi.sh
sudo reboot
```

# Optional installation steps
## Installing RXTX drivers 
Required only if you're using serial to 1-wire adapter or serial to RS485 adapter.
```bash
sudo apt-get install librxtx-java
sudo reboot
```

## Enabling PiFace
Required only if you're using PiFace attached to your Raspberry Pi.
```bash
curl -s get.pi4j.com | sudo bash
```

The long history of geekHOME
============================
GeekHOME was created in 2009. The code was written in C# because the system was designed to run .NET Microframework boards.
To render pages, a new framework called microASP was created (and is still used in the project).
There was no OpenHAB or Raspberry Pi back than.

On 2010 the first prototype running on FEZ Cobra board from GHI electronics has been developed. The Cobra was 100 times slower than the first version of Raspberry Pi (and it was also 10x more expensive).
To control the building the Cobra was connected to two 16-channel relay boards. To read temperature and input signals from alarm sensor the bare IO lines was used.
It was working... but the solution had one big problem: everything was connected in the "star" topology. The amount of cables was unmaintainable.

On 2012 the "start topology" problem was fixed. I run into a small company in Poland producing a complete set of automation boards based on 1-wire. It was a game changer! Using a bus reduced the wiring and made installation simpler. 
The prototype two is using 1-wire boards only. It's the most reliable, elegant and still a cheap solution for a wired smarthome.

On 2013 Microsoft gave up on .NETMF and Raspberry Pi came up. The geekHOME has been totally rewritten to java. 
The only things left was from previous solution was a microASP framework and "block automation" core that proved itself in the battle.

The years 2014-2016 was filled with testing and stabilization. Most of the work related to Android application for controlling the house outside local network.
In the meantime, "OpenHAB" and similar solutions become popular. People started to notice the market of DIY Home Automation.
