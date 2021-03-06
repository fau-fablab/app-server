package de.fau.fablab.app.server.core.drupal;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.LinkedList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RSSFeedChannel {

    private String title;
//    BUG: https://github.com/FasterXML/jackson-dataformat-xml/issues/65
//    Jackson cannot handle that there is a <atom:link> and a <link> element at the same time. We are only interested in the latter.
//     private String link;
    private String description;
    private String language;

    @JacksonXmlElementWrapper(useWrapping = false)
    private List<RSSFeedItem> item = new LinkedList<>();

    public RSSFeedChannel() {}

    public RSSFeedChannel(String title, /*String link,*/ String description, String language, List<RSSFeedItem> item) {
        this.title = title;
//         this.link = link;
        this.description = description;
        this.language = language;
        this.item = item;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

//     public String getLink() {
//         return link;
//     }

//     public void setLink(String link) {
//         this.link = link;
//     }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public List<RSSFeedItem> getItem() {
        return item;
    }

    public void setItem(RSSFeedItem item) {
        this.item.add(item);
    }

    public String toString() {
        return getTitle();
    }
}
