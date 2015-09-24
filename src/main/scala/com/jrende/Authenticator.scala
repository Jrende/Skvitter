package com.jrende

import java.awt.Desktop
import java.io.{InputStreamReader, BufferedReader, IOException, OutputStream, InputStream, File, FileInputStream, FileOutputStream}
import java.net.{URI, URISyntaxException}
import java.util.Properties

import twitter4j.auth.{AccessToken, RequestToken}
import twitter4j.conf.ConfigurationBuilder
import twitter4j.{Twitter, TwitterException, TwitterFactory}

import com.jrende.Colors._

object Authenticator {
  var twitter: Twitter = null

  def getConsumerKeySecret(): Option[(String, String)] = {
    val file = new File("consumer.properties")
    val prop = new Properties()
    var is: InputStream = null
    var ret: Option[(String, String)] = None
    try {
      if (file.exists()) {
        is = new FileInputStream(file)
        prop.load(is)
        ret = Some(prop.getProperty("oauth.consumerKey"), prop.getProperty("oauth.consumerSecret"))
      }
    } catch {
      case ioe: IOException =>
        ioe.printStackTrace()
        Console.println("Failed to read consumer file".Red())
    } finally {
      if (is != null) {
        try {
          is.close()
        } catch {
          case _: IOException =>
        }
      }
    }
    ret
  }

  def authenticate: Twitter = {
    if(twitter != null) {
      return twitter
    }
    val (consumerKey: String, consumerSecret:String) = getConsumerKeySecret().getOrElse(() => {
      Console.println("Unable to read consumer.properties".Red().Bold())
      System.exit(-1)
    })

    val cb = new ConfigurationBuilder()
    val file = new File("access.properties")
    val prop = new Properties()
    var is: InputStream = null
    var os: OutputStream = null
    try {
      if (file.exists()) {
        is = new FileInputStream(file)
        prop.load(is)
      }
    } catch {
      case ioe: IOException =>
        ioe.printStackTrace()
        System.exit(-1);

    } finally {
      if (is != null) {
        try {
          is.close()
        } catch {
          case ignore: IOException =>
        }
      }
    }
    if (os != null) {
      try {
        os.close()
      } catch {
        case ignore: IOException =>
      }
    }
    cb.setDebugEnabled(true)
      .setOAuthConsumerKey(consumerKey)
      .setOAuthConsumerSecret(consumerSecret)
      .setOAuthAccessToken(null)
      .setOAuthAccessTokenSecret(null)
    try {
      val twitter: Twitter = new TwitterFactory(cb.build()).getInstance()
      val requestToken: RequestToken = twitter.getOAuthRequestToken
      var accessToken: AccessToken = null
      if(prop.getProperty("oauth.accessToken") != null || prop.getProperty("oauth.accessTokenSecret") != null) {
        accessToken = new AccessToken(
          prop.getProperty("oauth.accessToken"),
          prop.getProperty("oauth.accessTokenSecret"))
      }

      val br = new BufferedReader(new InputStreamReader(System.in))
      while (accessToken == null) {
        System.out.println("Open the following URL and grant access to your account:")
        System.out.println(requestToken.getAuthorizationURL)
        try {
          Desktop.getDesktop.browse(new URI(requestToken.getAuthorizationURL))
        } catch {
          case _: UnsupportedOperationException =>
          case _: IOException =>
          case e: URISyntaxException => new AssertionError(e);
        }
        System.out.print("Enter the PIN(if available) and hit enter after you granted access.[PIN]:")
        val pin: String = io.StdIn.readLine()
        try {
          if (pin.length() > 0) {
            accessToken = twitter.getOAuthAccessToken(requestToken, pin)
          } else {
            accessToken = twitter.getOAuthAccessToken(requestToken)
          }
        } catch {
          case te: TwitterException =>
            if (401 == te.getStatusCode) {
              System.err.println("Unable to get the access token.")
            } else {
              te.printStackTrace()
            }
            return null
        }
      }
      twitter.setOAuthAccessToken(accessToken)

      try {
        prop.setProperty("oauth.accessToken", accessToken.getToken)
        prop.setProperty("oauth.accessTokenSecret", accessToken.getTokenSecret)
        os = new FileOutputStream(file)
        prop.store(os, "access.properties")
        os.close()
      } catch {
        case ioe: IOException =>
          ioe.printStackTrace()
          System.exit(-1)

      } finally {
        if (os != null) {
          try {
            os.close()
          } catch {
            case _: IOException =>
          }
        }
      }
      Console.println("Successfully authenticated as " + twitter.verifyCredentials().getScreenName.Green())
      return twitter
    } catch {
      case te: TwitterException =>
        te.printStackTrace()
        System.out.println("Failed to get accessToken: " + te.getMessage)
      case ioe: IOException =>
        ioe.printStackTrace()
        System.out.println("Failed to read the system input.")
    }
    null
  }
}
