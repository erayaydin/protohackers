package in.yayd.era.protohackers.prime_time

import akka.actor.ActorSystem
import akka.actor.Props
import java.net.InetSocketAddress

object PrimeTimeApp extends App {
  val system: ActorSystem = ActorSystem("SmokeTestApp")
  system.actorOf(
    Props(classOf[TcpServer], new InetSocketAddress("0.0.0.0", 9512))
  )
}
