setup:
	@gradle wrapper
.PHONY: setup

build:
	@./gradlew build
.PHONY: build

clean:
	@./gradlew clean
.PHONY: clean

adb\:serve:
	@adb kill-server
	@adb start-server
.PHONY: adb\:serve

adb\:list:
	@adb devices -l
.PHONY: adb\:list

adb\:install:
	@adb -d install -r -t app/build/outputs/apk/debug/app-debug.apk
.PHONY: adb\:install

adb\:log:
	@adb -d logcat | grep "`adb shell ps | grep app.yasha.nowt | awk '{ print $$2 }'`"
.PHONY: adb\:log
