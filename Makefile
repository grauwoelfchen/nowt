setup:
	@gradle wrapper
.PHONY: setup

build:
	@./gradlew build
.PHONY: build

clean:
	@./gradlew clean
.PHONY: clean
