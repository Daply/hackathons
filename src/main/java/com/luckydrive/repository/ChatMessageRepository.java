package com.luckydrive.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.luckydrive.model.ChatMessage;

public interface ChatMessageRepository extends CrudRepository<ChatMessage, Long> {

	@Query(value = "SELECT * FROM chat_messages cm WHERE cm.trip_id=:trip_id ORDER BY cm.time_sent", nativeQuery = true)
	List<ChatMessage> getTripChatMessages(@Param("trip_id") Long trip_id);

}
