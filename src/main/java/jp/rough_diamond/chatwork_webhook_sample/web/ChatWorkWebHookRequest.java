package jp.rough_diamond.chatwork_webhook_sample.web;

import java.util.Date;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatWorkWebHookRequest {
	private Long webhookSettingId;
	private String webhookEventType;
	private Long webhookEventTime;
	private Event webhookEvent;
	
	public Date getEventTime() {
		return new Date(getWebhookEventTime());
	}
	
	public Optional<EventType> getEventType() {
		try {
			return Optional.of(EventType.valueOf(getWebhookEventType()));
		} catch(IllegalArgumentException e) {
			return Optional.empty();
		}
	}
	
	public static ChatWorkWebHookRequest toObject(String json) throws Exception {
		ObjectMapper mapper = getObjectMapper();
		return mapper.readValue(json, ChatWorkWebHookRequest.class);
	}

	public static ObjectMapper getObjectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
		return mapper;
	}

	@Data
	@ToString
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Event {
		private Long messageId;
		private Integer roomId;
		private Integer accountId;
		private String body;
		private Long sendTime;
		private Long updateTime;
		
		public Date getSendDateTime() {
			return new Date(getSendTime());
		}

		public Date getUpdateDateTime() {
			return new Date(getUpdateTime());
		}
	}
	
	public static enum EventType {
		message_created,
		message_updated,
		mention_to_me;
	}
}
