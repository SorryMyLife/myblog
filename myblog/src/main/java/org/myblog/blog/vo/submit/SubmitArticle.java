package org.myblog.blog.vo.submit;

import lombok.Data;

@Data
public class SubmitArticle {
    String title,text,html,tags;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public SubmitArticle(String title, String text, String html, String tags) {
        this.title = title;
        this.text = text;
        this.html = html;
        this.tags = tags;
    }
}
