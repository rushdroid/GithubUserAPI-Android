package com.example.tawktopractice.data.model


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "user_detail_table")
data class UserDetail(
    @SerializedName("avatar_url")
    val avatarUrl: String?,
    @SerializedName("bio")
    val bio: String?,
    @SerializedName("blog")
    val blog: String?,
    @SerializedName("company")
    val company: String?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("events_url")
    val eventsUrl: String?,
    @SerializedName("followers")
    val followers: Int,
    @SerializedName("followers_url")
    val followersUrl: String?,
    @SerializedName("following")
    val following: Int,
    @SerializedName("following_url")
    val followingUrl: String?,
    @SerializedName("gists_url")
    val gistsUrl: String?,
    @SerializedName("gravatar_id")
    val gravatarId: String?,
    @SerializedName("hireable")
    val hireable: String?,
    @SerializedName("html_url")
    val htmlUrl: String?,
    @PrimaryKey
    @SerializedName("id")
    val id: Int,
    @SerializedName("location")
    val location: String?,
    @SerializedName("login")
    val login: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("node_id")
    val nodeId: String?,
    @SerializedName("organizations_url")
    val organizationsUrl: String?,
    @SerializedName("public_gists")
    val publicGists: Int,
    @SerializedName("public_repos")
    val publicRepos: Int,
    @SerializedName("received_events_url")
    val receivedEventsUrl: String?,
    @SerializedName("repos_url")
    val reposUrl: String?,
    @SerializedName("site_admin")
    val siteAdmin: Boolean,
    @SerializedName("starred_url")
    val starredUrl: String?,
    @SerializedName("subscriptions_url")
    val subscriptionsUrl: String?,
    @SerializedName("twitter_username")
    val twitterUsername: String?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("updated_at")
    val updatedAt: String?,
    @SerializedName("url")
    val url: String?,
    @SerializedName("note")
    var note: String? = ""
)