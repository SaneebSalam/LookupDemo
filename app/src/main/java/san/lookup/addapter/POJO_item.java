package san.lookup.addapter;

import java.util.ArrayList;


/**
 * Created by Saneeb Salam
 * on 12/7/2016.
 */
public class POJO_item {

    private String name;
    private String url;
    private String id;
    private String image;
    private String address;
    private String headerTitle;
    private String latlng;
    private ArrayList<POJO_item> allItemsInSection;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatlng() {
        return latlng;
    }

    public void setLatlng(String latlng) {
        this.latlng = latlng;
    }

    public POJO_item() {

    }

    String getHeaderTitle() {
        return headerTitle;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    ArrayList<POJO_item> getAllItemsInSection() {
        return allItemsInSection;
    }

    public void setAllItemsInSection(ArrayList<POJO_item> allItemsInSection) {
        this.allItemsInSection = allItemsInSection;
    }


    public POJO_item(String id, String name, String url) {

        this.id = id;
        this.name = name;
        this.url = url;
    }


    String getUrl() {
        return url;
    }


}
