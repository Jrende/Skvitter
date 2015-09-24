package com.jrende.commands

import com.jrende.model.TweetManager
import com.jrende.view.Renderer

case class Prev(params : Seq[String]) extends Command() {
  override def execute(): Unit = {
    Renderer.renderTweets(TweetManager.getPrevPage())
  }
}
