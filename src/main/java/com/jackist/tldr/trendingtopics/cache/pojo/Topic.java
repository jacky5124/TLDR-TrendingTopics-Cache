package com.jackist.tldr.trendingtopics.cache.pojo;

import java.util.List;

public record Topic(String topic, String summary, List<News> news) {
}
