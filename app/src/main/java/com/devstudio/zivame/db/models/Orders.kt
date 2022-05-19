package com.devstudio.zivame.db.models

import com.devstudio.zivame.models.Product
import java.util.*

open class Orders(
    val id: UUID = UUID.randomUUID(),
    var products: List<Product>
)