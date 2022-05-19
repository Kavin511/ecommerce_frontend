package com.devstudio.zivame.cart

import java.util.*

data class ShoppingCartItem(
    var id: UUID,
    var quantity: Long,
    var position: Long
)