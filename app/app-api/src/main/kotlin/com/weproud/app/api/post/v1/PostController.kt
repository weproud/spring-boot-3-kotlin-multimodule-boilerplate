package com.weproud.app.api.post.v1

import com.weproud.app.api.post.v1.dto.PostResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/posts")
class PostController(
    private val postService: PostService,
) {
    @GetMapping
    fun getPosts(): List<PostResponse> {
        return postService.getPosts()
    }
}
