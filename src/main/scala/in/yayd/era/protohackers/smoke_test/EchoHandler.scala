package in.yayd.era.protohackers.smoke_test

import akka.actor.{Actor, ActorLogging}

class EchoHandler extends Actor with ActorLogging {
  import akka.io.Tcp._

  def receive: Receive = {
    case Received(data) =>
      log.info(s"Got data. Data type: ${data.getClass}")
      sender() ! Write(data)
    case PeerClosed =>
      log.info("Connection has been closed")
      context stop self
  }
}
