package com.example.fastcampusmysql.util;

import com.example.fastcampusmysql.domain.member.entity.Member;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

public class MemberFixtureFactory {

	static public Member create() {
		var param = new EasyRandomParameters();
		return new EasyRandom(param).nextObject(Member.class);
	}

	static public Member create(Long seed) {
//		return Member.builder()
//				.nickname("yyun")
//				.email("yyun1234@gmail.com")
//				.build();
		var param = new EasyRandomParameters().seed(seed);
		return new EasyRandom(param).nextObject(Member.class);
	}
}
