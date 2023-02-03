# Nowt

`/naʊt/`

An immersive RSS reader for Android.

You can do **nowt** (nought) except reading an item/entry at a time.


## Requirements

* Android 10.0 System Image (Q API 30)
* OpenJDK 17
* Gradle 7.6

### Required permissions

* INTERNET
* ACCESS_NETWORK_STATE


## Build

```zsh
% make build
```


## LICENSE

`GPL-3.0-or-later`

See [LICENSE](./LICENSE).

```txt
Nowt
Copyright (C) 2023 Yasuhiro Яша Asaka

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.
```


## Notes

### Development

#### Java and Gradle

```zsh
% eselect java-vm list
Available Java Virtual Machines:
  [1]   openjdk-17  system-vm
  [2]   openjdk-bin-8
  [3]   openjdk-bin-11
  [4]   openjdk-bin-17
```

```zsh
% java --version
openjdk 17.0.6 2023-01-17
OpenJDK Runtime Environment 17.0.6_p10 (build 17.0.6+10)
OpenJDK 64-Bit Server VM 17.0.6_p10 (build 17.0.6+10, mixed mode, sharing)
```

```zsh
% gradle --version

------------------------------------------------------------
Gradle 7.6
------------------------------------------------------------

Build time:   2022-11-25 13:35:10 UTC
Revision:     daece9dbc5b79370cc8e4fd6fe4b2cd400e150a8

Kotlin:       1.7.10
Groovy:       3.0.13
Ant:          Apache Ant(TM) version 1.10.11 compiled on July 10 2021
JVM:          17.0.6 (Gentoo 17.0.6+10)
OS:           Linux 5.15.82-gentoo amd64
```

#### Setup Emulator

* Setup QEMU/KVM
  * Configure CPU virtualization by enabling AMD-V (svm) or Vt-x (vmx) in UEFI
    firmware or BIOS
    * Check support of the system's firhmware with
      `grep -E "svm|vmx" /proc/cpuinfo`
    * Check /dev/kvm is listed
  * VM acceleration
    * See [Configure VM acceleration](
https://developer.android.com/studio/run/emulator-acceleration#accel-vm)
    * e.g. on Gentoo Linux
      * app-emulation/qemu
      * libvirt
      * net-misc/bridge-utils
* Create a virtual device
* Install [Android SDK Command-line Tools](
https://developer.android.com/studio/command-line/)
  * Configure `ANDROID_HOME` (e.g. as `$HOME/.android` or `/opt/...` etc.)
* Run ./bin/run-emulator script
* See the doc: [Start the emulator from the command line](
https://developer.android.com/studio/run/emulator-commandline)
* (optional) Check/Edit hardware acceleration settings
  * Run with `run-emulator xxx -gpu swiftshader_indirect` etc.
  * To disable via config.ini
    * Find config.ini for AVDs
    * Disable hardware GPU use as followings

```zsh
% find ~/.android -name "config.ini"
/path/to/$ANDROID_HOME/avd/XXX.avd/config.ini
````

config.ini:

```ini
# default
# hw.gpu.enabled = yes
# hw.gpu.mode = auto
hw.gpu.enabled = no
hw.gpu.mode = off
```

#### Setup udev rules for USB debugging

e.g. for my Cat S52 ;)

Add an user in `plugdev` group.

```zsh
% sudo usermod -aG plugdev <user>
```

Create an udev rule.

```zsh
# Check IDs
% lsusb | grep S52
Bus 007 Device 011: ID 04b7:88e0 Compal Electronics, Inc. S52
```

```zsh
% cat /etc/udev/rules.d/51-android.rules
# Cat S52
SUBSYSTEMS=="usb", ATTRS{idVendor}=="04b7", ATTRS{idProduct}=="88e0", \
  MODE="0600", GROUP="plugdev", SYMLINK+="android%n"
% sudo udevadm control --reload-rules
```

After the device is connected, I can see it.

```zsh
% ls -la /dev/android3
lrwxrwxrwx 1 root root 15 Jan 30 02:18 /dev/android3 -> bus/usb/007/011
```

In order to make it available on USB debugging via adb, go `Settings` ->
`Developer options` -> `Default USB configuration`, then set it as
`File transfer` mode.

Then,

```zsh
% adb start-server
...
% adb devices -l
List of devices attached
S522003011998          device usb:7-6.3 product:CatS52 model:S52 device:CatS52 transport_id:1
```

