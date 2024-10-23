package com.jackist.tldr.trendingtopics.cache.pojo.cache;

import java.util.List;

public record Result(String mkt, String time, List<Topic> topics) {
}
