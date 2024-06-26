plugins {
	id("com.android.application")
	id("org.jetbrains.kotlin.android")
	id("com.google.devtools.ksp")
	id("com.google.dagger.hilt.android")
	id("org.jetbrains.kotlin.plugin.serialization") version "1.9.20"
}

android {
	namespace = "com.husiev.dynassist"
	compileSdk = 34
	
	defaultConfig {
		applicationId = "com.husiev.dynassist"
		minSdk = 24
		targetSdk = 34
		versionCode = 34
		versionName = "0.7.5"
		
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
	kotlinOptions {
		jvmTarget = "17"
	}
	buildFeatures {
		compose = true
		buildConfig = true
	}
	sourceSets {
		// Adds exported schema location as test app assets.
		getByName("androidTest").assets.srcDir("$projectDir/schemas")
	}
	composeOptions {
		kotlinCompilerExtensionVersion = "1.5.5"
	}
	packaging {
		resources {
			excludes += "/META-INF/{AL2.0,LGPL2.1}"
		}
	}
}

dependencies {
	
	implementation("androidx.core:core-ktx:1.13.1")
	implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
	implementation("androidx.activity:activity-compose:1.9.0")
	implementation(platform("androidx.compose:compose-bom:2024.05.00"))
	implementation("androidx.compose.ui:ui:1.6.7")
	implementation("androidx.compose.ui:ui-graphics:1.6.7")
	implementation("androidx.compose.ui:ui-tooling-preview:1.6.7")
	implementation("androidx.compose.material3:material3-window-size-class:1.2.1")
	implementation("androidx.compose.material3:material3:1.2.1")
	implementation("androidx.compose.material:material:1.6.7")
	implementation("androidx.compose.material:material-icons-core:1.6.7")
	implementation("androidx.compose.material:material-icons-core-android:1.6.7")
	implementation("androidx.compose.material:material-icons-extended-android:1.6.7")
	implementation("androidx.compose.material:material-icons-extended:1.6.7")
	implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")
	implementation("androidx.datastore:datastore-preferences:1.1.1")
	implementation("androidx.navigation:navigation-compose:2.7.7")
	implementation("org.jetbrains.kotlin:kotlin-reflect:1.9.23")
	// Hilt
	implementation("com.google.dagger:hilt-android:2.51.1")
	implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
	implementation("androidx.hilt:hilt-work:1.2.0")
	implementation(platform("androidx.compose:compose-bom:2024.05.00"))
	androidTestImplementation(platform("androidx.compose:compose-bom:2024.05.00"))
	ksp ("com.google.dagger:hilt-compiler:2.51.1")
	ksp ("androidx.hilt:hilt-compiler:1.2.0")
	// Retrofit
	implementation("com.squareup.retrofit2:retrofit:2.11.0")
	implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0")
	implementation("com.squareup.okhttp3:okhttp:4.12.0")
	implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
	implementation("io.coil-kt:coil-compose:2.6.0")
	// Room
	implementation("androidx.room:room-runtime:2.6.1")
	implementation("androidx.room:room-ktx:2.6.1")
	annotationProcessor("androidx.room:room-compiler:2.6.1")
	ksp("androidx.room:room-compiler:2.6.1")
	// WorkManager dependency
	implementation("androidx.work:work-runtime-ktx:2.8.1")
	// Test
	testImplementation("junit:junit:4.13.2")
	androidTestImplementation("androidx.test.ext:junit:1.1.5")
	androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
	androidTestImplementation(platform("androidx.compose:compose-bom:2024.05.00"))
	androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.6.7")
	debugImplementation("androidx.compose.ui:ui-tooling:1.6.7")
	debugImplementation("androidx.compose.ui:ui-test-manifest:1.6.7")
}

class RoomSchemaArgProvider(
	@get:InputDirectory
	@get:PathSensitive(PathSensitivity.RELATIVE)
	val schemaDir: File
) : CommandLineArgumentProvider {
	
	override fun asArguments(): Iterable<String> {
		 return listOf("room.schemaLocation=${schemaDir.path}")
	}
}

ksp {
	arg(RoomSchemaArgProvider(File(projectDir, "schemas")))
}