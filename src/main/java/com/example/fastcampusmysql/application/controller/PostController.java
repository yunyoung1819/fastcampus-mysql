package com.example.fastcampusmysql.application.controller;

import com.example.fastcampusmysql.application.usecase.CreatePostLikeUseCase;
import com.example.fastcampusmysql.application.usecase.CreatePostUsecase;
import com.example.fastcampusmysql.application.usecase.GetTimelinePostsUsecase;
import com.example.fastcampusmysql.domain.post.dto.DailyPostCount;
import com.example.fastcampusmysql.domain.post.dto.DailyPostCountRequest;
import com.example.fastcampusmysql.domain.post.dto.PostCommand;
import com.example.fastcampusmysql.domain.post.dto.PostDto;
import com.example.fastcampusmysql.domain.post.entity.Post;
import com.example.fastcampusmysql.domain.post.service.PostReadService;
import com.example.fastcampusmysql.domain.post.service.PostWriteService;
import com.example.fastcampusmysql.util.CursorRequest;
import com.example.fastcampusmysql.util.PageCursor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/posts")
public class PostController {
	final private PostWriteService postWriteService;

	final private PostReadService postReadService;

	final private GetTimelinePostsUsecase getTimelinePostsUsecase;

	final private CreatePostUsecase createPostUsecase;

	final private CreatePostLikeUseCase createPostLikeUseCase;

	@PostMapping("")
	public Long create(PostCommand command) {
		return createPostUsecase.execute(command);
	}

	@GetMapping("/daily-post-counts")
	public List<DailyPostCount> getDailyPostCounts(DailyPostCountRequest request) {
		return postReadService.getDailyPostCounts(request);
	}

	@GetMapping("/members/{memberId}")
	public Page<PostDto> getPosts(
			@PathVariable Long memberId,
			Pageable pageable
	) {
		return postReadService.getPosts(memberId, pageable);
	}

	@GetMapping("/members/{memberId}/by-cursor")
	public PageCursor<Post> getPostsByCursor(
			@PathVariable Long memberId,
			CursorRequest cursorRequest
	) {
		return postReadService.getPosts(memberId, cursorRequest);
	}

	@GetMapping("/member/{memberId}/timeline")
	public PageCursor<Post> getTimeline(
			@PathVariable Long memberId,
			CursorRequest cursorRequest
	) {
		return getTimelinePostsUsecase.executeByTimeline(memberId, cursorRequest);
	}

	// 좋아요 구현 - v1
	@PostMapping("/{postId}/like/v1")
	public void likePost(@PathVariable Long postId) {
//		postWriteService.likePost(postId);
		postWriteService.likePostByOptimisticLock(postId);
	}

	// 좋아요 구현 - v2 (테이블 분리)
	@PostMapping("/{postId}/like/v2")
	public void likePostV2(@PathVariable Long postId, @RequestParam Long memberId) {
		createPostLikeUseCase.execute(postId, memberId);
	}
}
