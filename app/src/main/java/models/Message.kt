package models

/**
 * Created by Joe on 8/30/2017.
 */

class Message {

    constructor() //empty for firebase

    constructor(messageText: String){
        text = messageText
    }
    var text: String? = null
    var timestamp: Long = System.currentTimeMillis()
}
