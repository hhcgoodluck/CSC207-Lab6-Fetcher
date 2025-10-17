package dogapi;

import java.util.*;

/**
 * This BreedFetcher caches fetch request results to improve performance and
 * lessen the load on the underlying data source. An implementation of BreedFetcher
 * must be provided. The number of calls to the underlying fetcher are recorded.
 *
 * If a call to getSubBreeds produces a BreedNotFoundException, then it is NOT cached
 * in this implementation. The provided tests check for this behaviour.
 *
 * The cache maps the name of a breed to its list of sub breed names.
 */
public class CachingBreedFetcher implements BreedFetcher {
    // Task 2: Complete this class

    // 记录底层 fetcher 被调用次数
    private int callsMade = 0;

    // 实际执行 API 请求的底层 fetcher（例如 DogApiBreedFetcher）
    private final BreedFetcher fetcher;

    // 缓存表 键为 breed 名称，值为对应的子品种列表
    private final Map<String, List<String>> cache = new HashMap<>();

    // 构造函数 传入一个真正执行 API 调用的 fetcher
    public CachingBreedFetcher(BreedFetcher fetcher) {
        this.fetcher = fetcher;
    }

    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {
        // return statement included so that the starter code can compile and run.
        // return new ArrayList<>();

        // 转成小写 保证 key 一致性
        String key = breed.toLowerCase();

        // 检查缓存
        if (cache.containsKey(key)) {
            return cache.get(key);
        }

        // 未命中缓存 调用底层 fetcher
        try {
            callsMade++;
            List<String> subBreeds = fetcher.getSubBreeds(breed);

            // 结果放入缓存
            cache.put(key, subBreeds);
            return subBreeds;
        }
        catch (BreedNotFoundException e) {
            throw e;
        }
    }

    public int getCallsMade() {
        return callsMade;
    }
}