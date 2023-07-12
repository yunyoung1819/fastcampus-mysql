package com.example.fastcampusmysql.domain.post.service;

import com.example.fastcampusmysql.domain.member.dto.MemberDto;
import com.example.fastcampusmysql.domain.post.entity.Post;
import com.example.fastcampusmysql.domain.post.entity.PostLike;
import com.example.fastcampusmysql.domain.post.repository.PostLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 좋아요 수 집계 테이블 분리
 */
@RequiredArgsConstructor
@Service
public class PostLikeWriteService {

	final private PostLikeRepository postLikeRepository;

	public Long create(Post post, MemberDto memberDto) {
		var postLike = PostLike
				.builder()
				.postId(post.getId())
				.memberId(memberDto.id())
				.build();

		return postLikeRepository.save(postLike).getPostId();
	}
}
