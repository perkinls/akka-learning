package com.lp.akka.notes

import akka.actor.Actor

/**
 * @author li.pan
 * @version 1.0.0
 * @Description TODO
 * @createTime 2021年01月14日 12:56:00
 */
class ArticleParseActor extends Actor {
  override def receive: Receive = {
    case ParseArticle(htmlString) =>
      val body: String = ArticleParser(htmlString)
      sender() ! body
  }
}

object ArticleParser {
  def apply(html: String): String =
    de.l3s.boilerpipe.extractors.ArticleExtractor.INSTANCE.getText(html)
}

case class ParseArticle(htmlBody: String)