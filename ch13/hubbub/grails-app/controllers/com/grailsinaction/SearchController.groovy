package com.grailsinaction

class SearchController {

    def search(String q) {
        if (!q) {
            return [:]
        }

        try {
            params.withHighlighter = {highlighter, index, sr ->
                // lazy-init the list of highlighted search results
                if (!sr.highlights) {
                    sr.highlights = []
                }
                // store highlighted text;
                // "content" is a searchable-property of the
                // Post domain class
                def matchedFragment = highlighter.fragment("content")
                sr.highlights[index] = "..." + (matchedFragment ?: "") + "..."
            }

            if (params.justMine) {
                q += " +loginId:${session.user.loginId}"
            }

            params.suggestQuery = true
            def searchResult = Post.search(q, params)
            return [searchResult: searchResult]
        } catch (e) {
            return [searchError: true]
        }
    }
    
}
