package com.grailsinaction

class SearchController {

    def search = {
        def query = params.q

        if (!query) {
            return [:]
        }

        try {
            params.withHighlighter = {highlighter, index, sr ->
                // lazy-init the storage
                if (!sr.highlights) {
                    sr.highlights = []
                }

                // store highlighted text; "content" is a searchable-property of the Post domain class
                def matchedFragment = highlighter.fragment("content")
                sr.highlights[index] = "..." + (matchedFragment ?: "") + "..."
            }

            def searchResult = Post.search(query, params)
            return [searchResult: searchResult]


        } catch (Exception e) {
            return [searchError: true]
        }
    }
}

