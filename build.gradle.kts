plugins { 
  java
  eclipse
  id("org.springframework.boot") version "3.1.0" apply false
  id("io.gatling.gradle") version "3.9.5" apply false
}

allprojects {
   group = "it.discovery"
}

subprojects {  
   apply(plugin = "java")

   java.sourceCompatibility = JavaVersion.VERSION_20
   java.targetCompatibility = JavaVersion.VERSION_20

   repositories {
     jcenter()
   }

   var springBootVersion = "3.1.0"
   
   dependencies {
        implementation(platform("org.springframework.boot:spring-boot-dependencies:$springBootVersion"))
        implementation("org.springframework.boot:spring-boot-starter-web")
        implementation("org.apache.commons:commons-lang3")

        runtimeOnly("jakarta.annotation:jakarta.annotation-api")
        compileOnly("org.projectlombok:lombok")
        annotationProcessor("org.projectlombok:lombok:1.18.26")
   } 
}

