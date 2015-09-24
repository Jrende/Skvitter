package com.jrende.commands

import scala.util.matching.Regex
import com.jrende.Colors._

case class AmbiguousCmd(matches : Seq[Command]) extends Command() {
  val parens = new Regex("\\(.*\\)")
  override def execute() = {
    Console.println("Ambiguous command. Did you mean:")
    val cmdNames: String = matches
      .map((cmd) => parens.replaceAllIn(cmd.toString, ""))
      .foldLeft("")(_ + " " + _)
    Console.println(cmdNames.LightGreen())
  }
}
