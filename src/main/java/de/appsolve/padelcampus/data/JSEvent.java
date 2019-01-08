package de.appsolve.padelcampus.data;

import de.appsolve.padelcampus.db.model.Event;
import org.joda.time.LocalDateTime;
import org.joda.time.format.ISODateTimeFormat;

public class JSEvent {

    public JSEvent(Event event) {
        this.title = event.getName();
        LocalDateTime localDateTime = event.getStartDate().toLocalDateTime(event.getStartTime());
        this.start = ISODateTimeFormat.basicDateTime().print(localDateTime);
        this.url = "/events/event/" + event.getId();
    }

    private String title;

    private String start;

    private String url;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getUrl() {
        return url;
    }
}
