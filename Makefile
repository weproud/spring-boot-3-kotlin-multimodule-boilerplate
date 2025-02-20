lint-check:
	./gradlew ktlintCheck

lint:
	./gradlew ktlintFormat

deps:
	./gradlew :app:app-api:dependencies

clean:
	./gradlew clean

test:
	./gradlew test

done: clean lint test
