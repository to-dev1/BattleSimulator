package spawner

import control.TurretControlSystem
import game.*
import mathematics.*
import rendering.*
import spawner.*
import terrain.*

import java.io.{File, FileReader, BufferedReader, FileNotFoundException, IOException}
import java.awt.Color
import scala.collection.mutable.Buffer

/**
 * Class for loading text from files.
 */
class FileLoader():

  def readFolder(path: String): Buffer[(String, String)] =
    println("Read folder: " + path)
    val output: Buffer[(String, String)] = Buffer.empty
    val folder = File(path)
    val files = folder.listFiles()
    for(file <- files) do
      if(file.isFile) then
        val name = file.getName
        output += ((name, readFile(path + name)))
      else if(file.isDirectory) then
        output.addAll(readFolder(path + file.getName + "/"))
    output


  def readFile(path: String): String =
    println("Read file: " + path)
    var output = ""
    try
      val reader = FileReader(path)
      val buffered = BufferedReader(reader)
      var reading = true
      try
        while reading do
          val text = buffered.readLine()
          if(text != null) then output += text
          else reading = false
      finally
        reader.close()
        buffered.close()
    catch
      case error: FileNotFoundException => println("The file " + path + " does not exist")
      case error: IOException => println("I/O exception when reading the file " + path)
    output



