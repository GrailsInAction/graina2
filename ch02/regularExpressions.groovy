#! /usr/bin/env groovy

import java.util.regex.Matcher

// This is the string we are going to match
String str = "The rain in Spain falls mainly on the plain"

// ...and now we try to match a pattern to that string. The pattern
// should capture any word with "ain" in it.
Matcher m = str =~ /\b(\w*)ain(\w*)\b/

if (m) {
    for (int i = 0; i < m.count; i++) {

        println "Found: '${m[i][0]}' – prefix: '${m[i][1]}'" +
            ", suffix: '${m[i][2]}'"
    }
}

