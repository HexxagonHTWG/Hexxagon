package aview.gui

import controller.GameStatus.{GAME_OVER, IDLE, TURN_PLAYER_1, TURN_PLAYER_2}
import controller.controllerComponent.ControllerInterface
import model.Player
import model.fieldComponent.FieldInterface
import scalafx.Includes.*
import scalafx.application.JFXApp3
import scalafx.geometry.Insets
import scalafx.geometry.Pos.*
import scalafx.scene.control.{Button, Label}
import scalafx.scene.image.Image
import scalafx.scene.layout.*
import scalafx.scene.paint.Color.*
import scalafx.scene.paint.{LinearGradient, Stops}
import scalafx.scene.shape.Polygon
import scalafx.scene.text.{Font, Text}
import scalafx.scene.{ImageCursor, Scene}
import scalafx.stage.Stage
import util.Observer

import scala.math.sqrt


class GUI(using controller: ControllerInterface[Player]) extends JFXApp3 with Observer:
  controller.add(this)
  controller.save()
  val size = 40
  private val font = "Hexa"
  private val fontsize = size * 1.5
  private val load = Font.loadFont(getClass.getResource("/Hexa.ttf").toExternalForm, 20)
  //private val load2 = Font.loadFont(getClass.getResource("/Hexag.ttf").toExternalForm, 20)

  override def update(): Unit =
    start()

  override def start(): Unit =
    stage = new JFXApp3.PrimaryStage {
      icons += new Image(getClass.getResource("/logo.png").toExternalForm, 100, 100, true, true)
      resizable = false
      title.value = "HEXXAGON"
      scene = new Scene((controller.hexField.matrix.col + 2) * size * 2, 800) {
        this.setCursor(new ImageCursor(new Image(getClass.getResource("/cursor.png").toExternalForm, 100, 100, true, true)))
        val border = new BorderPane()
        border.setBackground(new Background(Array(new BackgroundFill(new LinearGradient(endX = 0, stops = Stops(LightGrey, LightSteelBlue)), CornerRadii(size), Insets(10)))))
        val pane: Pane = Pane()
        var tmp: Hex = Hex(0, 0, 0, Player.Empty)
        for (j <- 0 until controller.hexField.matrix.row) {
          controller.hexField.matrix.matrix(j).zipWithIndex.foreach {
            (x, i) =>
              if i == 0 then
                tmp = Hex(size, 0, 0, x)
                tmp.relocate(i * size * 2, j * size * 1.8)
                setMouse(tmp, i, j)
                pane.children += tmp
              else i % 2 match {
                case 0 =>
                  tmp = Hex(size, 0, 0, x)
                  tmp.relocate(i * size * 2 - (size / 2.2) * i, j * size * 1.8)
                  setMouse(tmp, i, j)
                  pane.children += tmp
                case _ =>
                  tmp = Hex(size, 0, 0, x)
                  tmp.relocate(i * size * 2 - (size / 2.2) * i, j * size * 1.8 + size / 1.1)
                  setMouse(tmp, i, j)
                  pane.children += tmp
              }
          }
        }
        border.padding = Insets(50)
        border.center = pane
        border.top = {
          val l1 = new Label(controller.gameStatus.message())
          l1.style = s"-fx-font: $fontsize $font; -fx-text-fill: linear-gradient(darkblue, red);"
          l1.padding = Insets(0, 0, size, size * (controller.hexField.matrix.col / 2 - 1))
          l1
        }
        border.bottom = new HBox {
          padding = Insets(size, 0, size / 2, size * 0.75)
          if controller.gameStatus == GAME_OVER then
            val O = controller.hexField.matrix.oCount
            val X = controller.hexField.matrix.xCount
            val winner = if O < X then "PLAYER 1 WON" else if O == X then "DRAW" else "PLAYER 2 WON"
            val over = new Label(winner)
            over.textAlignment = scalafx.scene.text.TextAlignment.Center
            if winner == "PLAYER 1 WON" then
              over.style = s"-fx-font: $fontsize $font; -fx-text-fill: linear-gradient(darkblue, blue);"
            else
              over.style = s"-fx-font: $fontsize $font; -fx-text-fill: linear-gradient(black, red);"
            this.padding = Insets(size, 0, size, size * (controller.hexField.matrix.col / 2 - 1))
            this.children += over
          else
            val xcount = new Label("X: " + controller.hexField.matrix.xCount)
            val ocount = new Label("O: " + controller.hexField.matrix.oCount)
            xcount.textAlignment = scalafx.scene.text.TextAlignment.Center
            ocount.textAlignment = scalafx.scene.text.TextAlignment.Center
            xcount.style = s"-fx-font: $fontsize $font; -fx-text-fill: linear-gradient(darkblue, blue);"
            ocount.style = s"-fx-font: $fontsize $font; -fx-text-fill: linear-gradient(black, red);"
            this.children += xcount
            this.children += ocount
            this.setSpacing(size * controller.hexField.matrix.col - size)
        }

        border.right = {
          val css =
            s"""
                    #dark-blue {
                        -fx-background-color:
                            #090a0c,
                            linear-gradient(#38424b 0%, #1f2429 20%, #191d22 100%),
                            linear-gradient(#20262b, #191d22),
                            radial-gradient(center 50% 0%, radius 100%, rgba(114,131,148,0.9), rgba(255,255,255,0));
                        -fx-background-radius: 5,4,3,5;
                        -fx-background-insets: 0,1,2,0;
                        -fx-text-fill: black;
                        -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1 );
                        -fx-font-family: $font;
                        -fx-text-fill: linear-gradient(red, blue);
                        -fx-font-size: 20px;
                        -fx-padding: 10 20 10 20;
                    }
                    #dark-blue Text {
                        -fx-effect: dropshadow( one-pass-box , rgba(0,0,0,0.9) , 1, 0.0 , 0 , 1 );
                    }
                    """
          val b1 = new Button("UNDO")
          b1.setOnMouseClicked(x => controller.undo())
          b1.style = css
          val b2 = new Button("REDO")
          b2.setOnMouseClicked(x => controller.redo())
          b2.style = css
          val b3 = new Button("FILL")
          b3.setOnMouseClicked(x => controller.fillAll(Player.X))
          b3.style = css
          val b4 = new Button("RESET")
          b4.setOnMouseClicked(x => controller.reset())
          b4.style = css
          val b5 = new Button("SAVE")
          b5.setOnMouseClicked(x => controller.save())
          b5.style = css
          val b6 = new Button("LOAD")
          b6.setOnMouseClicked(x => controller.load())
          b6.style = css
          val vb = new VBox(b1, b2, b3, b4, b5, b6)
          vb.spacing = size
          vb
        }

        root = border
      }

    }

  private def setMouse(p: Hex, i: Int, j: Int): Unit =
    p.setOnMouseClicked(_ => {
      controller.gameStatus match {
        case TURN_PLAYER_1 => controller.place(Player.X, i, j)
        case TURN_PLAYER_2 => controller.place(Player.O, i, j)
        case IDLE => controller.place(Player.O, i, j)
        case GAME_OVER => println("GAME OVER")
      }
    })

class Hex(size: Double, x: Double, y: Double, var t: Player) extends StackPane:
  private val cons = sqrt(3) / 2
  private val pol: Polygon = Polygon()
  pol.points ++= Seq(
    x, y,
    x + size, y,
    x + size * (3.0 / 2.0), y + size * cons,
    x + size, y + size * sqrt(3),
    x, y + size * sqrt(3),
    x - (size / 2.0), y + size * cons)
  pol.fill = White
  pol.setStroke(Black)

  var text: Text = Text(x, y, t.toString)
  text.setTextAlignment(scalafx.scene.text.TextAlignment.Center)
  text.style = s"-fx-font: $size arial"
  text.fill = t match {
    case Player.X => new LinearGradient(endX = 0, stops = Stops(Black, DarkBlue))
    case Player.O => new LinearGradient(endX = 0, stops = Stops(Black, DarkRed))
    case _ => White
  }

  this.getChildren.addAll(pol, text)
