package com.lp.akka.notes

/**
 * @author li.pan
 * @version 1.0.0
 * @Description TODO
 * @createTime 2021年01月14日 13:03:00
 */
object ArticleParser {
  def apply(html: String): String = de.l3s.boilerpipe.extractors.ArticleExtractor.INSTANCE.getText(html)
}
