package com.example.ShoppingLists

class Item{
    var name : String = ""
    var itemId : Int = 0
    var parentId : Int = 0

    constructor(name:String, itemId : Int, parentId: Int){
        this.name = name
        this.itemId = itemId
        this.parentId = parentId
    }
    constructor(){}
}