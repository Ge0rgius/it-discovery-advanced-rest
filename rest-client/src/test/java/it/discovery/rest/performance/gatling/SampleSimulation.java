package it.discovery.rest.performance.gatling;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.core.CoreDsl.atOnceUsers;
import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.http.HttpDsl.http;

public class SampleSimulation extends Simulation {

    HttpProtocolBuilder httpProtocol = http
            .baseUrl("http://localhost:8080");

    ScenarioBuilder scn = scenario("SampleSimulation")
            .exec(http("request_1")
                    .get("/api/books"))
            .pause(1);

    {
        setUp(
                scn.injectOpen(atOnceUsers(1000))
        ).protocols(httpProtocol);
    }

}
