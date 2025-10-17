package dogapi;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

/**
 * BreedFetcher implementation that relies on the dog.ceo API.
 * Note that all failures get reported as BreedNotFoundException
 * exceptions to align with the requirements of the BreedFetcher interface.
 */
public class DogApiBreedFetcher implements BreedFetcher {
    private final OkHttpClient client = new OkHttpClient();

    /**
     * Fetch the list of sub breeds for the given breed from the dog.ceo API.
     * @param breed the breed to fetch sub breeds for
     * @return list of sub breeds for the given breed
     * @throws BreedNotFoundException if the breed does not exist (or if the API call fails for any reason)
     */
    @Override
    public List<String> getSubBreeds(String breed) {
        //      Task 1: Complete this method based on its provided documentation
        //      and the documentation for the dog.ceo API. You may find it helpful
        //      to refer to the examples of using OkHttpClient from the last lab,
        //      as well as the code for parsing JSON responses.

        // return statement included so that the starter code can compile and run.
        // return new ArrayList<>();

        // 构造完整 API 请求 URL
        // Dog API 的端点格式：https://dog.ceo/api/breed/{breed}/list
        String url = "https://dog.ceo/api/breed/" + breed.toLowerCase() + "/list";

        // 创建 HTTP 请求对象
        // Request.Builder() 用于构建一个 HTTP 请求（GET 请求）
        Request request = new Request.Builder()
                .url(url)
                .build();

        // 执行请求并处理响应
        try (Response response = client.newCall(request).execute()) {
            // 检查 HTTP 响应是否成功
            if (!response.isSuccessful()) {
                throw new BreedNotFoundException("API request failed for breed: " + breed);
            }

            // 读取响应体 JSON字符串
            String jsonData = response.body().string();

            // 将 JSON 字符串 解析为 JSONObject
            JSONObject json = new JSONObject(jsonData);

            // 检查 "status" 字段是否为 "success"
            if (!json.has("status") || !json.getString("status").equals("success")) {
                throw new BreedNotFoundException("Invalid response for breed: " + breed);
            }

            // 从 JSON 中提取 "message" JSONArray 数组 包含所有子品种
            JSONArray subBreedsArray = json.getJSONArray("message");

            // 将 JSONArray 转换为 Java List<String>
            List<String> subBreeds = new ArrayList<>();
            for (int i = 0; i < subBreedsArray.length(); i++) {
                subBreeds.add(subBreedsArray.getString(i));
            }

            return subBreeds;

        } catch (IOException e) {
            throw new BreedNotFoundException("Network or IO error while fetching breed: " + breed);
        } catch (Exception e) {
            throw new BreedNotFoundException("Unexpected error for breed: " + breed);
        }
    }
}