# Nowt

`/naʊt/`

You can do nowt (nought).


## Requirements

* Android 10.0 System Image (Q API 30)
* OpenJDK 17
* Gradle 7.6


## Build

```zsh
% make build
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
