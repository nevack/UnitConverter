import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.kotlin.dsl.configure

pluginManager.withPlugin("com.android.application") {
    extensions.configure<ApplicationExtension>("android") {
        buildFeatures {
            viewBinding = true
        }
    }
}

pluginManager.withPlugin("com.android.library") {
    extensions.configure<LibraryExtension>("android") {
        buildFeatures {
            viewBinding = true
        }
    }
}
