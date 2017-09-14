package cc.idiary.nuclear.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Neo on 2017/6/2.
 */
public class FileInputResponseBuilder {
    private Map<String, Object> json = new HashMap<>();

    public FileInputResponseBuilder setError(String msg){
        json.put("error", msg);
        return this;
    }

    public FileInputResponseBuilder addPreviewItem(String url, String fileName, Long size, String key){

        @SuppressWarnings("unchecked")
        List<String> initialPreview = (List<String>) json.get("initialPreview");
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> initialPreviewConfig = (List<Map<String, Object>>) json.get("initialPreviewConfig");

        if (initialPreview == null){
            initialPreview = new ArrayList<>();
            json.put("initialPreview", initialPreview);
        }
        if (initialPreviewConfig == null){
            initialPreviewConfig = new ArrayList<>();
            json.put("initialPreviewConfig", initialPreviewConfig);
        }

        initialPreview.add(url);

        Map<String, Object> configItem = new HashMap<>();
        configItem.put("caption", fileName);
        configItem.put("size", size);
//        configItem.put("url", url);
        configItem.put("key", key);
        configItem.put("type", getType(fileName));

        initialPreviewConfig.add(configItem);

        return this;
    }

    private String getType(String fileName){

        if (fileName.matches(".*pdf$")) {
            return  "pdf";
        }
        if (fileName.matches(".*(jpg|gif|png)$")) {
            return "image";
        }
        if (fileName.matches(".*(mp4|ogg)$")) {
            return "video";
        } else {
            return "object";
        }
    }

    public Map<String, Object> build(){
        return json;
    }
}
