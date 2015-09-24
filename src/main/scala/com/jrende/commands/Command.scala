package com.jrende.commands

trait Command {
  //var params: Seq[String] = List()
  def execute(): Unit = {}
}

