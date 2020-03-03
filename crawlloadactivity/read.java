package json.android.com.jsontest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;


//json file needs to put in assets folder in the main
// haven't tested
public class ReadActivity extends CourseDetailActivity {
	@Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coursedetail);
       try {
	       	InputStream is = CourseDetailActivity.this.getClass().getClassLoader().getResourceAsStream("assets" + "2019_course_name.json")
	       	BufferedReader read = new BufferedReader(new InputStreamReader(is));
	       	String line;
	       	StringBuilder builder = new StringBuilder();
	       	while ((line = read.readLine()) != null) {
	       		builder.append(line);
	       	}
	       	is.close();
	       	read.close();
	       	try {
	       		JSONObject root = new JSONObject(builder.toString());
	       		JSONArray info = root.getJSONArray();
	       		for (int i = 0; i < info.length(); i++) {
	       			JSONObject inf = info.getJSONObject(i);
	       		}
	       	}
	       	folderList = new ArrayList<>();
            for (int i = 0; i < courseJsonArray.length(); i++) {
                JSONObject courseJsonObject = courseJsonArray.getJSONObject(i);
                Folder folder = new Folder();
                folder.setId(courseJsonObject.getInt("id"));
                folder.setTitle(courseJsonObject.getString("title"));
                folder.setContent(courseJsonObject.getString("content"));
                folder.setPath(courseJsonObject.getString("path"));
                folder.setFileName(courseJsonObject.getString("fileName"));
                folder.setLikeNum(courseJsonObject.getInt("likeNum"));
                folder.setDislikeNum(courseJsonObject.getInt("dislikeNum"));
                folderList.add(folder);
	       	catch (JSONException e) {
	       		e.printStackTrace();
	       	}
	    }
       	catch (UnsupportedOperationException e) {
       		e.printStackTrace();
       	}
       	catch (IOException e) {
       		e.printStackTrace();
       	}
    }
}