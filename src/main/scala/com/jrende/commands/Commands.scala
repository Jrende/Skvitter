package com.jrende.commands

import com.jrende.commands.Show


object Commands {

  val commands = List(
    Discover,
    Exit,
    Help,
    Next,
    Open,
    Prev,
    Show,
    Tweet
  )

  def getCommand(input: String): Option[Command] = {
    val cmd = input.split(" ")
    val result = commands.filter(_.toString().toLowerCase.startsWith(cmd.head))
    result.length match {
      case 0 => None
      case 1 => Some(result.head(cmd.tail))
      case _ => Some(AmbiguousCmd(result.map(_(cmd.tail))))
    }
  }
}












