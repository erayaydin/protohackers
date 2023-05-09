package in.yayd.era.protohackers.smoke_test

import akka.actor.{ActorRef, ActorSystem, Props}

import java.net.InetSocketAddress

object SmokeTestApp extends App {
  val system: ActorSystem = ActorSystem("SmokeTestApp")
  system.actorOf(
    Props(classOf[TcpServer], new InetSocketAddress("0.0.0.0", 9512))
  )
}
