package com.example.fastcampusmysql.domain.post;

import com.example.fastcampusmysql.domain.post.entity.Post;
import com.example.fastcampusmysql.domain.post.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;

@SpringBootTest
public class PostBulkInsertTest {
	@Autowired
	private PostRepository postRepository;

	@Test
	public void bulkInsert() {
		var easyRandom = PostFixtureFactory.get(
				3L,
				LocalDate.of(1970, 1, 1),
				LocalDate.of(2023, 5, 10)
		);

		var stopWatch = new StopWatch();
		stopWatch.start();

		int _1만 = 10000;
		var posts = IntStream.range(0, _1만 * 100)
				.parallel()
				.mapToObj(i -> easyRandom.nextObject(Post.class))
				.toList();
		stopWatch.stop();

		var queryStopWatch = new StopWatch();
		System.out.println("객체 생성 시간 : " + stopWatch.getTotalTimeSeconds());

		queryStopWatch.start();
		postRepository.bulkInsert(posts);
		queryStopWatch.stop();
		System.out.println("DB 인서트 시간 : " + queryStopWatch.getTotalTimeSeconds());
	}
}
