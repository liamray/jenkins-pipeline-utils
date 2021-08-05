def makeGetRequest(url, headers) {
    def connection = new URL(url).openConnection() as HttpURLConnection
    connection.setDoOutput(true)

    if (headers != null) {
        headers.each { item -> connection.setRequestProperty(item.key, item.value) }
    }

    return [
            "text"        : connection.inputStream.text,
            "responseCode": connection.responseCode,
            "headers"     : connection.getHeaderFields()
    ]
}




def makePostRequest(url, headers, params) {
    // url
    def httpPost = new URL(url).openConnection();

    // method
    httpPost.setRequestMethod("POST")
    httpPost.setDoOutput(true)

    // headers
    headers.each { item -> httpPost.setRequestProperty(item.key, item.value) }

    // dealing with params
    def paramsAsStr

    if (params.getClass() == java.lang.String) {
        paramsAsStr = params
    }

    // is key=value params?
    if (params.getClass() == java.util.LinkedHashMap && headers.containsValue("application/x-www-form-urlencoded")) {
        paramsAsStr = ""
        params.each { item -> paramsAsStr += "${item.key}=${item.value}&" }

    }

    // is JSON params?
    if (params.getClass() == java.util.LinkedHashMap && headers.containsValue("application/json")) {
        paramsAsStr = stringifyJson(params)
    }

    if (paramsAsStr == null) {
        error "The [params] cannot be converted to the necessary form. It must either of a String or Map type. When the parameter of the Map type the [headers] must contain a [application/json] or [application/x-www-form-urlencoded] record to know how to conver the params"
    }

    // params
    httpPost.getOutputStream().write(paramsAsStr.getBytes("UTF-8"));

    // executing the request
    def httpResponse = httpPost.getResponseCode();
    text = httpPost.getInputStream().getText();

    return [
            "text"        : text,
            "responseCode": httpResponse,
            "headers"     : httpPost.getHeaderFields()
    ]
}
