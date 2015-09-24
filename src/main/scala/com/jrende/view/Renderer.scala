package com.jrende.view

import com.jrende.model.TweetManager
import twitter4j.Status
import com.jrende.Colors._

object Renderer {

  def buildTweetString(tweet: Status, sb: StringBuilder) = {
    var user = tweet.getUser
    var text = tweet.getText
    if(tweet.isRetweet) {
      sb.append(("Retweeted by " + user.getName).LightGray().Dim() + '\n')
      user = tweet.getRetweetedStatus.getUser
      text = tweet.getRetweetedStatus.getText
    }
    sb.append(user.getName.Green().Bold())
    sb.append(" ")
    sb.append(("@" + user.getScreenName).LightGreen())
    sb.append(" (id: " + TweetManager.tweetIdToLocalId.getOrElse(tweet.getId, -1) + ")")
    sb.append('\n')
    text.split("\n").foreach((msg: String) => {
      sb.append(indent(2))
      sb.append(msg.LightBlue())
      sb.append('\n')
    })
    sb.append(indent(2))
    sb.append("RT: ")
    sb.append(tweet.getRetweetCount)
    sb.append(indent(4))
    sb.append("Fav: ")
    sb.append(tweet.getFavoriteCount)
    sb.append("\n")
  }

  def renderTweet(tweet: Status) = {
    val sb = new StringBuilder()
    buildTweetString(tweet, sb)
    Console.println(sb.toString())
    sb.append("\n")
  }

  def renderTweets(tweets: Seq[Status]): Unit = {
    val sb = new StringBuilder()
    tweets.reverse.foreach((tweet) => {
      buildTweetString(tweet, sb)
      sb.append("\n")
    })
    Console.println(sb.toString())
  }
  private def indent(num: Int): String = {
    (0 to num - 1).foldLeft("")((a, b) => a + " ")
  }
}
