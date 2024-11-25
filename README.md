## Introduction

This is the repository of the cache service of [TLDR: Trending Topics](https://huggingface.co/spaces/jacky5124/TLDR-TrendingTopics), 
an AI with grounding that summarizes relevant news of trending topics. The cache service is powered by 
Spring Boot and Redis, and it allows scheduled jobs to cache results through the sole PUT API and 
the frontend to fetch results through the sole GET API. While the GET API is unrestricted, the PUT API 
requires special authorization which is only obtainable by scheduled jobs, so that cached content will 
not be overwritten by any other party accidentally or deliberately. The cache service is run inside a 
Docker container, allowing flexible scaling to accommodate any kind of user traffic by simply increasing 
or decreasing the number of containers.

## Related Repositories

- [Hugging Face repository of the frontend](https://huggingface.co/spaces/jacky5124/TLDR-TrendingTopics/tree/main)
- [GitHub repository of the scheduled jobs](https://github.com/jacky5124/TLDR-TrendingTopics-Functions)

## API Specification

- `GET /v1/trending-topics?mkt` Checks Redis whether results of trending topics for country market `mkt` 
are available. If results are available, just returns the results, otherwise returns an empty response, 
but in the future it will return an error. This is supposed to be called by the frontend.
- `PUT /v1/trending-topics?mkt` Caches results of trending topics for country market `mkt` in Redis. If there 
are previously cached results for the same `mkt`, then they will be overwritten by the given results. This 
API requires special authorization and is supposed to be called by only the scheduled jobs.

## Value of Query Parameter `mkt`

- For Canada, it will be `en-CA`.
- For United States, it will be `en-US`.

## Results JSON

Both the GET API and the PUT API returns and accepts a JSON of the same format respectively as follows:

```json
{
  "mkt": "either en-CA or en-US",
  "time": "UTC date and time of summary generation for all the following trending topics",
  "topics": [
    {
      "topic": "this trending topic string",
      "summary": "generated summary of this trending topic from snippets of the following news articles",
      "news": [
        {
          "title": "title string of this news article",
          "url": "URL of this news article"
        }
      ]
    }
  ]
}
```