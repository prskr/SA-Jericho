import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class RandomJokeSimulation extends Simulation {
    val httpConf = http
        .baseURL("http://xpl-baez:8080")
        .acceptHeader("application/json")
        .acceptEncodingHeader("gzip, deflate")
        .userAgentHeader("Mozilla/5.0 (Windows NT 5.1; rv:31.0) Gecko/20100101 Firefox/31.0")

    val scn = scenario("RandomJokeSimulation").repeat(100) {
        exec(http("getRandomJoke")
        .get("/api/v1/joke/random"))
    }    

    setUp(
        scn.inject(atOnceUsers(100))
    ).protocols(httpConf)
}