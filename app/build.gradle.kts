import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
	alias(libs.plugins.android.application)
	alias(libs.plugins.kotlin.android)
	alias(libs.plugins.devtools.ksp)
	alias(libs.plugins.dagger.hilt)
	alias(libs.plugins.kotlin.serialization)
	alias(libs.plugins.compose.compiler)
	alias(libs.plugins.androidx.room)
}

android {
	namespace = "com.husiev.dynassist"
	compileSdk = 36
	
	defaultConfig {
		applicationId = "com.husiev.dynassist"
		minSdk = 24
		targetSdk = 36
		versionCode = 46
		versionName = "0.8.9"
		
		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
		vectorDrawables {
			useSupportLibrary = true
		}
	}
	
	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(
				getDefaultProguardFile("proguard-android-optimize.txt"),
				"proguard-rules.pro"
			)
		}
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_17
		targetCompatibility = JavaVersion.VERSION_17
	}
	buildFeatures {
		compose = true
		buildConfig = true
	}
	packaging {
		resources {
			excludes += "/META-INF/{AL2.0,LGPL2.1}"
		}
	}
}

kotlin {
	compilerOptions {
		jvmTarget.set(JvmTarget.JVM_17)
	}
}

room {
	schemaDirectory("$projectDir/schemas")
}

dependencies {
	
	implementation(libs.androidx.core.ktx)
	implementation(libs.androidx.lifecycle.runtime.ktx)
	implementation(libs.androidx.activity.compose)
	implementation(platform(libs.androidx.compose.bom))
	implementation(libs.androidx.ui)
	implementation(libs.androidx.ui.graphics)
	implementation(libs.androidx.ui.tooling.preview)
	implementation(libs.androidx.material3.window.size.clazz)
	implementation(libs.androidx.material3)
	implementation(libs.androidx.material)
	implementation(libs.androidx.material.icons.core)
	implementation(libs.androidx.material.icons.core.android)
	implementation(libs.androidx.material.icons.extended.android)
	implementation(libs.androidx.material.icons.extended)
	implementation(libs.androidx.lifecycle.runtime.compose)
	implementation(libs.androidx.datastore.preferences)
	implementation(libs.androidx.navigation.compose)
	implementation(libs.kotlin.reflect)
	// Hilt
	implementation(libs.hilt.android)
	implementation(libs.androidx.hilt.navigation.compose)
	implementation(libs.androidx.hilt.work)
	implementation(platform(libs.androidx.compose.bom))
	ksp (libs.hilt.compiler)
	ksp (libs.androidx.hilt.compiler)
	// Retrofit
	implementation(libs.retrofit)
	implementation(libs.retrofit2.kotlinx.serialization.converter)
	implementation(libs.okhttp)
	implementation(libs.kotlinx.serialization.json)
	implementation(libs.coil.compose)
	// Room
	implementation(libs.androidx.room.runtime)
	implementation(libs.androidx.room.ktx)
	annotationProcessor(libs.androidx.room.compiler)
	ksp(libs.androidx.room.compiler)
	// WorkManager dependency
	implementation(libs.androidx.work.runtime.ktx)
	// Test
	testImplementation(libs.junit)
	androidTestImplementation(libs.androidx.junit)
	androidTestImplementation(libs.androidx.espresso.core)
	androidTestImplementation(platform(libs.androidx.compose.bom))
	androidTestImplementation(libs.androidx.ui.test.junit4)
	debugImplementation(libs.androidx.ui.tooling)
	debugImplementation(libs.androidx.ui.test.manifest)
}