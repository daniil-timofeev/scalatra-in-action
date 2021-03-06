package com.constructiveproof.crawler.actors

import java.net.URL
import java.nio.charset.StandardCharsets

import akka.actor.Actor

import scala.io.Source


class GrabActor extends Actor {

  def receive = {
    case url: URL => evaluate(url)
    case _ => sender ! "That wasn't a URL."
  }

  def evaluate(url: URL) = {
    val content = Source.fromURL(
      url, StandardCharsets.UTF_8.name()
    ).mkString

    content.contains("Akka") match {
      case true => sender ! "It's an Akka-related site, very cool."
      case false => sender ! "No Akka here, you've made some sort of " +
        "mistake in your reading choices."
    }
  }

}