package lib

import di.CoreModule
import lib.field.FieldInterface
import lib.json.HexJson

case class CoreRestClient() extends ControllerInterface[Player]:
  private val coreUrl = "http://0.0.0.0:8080"
  var hexField: FieldInterface[Player] = HexJson.decode(exportField)

  override def gameStatus: GameStatus = GameStatus.valueOf(requests.get(s"$coreUrl/status").text())

  override def fillAll(c: Player): Unit =
    hexField = HexJson.decode(requests.post(s"$coreUrl/fillAll/$c").text())
    notifyObservers()

  override def save(): Unit =
    hexField = HexJson.decode(requests.post(s"$coreUrl/save").text())
    notifyObservers()

  override def load(): Unit =
    hexField = HexJson.decode(requests.post(s"$coreUrl/load").text())
    notifyObservers()

  override def place(c: Player, x: Int, y: Int): Unit =
    hexField = HexJson.decode(requests.post(s"$coreUrl/place/$c/$x/$y").text())
    notifyObservers()

  override def undo(): Unit =
    hexField = HexJson.decode(requests.post(s"$coreUrl/undo").text())
    notifyObservers()

  override def redo(): Unit =
    hexField = HexJson.decode(requests.post(s"$coreUrl/redo").text())
    notifyObservers()

  override def reset(): Unit =
    hexField = HexJson.decode(requests.post(s"$coreUrl/reset").text())
    notifyObservers()

  override def exportField: String = requests.get(s"$coreUrl/exportField").text()
