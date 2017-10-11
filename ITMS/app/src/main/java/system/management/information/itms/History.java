package system.management.information.itms;

/**
 * Created by janescience on 22/8/2560.
 */

public class History {

    private String Name;
    private String Topic;
    private String Page;
    private String Header;
    private String Detail;
    private String Date;


    public History(){

    }

    public History(String Name, String Topic,String Page,String Header, String Detail, String Date){

        this.Name=Name;
        this.Topic=Topic;
        this.Page=Page;
        this.Header=Header;
        this.Detail=Detail;
        this.Date=Date;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPage() {
        return Page;
    }

    public void setPage(String page) {
        Page = page;
    }

    public String getTopic() {
        return Topic;
    }

    public void setTopic(String topic) {
        Topic = topic;
    }

    public String getHeader() {
        return Header;
    }

    public void setHeader(String Header) {
        this.Header = Header;
    }

    public String getDetail() {
        return Detail;
    }

    public void setDetail(String Detail) {
        this.Detail = Detail;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String Date) {
        this.Date = Date;
    }
}
