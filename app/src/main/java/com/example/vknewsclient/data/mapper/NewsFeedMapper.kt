package com.example.vknewsclient.data.mapper

import com.example.vknewsclient.data.model.CommentsResponseDto
import com.example.vknewsclient.data.model.NewsFeedResponseDto
import com.example.vknewsclient.domain.FeedPost
import com.example.vknewsclient.domain.PostComment
import com.example.vknewsclient.domain.StatisticItem
import com.example.vknewsclient.domain.StatisticType
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.absoluteValue

class NewsFeedMapper {

    fun mapResponseToPosts(responseDto: NewsFeedResponseDto): List<FeedPost> {
        val result = mutableListOf<FeedPost>()
        val posts = responseDto.newsFeedContent.posts
        val groups = responseDto.newsFeedContent.groups

        for (post in posts) {
            val group = groups.find { it.id == post.communityId.absoluteValue } ?: break
                val feedPost = FeedPost(
                    id = post.id,
                    communityId = post.communityId,
                    communityName = group.name,
                    publicationDate = mapTimestampToDate(post.date * 1000),
                    communityImageUrl = group.imageUrl,
                    contentText = post.text,
                    contentImageUrl = post.attachments?.firstOrNull()?.photo?.photoUrls?.lastOrNull()?.url,
                    statistics = listOf(
                        StatisticItem(type = StatisticType.LIKES, count = post.likes.count),
                        StatisticItem(type = StatisticType.VIEWS, count = post.views.count),
                        StatisticItem(type = StatisticType.SHARES, count = post.reposts.count),
                        StatisticItem(type = StatisticType.COMMENTS, count = post.comments.count),
                    ),
                    isLiked = post.likes.userLikes > 0
                )
            result.add(feedPost)
        }
        return result
    }

    private fun mapTimestampToDate(timestamp: Long): String {
        return SimpleDateFormat("d MMMM yyyy, hh:mm", Locale.getDefault()).format(Date(timestamp))
    }

    fun mapResponseToComments(response: CommentsResponseDto): List<PostComment> {
        val result = mutableListOf<PostComment>()
        val comments = response.content.comments
        val profiles = response.content.profiles
        for (comment in comments) {
            if (comment.text.isBlank()) continue
            val author = profiles.firstOrNull { it.id == comment.authorId } ?: continue
            val postComment = PostComment(
                id = comment.id,
                authorName = "${author.firstName} ${author.lastName}",
                authorAvatarUrl = author.avatarUrl,
                commentText = comment.text,
                publicationDate = mapTimestampToDate(comment.date)
            )
            result.add(postComment)
        }
        return result
    }
}