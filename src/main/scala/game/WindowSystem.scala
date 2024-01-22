package game

import java.awt
import java.awt.{Dimension, KeyEventDispatcher, KeyboardFocusManager}
import java.awt.event.ActionListener
import javax.swing.Timer
import scala.swing.*
import scala.swing.event.Key
import game.SimulationControl

import java.awt.event.KeyEvent
import scala.swing.event.KeyPressed
import scala.collection.mutable.Buffer

/**
 * Window system and the main entry point to the program.
 */
object WindowSystem extends SimpleSwingApplication:

  //1000, 500
  //1500, 900
  val settings = WindowSettings(1500, 900, 1, "Simulation")
  val top: MainFrame = MainWindow(settings, this)

/**
 * The window that the connected simulation control is displayed with.
 *
 * @param windowSettings
 *  The settings of the window
 */
class MainWindow(windowSettings: WindowSettings, val application: SimpleSwingApplication) extends MainFrame:
  //Systems
  val simulation: SimulationControl = SimulationControl(windowSettings, this)

  //Input
  val pressedKeys: Buffer[event.Key.Value] = Buffer.empty

  //Window
  title = windowSettings.name

  contents = new Panel:
    preferredSize = Dimension(windowSettings.width, windowSettings.height)

    //Keys
    listenTo(this.keys)

    reactions += {
      //Handle all keys
      case KeyPressed(_, key, _, _) =>
        pressedKeys += key
    }

    focusable = true
    requestFocus()

    //Rendering
    override def paintComponent(g: Graphics2D): Unit = g.drawImage(simulation.render(), 0, 0, null)

  //Timer
  val time = new Timer(0 , new ActionListener() {
    override def actionPerformed(e: awt.event.ActionEvent): Unit =
      repaint()
  })
  time.start()


class WindowSettings(val width: Int, val height: Int, val pixelScale: Int, val name: String)


