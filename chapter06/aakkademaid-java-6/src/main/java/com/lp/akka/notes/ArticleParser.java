package com.lp.akka.notes;

import com.jasongoodwin.monads.Try;

/**
 * @author lipan
 */
public class ArticleParser {
    public static Try<String> apply(String html) {
        return Try.ofFailable(
                () -> de.l3s.boilerpipe.extractors.ArticleExtractor.INSTANCE.getText(html)
        );
    }
}
