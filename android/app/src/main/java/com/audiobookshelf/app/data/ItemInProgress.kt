/*
    Used in Android Auto to represent a podcast episode or an audiobook in progress
 */

package com.audiobookshelf.app.data

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.core.json.JsonReadFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.json.JSONObject

@JsonIgnoreProperties(ignoreUnknown = true)
data class ItemInProgress(
  val libraryItemWrapper: LibraryItemWrapper,
  val episode: PodcastEpisode?,
  val progressLastUpdate: Long,
  val isLocal: Boolean
) {
  companion object {
    fun makeFromServerObject(serverItem: JSONObject):ItemInProgress {
      val jacksonMapper = jacksonObjectMapper().enable(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature())
      val libraryItem = jacksonMapper.readValue<LibraryItem>(serverItem.toString())

      var episode:PodcastEpisode? = null
      if (serverItem.has("recentEpisode")) {
        episode = jacksonMapper.readValue<PodcastEpisode>(serverItem.get("recentEpisode").toString())
      }

      val progressLastUpdate:Long = serverItem.getLong("progressLastUpdate")
      return ItemInProgress(libraryItem, episode, progressLastUpdate, false)
    }
  }
}
