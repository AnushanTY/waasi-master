package waasi.waasi;

/**
 * Created by Chrustkiran on 23/09/2017.
 */

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;
public class Offer {

    public String name;
    public String desc;
    public String image;
    public Integer like;



    public Map<String, Boolean> stars = new HashMap<>();

    public Offer() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Offer(String name, String desc,String image,Integer like) {
        this.name = name;
        this.desc = desc;
        this.image = image;
        this.like = like;





    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("desc", desc);
        result.put("image",image);
        result.put("like",like);

        return result;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }
    public  String getImage(){
        return image;
    }
    public Integer getLike(){return like;}


}