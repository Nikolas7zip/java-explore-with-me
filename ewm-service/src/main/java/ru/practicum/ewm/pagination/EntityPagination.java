package ru.practicum.ewm.pagination;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class EntityPagination {

    private int from;
    private int size;
    private int page;
    private final Sort sort;

    public EntityPagination(int from, int size, Sort sort) {
        if (from < 0) {
            throw new IllegalArgumentException("From index must not be less than zero!");
        }

        if (size < 1) {
            throw new IllegalArgumentException("Size must not be less than one!");
        }
        this.from = from;
        this.size = size;
        this.page = from / size;
        this.sort = sort;
    }

    public int getPageNumber() {
        return page;
    }

    public int getFrom() {
        return from;
    }

    public long getSize() {
        return size;
    }

    public Sort getSort() {
        return sort;
    }

    public Pageable getPageable() {
        return PageRequest.of(page, size, sort);
    }

    @Override
    public String toString() {
        return "EntityPagination{" +
                "from=" + from +
                ", size=" + size +
                ", page=" + page +
                ", sort=" + sort +
                '}';
    }
}
