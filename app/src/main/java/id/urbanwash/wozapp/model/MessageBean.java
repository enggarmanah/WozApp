package id.urbanwash.wozapp.model;

import java.util.Date;

/**
 * Created by Radix on 1/2/2016.
 */
public class MessageBean extends BaseBean {

    String content;
    Date broadcastDate;
    String status;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getBroadcastDate() {
        return broadcastDate;
    }

    public void setBroadcastDate(Date broadcastDate) {
        this.broadcastDate = broadcastDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
