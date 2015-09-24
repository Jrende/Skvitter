package com.jrende.commands

import com.jrende.model.TweetManager
import twitter4j.Status
import scala.collection.JavaConversions
import com.jrende.Colors._

case class Open(params: Seq[String]) extends Command() {
  override def execute(): Unit = {

    if (params.isEmpty) {
      return
    }
    val id = params.head.toLong
    val tweet = TweetManager.getTweetById(id)

    val mediaUrls: Seq[String] = tweet
      .map(_.getMediaEntities)
      .getOrElse(Array.empty)
      .toList
      .map(_.getMediaURL)
    val urlEntities: Seq[String] = tweet
      .map(_.getURLEntities)
      .getOrElse(Array.empty)
      .toList
      .map(_.getExpandedURL)
    val urls = mediaUrls ++ urlEntities
    urls.length match {
      case 0 => Console.println("No urls in tweet " + params.head)
      case 1 => Runtime.getRuntime.exec("xdg-open " + urls.head)
      case _ =>
        val sb = new StringBuilder()
        sb.append("Multiple urls in tweet. Enter number you want to open, or anything else to cancel\n")
        for (i <- urls.indices) {
          sb.append(i.toString.Bold())
          sb.append(" - ")
          sb.append(urls(i).Underlined())
          sb.append('\n')
        }
        sb.append(urls.length.toString.Bold())
        sb.append(" - ")
        sb.append("All of them!".Underlined())
        sb.append('\n')
        Console.println(sb.toString())

        val num = io.StdIn.readLine()
        if (num.forall(_.isDigit) && num.toInt <= urls.length) {
          if (num.toInt == urls.length) {
            urls.foreach((url: String) => {
              Runtime.getRuntime.exec("xdg-open " + url)
            })
          } else {
            Runtime.getRuntime.exec("xdg-open " + urls(num.toInt))
          }
        } else {
          Console.println("No url with index of " + num)
        }
    }

  }
}
