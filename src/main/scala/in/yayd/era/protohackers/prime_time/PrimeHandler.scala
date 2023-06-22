package in.yayd.era.protohackers.prime_time

import akka.actor.ActorLogging
import akka.actor.Actor
import akka.io.Tcp._
import io.circe._
import io.circe.generic.semiauto._
import io.circe.syntax._
import io.circe.parser._
import cats.syntax.functor._
import akka.util.ByteString

sealed trait Number
case class IntNumber(v: Int) extends Number
case class FloatNumber(v: Float) extends Number

case class Request(method: String, number: Number)
case class Result(method: String, prime: Boolean)

class PrimeHandler extends Actor with ActorLogging {

  """
  implicit val decodeNumber: Decoder[Number] = {
    Decoder.decodeInt.validate(_.value.isNumber, "ExpectedNumber").map(IntNumber).widen[Number] or
      Decoder.decodeFloat.validate(_.value.isNumber, "ExpectedNumber").map(FloatNumber).widen[Number]
  }
  """

  implicit val decodeRequest: Decoder[Request] = deriveDecoder

  override def receive: Receive = {
    case Received(data) =>
      log.info(s"Got data from ${sender().toString()}. Data type: ${data.getClass} Data size: ${data.length}")
      println(data.map(b => b.toChar).mkString)

      data.utf8String.split('\n').foreach(
        req =>
          decode[Request](req) match {
            case Right(Request("isPrime", IntNumber(number))) =>
              log.info(s"isPrime int number $number")
              val result = Result("isPrime", isPrime(number))
              log.info(s"sending $result")
              log.info(s"sending ${result.asJson.noSpaces}")
              sender() ! Write(ByteString(result.asJson.noSpaces + "\n"))
            case Right(Request("isPrime", FloatNumber(number))) =>
              log.info(s"isPrime float number $number")
              val result = Result("isPrime", false)
              sender() ! Write(ByteString(result.asJson.noSpaces))
            case Right(_) | Left(_) =>
              sender() ! Write(ByteString("malformed"))
              context stop self
          }
      )
    case PeerClosed =>
      log.info("Connection has been closed")
      context stop self
  }

  private def isPrime(n: Int): Boolean = {
    if (n == 2) return true
    if (n <= 1 || n % 2 == 0) return false
    for (e <- 3.until(n, 2)) if (n % e == 0) return false
    true
  }

}
