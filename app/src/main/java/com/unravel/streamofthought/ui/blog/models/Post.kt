package com.unravel.streamofthought.ui.blog.models

data class Post(
    val text: String = "",
    val createdBy: com.unravel.streamofthought.ui.blog.models.User = User(),
    val createdAt: Long = 0L,
    val likedBy: ArrayList<String> = ArrayList())