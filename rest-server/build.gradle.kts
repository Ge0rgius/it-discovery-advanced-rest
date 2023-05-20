dependencies {
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.modelmapper:modelmapper:3.1.1")
    implementation("com.jayway.jsonpath:json-path:2.8.0")
    
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.rest-assured:rest-assured")
    testImplementation("io.rest-assured:spring-mock-mvc")
    testImplementation("io.rest-assured:json-path")
}

tasks.test {
    useJUnitPlatform()
}