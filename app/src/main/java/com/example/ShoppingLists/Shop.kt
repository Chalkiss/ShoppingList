package com.example.ShoppingLists

import java.util.*
class Shop{
    var title : String = ""
    var date : String = ""
    var itemId : Int = 0
    var isArchived : Int = 0

    constructor(title : String, date : String, itemId: Int){
        this.title = title
        this.date = date
        this.itemId = itemId
    }
    constructor(){}
}
