package ru.practicum.ewm.dto;

public class ViewCount {
    private String uri;
    private Long total;

    public ViewCount(String uri, Long total) {
        this.uri = uri;
        this.total = total;
    }

    public String getUri() {
        return uri;
    }

    public Long getTotal() {
        return total;
    }
}
