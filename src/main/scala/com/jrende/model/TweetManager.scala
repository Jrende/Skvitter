package com.jrende.model

import java.io.{FileNotFoundException, FileOutputStream, ObjectOutputStream, IOException, FileInputStream, ObjectInputStream, File}
import java.time.Instant

import twitter4j.{Paging, Twitter, Status}
import com.jrende.Authenticator
import com.jrende.Colors._
import scala.collection.JavaConversions._

object TweetManager {
  var debug = true
  var localIdToTweet = scala.collection.mutable.Map[Long, Status]()
  var tweetIdToLocalId = scala.collection.mutable.Map[Long, Long]()
  var last_id: Long = 0

  var currentPage = 1
  val tweetsPerPage = 10

  val loadTime = 60 * 1000

  //Int: Page number
  val loadedPages = scala.collection.mutable.Map[Int, (Seq[Status], Instant)]()

  def loadAndCacheHomeTimeline(page: Int, count: Int): Seq[Status] = {
    Console.println("Fetch page " + page)
    val twitter = Authenticator.authenticate
    val timeline = twitter.getHomeTimeline(new Paging(page, count))
    loadedPages.put(page, (timeline, java.time.Instant.now()))
    timeline
  }

  def getHomeTimeline(page: Int, count: Int = tweetsPerPage): Seq[Status] = {
    val now = java.time.Instant.now()
    val statusList = loadedPages.get(page)
      .find((item: (Seq[Status], Instant)) => {
      val (_, timeLoaded) = item
      (now.toEpochMilli - timeLoaded.toEpochMilli) < loadTime
    })
      .map(_._1)
      .getOrElse(loadAndCacheHomeTimeline(page, count))

    addIdToTweets(statusList)
    statusList
  }

  def addIdToTweets(statusList: Seq[Status]) = {
    statusList.foreach((status) => {
      localIdToTweet.putIfAbsent(last_id, status)
      tweetIdToLocalId.putIfAbsent(status.getId, last_id)
      last_id += 1
    })
  }

  def getHomeTimeline(): Seq[Status] = getHomeTimeline(1)

  def getNextPage(): Seq[Status] = {
    currentPage += 1
    getHomeTimeline(currentPage)
  }

  def getPrevPage(): Seq[Status] = {
    currentPage = Math.max(currentPage - 1, 1)
    getHomeTimeline(currentPage)
  }

  def getTweetById(id: Long): Option[Status] = {
    localIdToTweet.get(id)
  }

  private def getDebugTweets(): Seq[Status] = {
    try {
      val file = new File("debugTweets")
      if (!file.exists()) file.createNewFile()
      Console.println("Get existing debug tweets")
      val fileInputStream = new FileInputStream(file)
      val in = new ObjectInputStream(fileInputStream)
      in.readObject().asInstanceOf[List[Status]]
    } catch {
      case e: IOException => Console.println("Failed to read file".Red()); List.empty
    }
  }

  private def loadDebugTweets(twitter: Twitter): Unit = {
    try {
      val file = new File("debugTweets")
      val list = twitter.getHomeTimeline.toList
      val oos = new ObjectOutputStream(new FileOutputStream(file))
      oos.writeObject(list)
      oos.close()
    } catch {
      case e: FileNotFoundException => Console.println("Can't find debugTweets".Red()); List.empty
      case e: IOException => Console.println("Failed to read file".Red()); List.empty
    }
  }
}
