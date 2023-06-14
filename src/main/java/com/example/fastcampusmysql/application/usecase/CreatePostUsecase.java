package com.example.fastcampusmysql.application.usecase;

import com.example.fastcampusmysql.domain.follow.entity.Follow;
import com.example.fastcampusmysql.domain.follow.service.FollowReadService;
import com.example.fastcampusmysql.domain.post.dto.PostCommand;
import com.example.fastcampusmysql.domain.post.service.PostWriteService;
import com.example.fastcampusmysql.domain.post.service.TimelineWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CreatePostUsecase {
	final private PostWriteService postWriteService;
	final private FollowReadService followReadService;
	final private TimelineWriteService timelineWriteService;

	public Long execute(PostCommand postCommand) {
		// POST 게시글 저장
		var postId = postWriteService.create(postCommand);
		// 팔로워 목록 가져오기
		var followerMemberIds = followReadService
				.getFollowers(postCommand.memberId())
				.stream()
				.map(Follow::getFromMemberId)
				.toList();
		timelineWriteService.deliveryToTimeline(postId, followerMemberIds);

		return postId;
	}
}
