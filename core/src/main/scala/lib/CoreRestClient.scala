package lib

import cats.effect.IO
import lib.field.FieldInterface
import org.http4s.ember.client.EmberClientBuilder

// TODO
abstract class CoreRestClient[T] extends ControllerInterface[T]:
  val coreUrl = "http://0.0.0.0:8080"
  val httpClient = EmberClientBuilder.default[IO].build
  var hexField: FieldInterface[T]

  override def gameStatus: GameStatus = GameStatus.valueOf(httpClient.use(_.expect[String](s"$coreUrl/gameStatus")).toString)

  override def fillAll(c: T): Unit = ???

  override def save(): Unit = ???

  override def load(): Unit = ???

  override def place(c: T, x: Int, y: Int): Unit = ???

  override def undo(): Unit = ???

  override def redo(): Unit = ???

  override def reset(): Unit = ???

  override def exportField: String = ???
