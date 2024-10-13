package com.jackist.tldr.trendingtopics.summarycache.pojo;

import java.util.List;

public record Result(String mkt, String time, List<Topic> topics) {
}
