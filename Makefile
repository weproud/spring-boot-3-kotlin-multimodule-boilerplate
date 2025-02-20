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

openapi3:
	./gradlew :app:app-api:openapi3

done: clean lint test

swagger: lint test openapi3
