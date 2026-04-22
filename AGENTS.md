# AGENTS.md

## Build And Verify
- Use JDK 21. CI installs Temurin 21, and both Android modules set `kotlin.jvmToolchain(21)`.
- Gradle projects are `:app` and `:nbrb-api`. `build-logic` is an included build, not a normal subproject.
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
- `:nbrb-api` owns the NBRB currency client/repository (`NBRBService`, `NBRBRepository`, serializers, cache files).
- Currency conversion spans both modules: `CurrencyConverter` lives in `:app`, but gets `NBRBRepository` injected from `:nbrb-api` through `ConverterViewModel`.
- History persistence is local Room state in `app/src/main/kotlin/dev/nevack/unitconverter/history/db/` and is provided through Hilt in `HistoryModule.kt`.

## Repo-Specific Gotchas
- Release signing is injected by the custom plugin `dev.nevack.plugins.signing-config` from root `signing.properties`. If that file is missing, the plugin no-ops; prefer debug builds for routine verification.
- `app/google-services.json` is materialized in CI from a secret and is gitignored locally. Do not add or commit it.
- Spotless coverage is split: root `spotlessCheck` formats `*.gradle.kts`, while `:app` and `:nbrb-api` format Kotlin source. `build-logic` Kotlin sources are outside those module targets.
- If you change `build-logic`, verify with a real Gradle invocation (for example `./gradlew help` plus the affected task), because it is compiled as an included build.
