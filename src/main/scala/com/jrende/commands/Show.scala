package com.jrende.commands

import com.jrende.model.TweetManager
import com.jrende.view.Renderer
import com.jrende.Colors._
import twitter4j.Status;
import scala.util.matching.Regex

case class Show(params : Seq[String]) extends Command {
  val digit = """\d+""".r
  val username = """@.*""".r
  override def execute() = {
    if (params.isEmpty) {
      Renderer.renderTweets(TweetManager.getHomeTimeline())
    } else {
      if (digit.findFirstIn(params.head).nonEmpty) {
        val tweet = TweetManager.getTweetById(params.head.toInt)
        tweet match {
          case Some(tweet: Status) => Renderer.renderTweet(tweet)
          case None => Console.println(("No tweet with id " + params.head).Red())
        }
      }
    }
  }
}
