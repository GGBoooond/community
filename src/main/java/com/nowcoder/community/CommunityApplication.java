package com.nowcoder.community;

import com.nowcoder.community.elasticsearch.DiscussPostRepository;
import com.nowcoder.community.service.DiscussPostService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@SpringBootApplication
public class CommunityApplication {
	@Resource
	private DiscussPostRepository discussPostRepository;
	@Resource
	private DiscussPostService discussPostService;
	@PostConstruct
	public void init() {
		// 解决netty启动冲突问题
		// see Netty4Utils.setAvailableProcessors()
		System.setProperty("es.set.netty.runtime.available.processors", "false");

		//加载数据到ES
		discussPostRepository.saveAll(discussPostService.selectDiscussPosts(0,0,10000));
	}

	public static void main(String[] args) {
		SpringApplication.run(CommunityApplication.class, args);
	}

}
