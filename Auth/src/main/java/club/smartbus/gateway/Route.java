package club.smartbus.gateway;


public class Route {
    private String id;
    private String path;
    private String url;

    public Route(String id, String url, String path) {
        this.id = id;
        this.url = url;
        this.path = path;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
