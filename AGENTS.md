# AGENTS.md

## Build And Verify
- Use JDK 21. CI installs Temurin 21, and all modules set `kotlin.jvmToolchain(21)`.
- Gradle projects are `:app`, `:core:converter`, `:feature:converter`, `:feature:history-api`, `:feature:history`, and `:nbrb-api`. `build-logic` is an included build, not a normal subproject.
- Fast local verification for app changes: `./gradlew :app:assembleDebug`
- Format before finishing: `./gradlew spotlessApply`
- CI currently runs these checks: `./gradlew --stacktrace :app:assembleDebug`, `./gradlew spotlessCheck`, `./gradlew --no-parallel dependencyUpdates`
- `dependencyUpdates` is a custom root task from `build-logic`; keep `--no-parallel` to match CI.
- There are no checked-in tests under `app/src/test`, `app/src/androidTest`, or `nbrb-api/src/test` right now. Do not assume an existing test suite covers changes.

## Architecture
- `:app` is a ViewBinding/XML Android app, not Compose.
- Navigation is manual AppCompat activity/fragment wiring; there is no Navigation Component graph.
- Launcher entrypoint is `app/src/main/kotlin/dev/nevack/unitconverter/categories/CategoriesActivity.kt`.
- `CategoriesActivity` handles the main category list. On tablets it also hosts `ConverterFragment`; on phones it launches `ConverterActivity`.
- `:feature:converter` owns the converter UI, catalog wiring, and Android-facing converter resources.
- `:core:converter` owns the pure converter domain types and implementations shared by `:feature:converter` and `:nbrb-api`.
- `:nbrb-api` owns the NBRB currency client/repository (`NBRBService`, `NBRBRepository`, serializers, cache files).
- `:feature:history` owns the history UI and Room-backed persistence.
- `:feature:history-api` owns the shared history contracts used across features.

## Repo-Specific Gotchas
- Release signing is injected by the custom plugin `dev.nevack.plugins.signing-config` from root `signing.properties`. If that file is missing, the plugin no-ops; prefer debug builds for routine verification.
- `app/google-services.json` is materialized in CI from a secret and is gitignored locally. Do not add or commit it.
- Spotless coverage is split: root `spotlessCheck` formats `*.gradle.kts`, while `:app` and `:nbrb-api` format Kotlin source. `build-logic` Kotlin sources are outside those module targets.
- If you change `build-logic`, verify with a real Gradle invocation (for example `./gradlew help` plus the affected task), because it is compiled as an included build.
