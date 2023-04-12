package lib

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import di.CoreModule
import lib.field.FieldInterface
import org.http4s.*
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.headers.Accept
import org.http4s.implicits.uri

class CoreRestClient(using persistence: FileIOInterface) extends ControllerInterface[Player]:
  private val coreUrl = "http://0.0.0.0:8080"
  var hexField: FieldInterface[Player] = persistence.decode(exportField)

  override def gameStatus: GameStatus = GameStatus.valueOf(
    request(Method.GET, "/gameStatus")
  )

  private def request(method: Method, path: String) = httpClient(Request[IO](
    method = method,
    uri = Uri.fromString(s"$coreUrl$path").getOrElse(Uri()),
    headers = Headers(
      Accept(MediaType.application.json),
    )
  )).unsafeRunSync()

  private def httpClient(request: Request[IO]): IO[String] = EmberClientBuilder.default[IO].build.use(_.expect[String](request))

  override def fillAll(c: Player): Unit = hexField = persistence.decode(request(Method.POST, "/fillAll/$c"))

  override def save(): Unit = hexField = persistence.decode(request(Method.POST, "/save"))

  override def load(): Unit = hexField = persistence.decode(request(Method.POST, "/load"))

  override def place(c: Player, x: Int, y: Int): Unit = hexField = persistence.decode(request(Method.POST, "/place/$c/$x/$y"))

  override def undo(): Unit = hexField = persistence.decode(request(Method.POST, "/undo"))

  override def redo(): Unit = hexField = persistence.decode(request(Method.POST, "/redo"))

  override def reset(): Unit = hexField = persistence.decode(request(Method.POST, "/reset"))

  override def exportField: String = request(Method.GET, "/exportField")
