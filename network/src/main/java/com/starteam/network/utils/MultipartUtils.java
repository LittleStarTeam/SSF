/*******************************************************************************
 * Copyright (c) 2016 Mickael Gizthon . All rights reserved. Email:2013mzhou@gmail.com
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.starteam.network.utils;

import android.text.TextUtils;

import java.io.File;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.reactivex.annotations.NonNull;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * des: 文件body封装
 */
public class MultipartUtils {

    public static String DEFAULT_KEY = "key";

    /**
     * 获取文件body
     *
     * @param file 文件
     * @return
     */
    public static MultipartBody.Builder getMutipartBuilder(File file) {
        List<File> files = new ArrayList<>();
        files.add(file);
        return getMutipartBuilder(null, null, files);
    }

    public static MultipartBody.Builder getMutipartBuilder(String key, File file) {
        List<File> files = new ArrayList<>();
        files.add(file);
        return getMutipartBuilder(null, key, files);
    }

    public static MultipartBody.Builder getMutipartBuilder(Map<String, String> parameters, File file) {
        List<File> files = new ArrayList<>();
        files.add(file);
        return getMutipartBuilder(parameters, null, files);
    }

    public static MultipartBody.Builder getMutipartBuilder(String key, Map<String, String> parameters, File file) {
        List<File> files = new ArrayList<>();
        files.add(file);
        return getMutipartBuilder(parameters, key, files);
    }

    public static MultipartBody.Builder getMutipartBuilder(String key, List<File> files) {
        return getMutipartBuilder(null, key, files);
    }

    public static MultipartBody.Builder getMutipartBuilder(List<File> files) {
        return getMutipartBuilder(null, null, files);
    }

    public static MultipartBody.Builder getMutipartBuilder(Map<String, String> parameters, List<File> files) {
        return getMutipartBuilder(parameters, null, files);
    }

    @NonNull
    public static MultipartBody.Builder getMutipartBuilder(Map<String, String> parameters, String key, List<File> files) {
        if (TextUtils.isEmpty(key)) {
            key = DEFAULT_KEY;
        }
        MultipartBody.Builder builder = (new MultipartBody.Builder()).setType(MultipartBody.FORM);//FORM 表单提交上传文件

        if (parameters != null && parameters.size() > 0) {
            Iterator<Map.Entry<String, String>> paramsEntry = parameters.entrySet().iterator();
            Map.Entry<String, String> paramsMap;
            while (paramsEntry.hasNext()) {
                paramsMap = paramsEntry.next();
                builder.addFormDataPart(paramsMap.getKey(), paramsMap.getValue());
            }
        }

        if (files != null && files.size() > 0) {
            Iterator<File> fileEntry = files.iterator();
            File file;
            String fileName;
            MediaType mediaType;
            while (fileEntry.hasNext()) {
                file = fileEntry.next();
                fileName = file.getName();
                mediaType = MediaType.parse(guessMimeType(file.getName()));
                builder.addFormDataPart(key, fileName, RequestBody.create(mediaType, file));
            }
        }

        return builder;
    }

    /**
     * 自动解析文件类型
     *
     * @param path 文件路径
     * @return 文件 type
     */
    private static String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

}
