package com.lp.akka.notes;

import com.jasongoodwin.monads.Try;

/**
 * @author li.pan
 * @version 1.0.0
 * @Description 用 BoilerPipe 库来解析页面。
 * @createTime 2021年01月14日 12:47:00
 */
public class ArticleParser {
    public static Try<String> apply(String html) {
        return Try.ofFailable(
                () -> de.l3s.boilerpipe.extractors.ArticleExtractor.INSTANCE.getText(html)
        );
    }
}
