package com.example.fastcampusmysql.domain.post.service;

import com.example.fastcampusmysql.domain.post.dto.DailyPostCount;
import com.example.fastcampusmysql.domain.post.dto.DailyPostCountRequest;
import com.example.fastcampusmysql.domain.post.entity.Post;
import com.example.fastcampusmysql.domain.post.repository.PostRepository;
import com.example.fastcampusmysql.util.CursorRequest;
import com.example.fastcampusmysql.util.PageCursor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.OptionalLong;

@RequiredArgsConstructor
@Service
public class PostReadService {
	final private PostRepository postRepository;

	public List<DailyPostCount> getDailyPostCounts(DailyPostCountRequest request) {
		/*
		 * 반환 값 -> 리스트 [작성일자, 작성회원, 작성 게시물 갯수]
		 * select createdDate, memberId, count(id)
		 * from Post
		 * where memberId = :memberId and createDate between firstDate and lastDate
		 * group by createDate memberId
		 */
		return postRepository.groupByCreatedDate(request);
	}

	public Page<Post> getPosts(Long memberId, Pageable pageRequest) {
		return postRepository.findAllByMemberId(memberId, pageRequest);
	}

	public PageCursor<Post> getPosts(Long memberId, CursorRequest cursorRequest) {
		var posts = findAllBy(memberId, cursorRequest);
		var nextKey = getNextKey(posts);
		return new PageCursor<>(cursorRequest.next(nextKey), posts);
	}

	public PageCursor<Post> getPosts(List<Long> memberIds, CursorRequest cursorRequest) {
		var posts = findAllBy(memberIds, cursorRequest);
		var nextKey = getNextKey(posts);
		return new PageCursor<>(cursorRequest.next(nextKey), posts);
	}

	private long getNextKey(List<Post> posts) {
		return posts.stream()
				.mapToLong(Post::getId)
				.min()
				.orElse(CursorRequest.NONE_KEY);
	}

	private List<Post> findAllBy(Long memberId, CursorRequest cursorRequest) {
		if (cursorRequest.hasKey()) {
			return postRepository.findAllByLessThanIdMemberIdAndOrderByIdDesc(
					cursorRequest.key(),
					memberId,
					cursorRequest.size()
			);
		}
		return postRepository.findAllByMemberIdAndOrderByIdDesc(memberId, cursorRequest.size());
	}

	private List<Post> findAllBy(List<Long> memberIds, CursorRequest cursorRequest) {
		if (cursorRequest.hasKey()) {
			return postRepository.findAllByLessThanIdAndInMemberIdsAndOrderByIdDesc(
					cursorRequest.key(),
					memberIds,
					cursorRequest.size()
			);
		}
		return postRepository.findAllByInMemberIdAndOrderByIdDesc(memberIds, cursorRequest.size());
	}

	public List<Post> getPosts(List<Long> ids) {
		return postRepository.findAllByInId(ids);
	}
}
