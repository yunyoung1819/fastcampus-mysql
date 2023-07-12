package com.example.fastcampusmysql.application.usecase;

import com.example.fastcampusmysql.domain.member.service.MemberReadService;
import com.example.fastcampusmysql.domain.post.service.PostLikeWriteService;
import com.example.fastcampusmysql.domain.post.service.PostReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreatePostLikeUseCase {
	final private PostReadService postReadService;
	final private MemberReadService memberReadService;
	final private PostLikeWriteService postLikeWriteService;

	public void execute(Long postId, Long memberId) {
		var post = postReadService.getPost(postId);
		var member = memberReadService.getMember(memberId);
		postLikeWriteService.create(post, member);
	}
}
