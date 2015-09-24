package com.jrende

import java.io.{IOException, FileNotFoundException, ObjectInputStream, FileOutputStream, ObjectOutputStream, FileInputStream, File}

import Colors._
import com.jrende.commands.{Exit, Commands}
import twitter4j.{ResponseList, Twitter, Status}
import scala.collection.JavaConversions._

object ApplicationMain extends App {


  override def main(args: Array[String]) = {
    var exitRequested = false
    val read = new Thread(new Runnable {
      override def run(): Unit = {
        while (!exitRequested) {
          val command = Commands.getCommand(io.StdIn.readLine())
          command match {
            case Some(Exit(_)) => exitRequested = true
            case Some(cmd) => cmd.execute()
            case None => Console.println("Unknown command!".Red())
          }
        }
      }
    })
    read.start()
  }

}