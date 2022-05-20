package com.devstudio.zivame.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class ShoppingCartItem():RealmObject(){
    @PrimaryKey
    var id:Long=0
    var name:String = ""
    var rating:Int = 0
    var price:String = ""
    var imageUrl:String = ""
}