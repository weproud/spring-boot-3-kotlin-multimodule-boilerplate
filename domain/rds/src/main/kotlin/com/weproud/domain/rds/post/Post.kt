package com.weproud.domain.rds.post

import com.weproud.domain.BaseAuditEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "posts")
class Post(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = NEW_ID,
    @Column(name = "user_id", nullable = false)
    var userId: String,
    @Column(name = "title", nullable = false)
    var title: String,
    @Column(name = "content", nullable = false)
    var content: String,
) : BaseAuditEntity() {
    companion object {
        fun create(
            userId: String,
            title: String,
            content: String,
        ): Post {
            return Post(
                id = NEW_ID,
                userId = userId,
                title = title,
                content = content,
            )
        }
    }
}
