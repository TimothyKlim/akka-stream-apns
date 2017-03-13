package reactivehub.akka.stream.apns.examples

import akka.actor._
import akka.stream.scaladsl._
import akka.stream.{ActorMaterializer, OverflowStrategy}
import io.netty.channel.nio.NioEventLoopGroup
import reactivehub.akka.stream.apns.Environment.Production
import reactivehub.akka.stream.apns.marshallers.CirceSupport
import reactivehub.akka.stream.apns.TlsUtil.loadPkcs12FromResource
import reactivehub.akka.stream.apns._

object Main extends App with CirceSupport {
  implicit val system = ActorSystem("system")
  implicit val _ = ActorMaterializer()

  import system.dispatcher

  val deviceToken = DeviceToken(
    "3103fb2fb90f92914948406f1ad3b6f16b46754817816afa01a429801960e1f4")

  val payload = Payload.Builder().withAlert("Hello!").withBadge(1)

  val group = new NioEventLoopGroup()
  val apns = ApnsExt(system).connection[UUID](
    Production,
    loadPkcs12FromResource("/cert.p12", "pass"),
    group)

  val source = Source.single(1 → Notification(deviceToken, payload))
  source
    .via(apns)
    .runForeach(println)
    .onComplete { _ ⇒
      group.shutdownGracefully()
      system.terminate()
    }
}
