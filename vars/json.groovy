import groovy.json.*;

def parseJson(text) {
    return new JsonSlurper().parseText(text)
}

def stringifyJson(obj) {
    new JsonBuilder(obj).toString()
}
