setup:
	@gradle wrapper
.PHONY: setup

check:
	@./gradlew --console=verbose check --exclude-task test
.PHONY: check

test:
	@./gradlew --console=verbose test --exclude-task testReleaseUnitTest
.PHONY: test

# https://developer.android.com/studio/build/building-cmdline
build\:debug:
	@./gradlew --console=verbose assembleDebug --exclude-task check --exclude-task test
.PHONY: build\:debug

build\:release:
	@./gradlew --console=verbose assembleRelease
.PHONY: build\:release

build: build\:debug
.PHONY: build

clean:
	@./gradlew cleanTest clean
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
