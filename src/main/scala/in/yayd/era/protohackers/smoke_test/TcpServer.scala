package in.yayd.era.protohackers.smoke_test

import akka.actor.{Actor, ActorLogging, Props}
import akka.io.{IO, Tcp}

import java.net.InetSocketAddress

private class TcpServer(remote: InetSocketAddress)
    extends Actor
    with ActorLogging {
  import akka.io.Tcp._
  import context.system

  IO(Tcp) ! Bind(self, remote)

  override def receive: Receive = {
    case Bound(localAddress) =>
      log.info(s"Server started on $localAddress")
    case Connected(remote, local) =>
      log.info(s"New connection. Remote: $remote, Local: $local.")
      val handler = context.actorOf(Props[EchoHandler])
      sender() ! Register(handler, keepOpenOnPeerClosed = true)
    case CommandFailed(_: Bind) ⇒ context stop self
  }
}
