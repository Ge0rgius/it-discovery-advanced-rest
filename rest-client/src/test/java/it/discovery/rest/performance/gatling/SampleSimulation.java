package it.discovery.rest.performance.gatling;

import static io.gatling.javaapi.core.CoreDsl.atOnceUsers;
import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.http.HttpDsl.http;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

public class SampleSimulation extends Simulation {

    HttpProtocolBuilder httpProtocol = http 
            .baseUrl("https://dog.ceo/api/breeds/list/all");

    ScenarioBuilder scn = scenario("SampleSimulation")
            .exec(http("request_1")
                    .get("/"))
            .pause(5);

    {
        setUp(
                scn.injectOpen(atOnceUsers(1))
        ).protocols(httpProtocol);
    }

}
