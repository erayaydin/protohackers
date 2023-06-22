package in.yayd.era.protohackers.prime_time

import java.net.InetSocketAddress
import akka.actor.Actor
import akka.actor.ActorLogging
import akka.io.IO
import akka.io.Tcp
import akka.io.Tcp._
import akka.actor.Props

private class TcpServer(remote: InetSocketAddress)
    extends Actor
    with ActorLogging {

  import context.system

  IO(Tcp) ! Tcp.Bind(self, remote)

  override def receive: Receive = {
    case Bound(localAddress) =>
      log.info(s"Server started on $localAddress")
    case Connected(remote, local) =>
      log.info(s"New connection. Remote: $remote, Local: $local")
      val handler = context.actorOf(Props[PrimeHandler])
      sender() ! Register(handler, keepOpenOnPeerClosed = true)
    case CommandFailed(_: Bind) => context stop self
  }

} 
