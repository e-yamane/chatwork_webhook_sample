package jp.rough_diamond.chatwork_webhook_sample.web;

import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.masahirosaito.chatwork4j.ChatWork4j;
import com.masahirosaito.chatwork4j.data.rooms.Member;

import jp.rough_diamond.chatwork_webhook_sample.web.ChatWorkWebHookRequest.EventType;

@RestController
@RequestMapping("/chatwork")
public class WebHook {
	@RequestMapping(value="/", method=RequestMethod.POST)
	public void postMessage(@RequestBody String param) {
		try {
//			System.out.println(param);
			ChatWorkWebHookRequest request = ChatWorkWebHookRequest.toObject(param);
			request.getEventType().filter(
					type -> type == EventType.message_created).ifPresent(et -> execute(request));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void execute(ChatWorkWebHookRequest request) {
		if(isだが断る(request)) {
			sendだが断る(request);
		}
	}

	@Value("${chatwork.api.key}")
	private String chatworkApiKey;
	
	private void sendだが断る(ChatWorkWebHookRequest request) {
//		System.out.println(chatworkApiKey);
		ChatWork4j chatWork4j = new ChatWork4j(chatworkApiKey);
		Member replyMember = Stream.of(chatWork4j.getMembers(
				request.getWebhookEvent().getRoomId())).filter(
						m -> m.getAccount_id() == request.getWebhookEvent().getAccountId()).findFirst().get();
		String message = String.format("[To:%d] %sさん\nだが断る", replyMember.getAccount_id(), replyMember.getName());
		chatWork4j.postMessage(request.getWebhookEvent().getRoomId(), message);
	}

	private boolean isだが断る(ChatWorkWebHookRequest request) {
		String body = request.getWebhookEvent().getBody().trim();
		if(body.startsWith("いいね")) return true;
		if(body.startsWith("いいですね")) return true;
		if(body.startsWith("(y)")) return true;
		if(body.startsWith("(handshake)")) return true;
		if(body.indexOf("お願い") != -1) return true;
		if(body.indexOf("(bow)") != -1) return true;
		return false;
	}
}
