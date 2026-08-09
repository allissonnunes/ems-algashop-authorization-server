package br.dev.allissonnunes.algashop.authorization_server.application.user.query;

import lombok.Builder;
import org.springframework.data.domain.Page;

import java.util.List;

@Builder
public record PageModel<T>(
        int number,
        int size,
        int totalPages,
        long totalElements,
        List<T> content
) {

    public static <T> PageModel<T> of(final Page<T> page) {
        return PageModel.<T>builder()
                .number(page.getNumber())
                .size(page.getSize())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .content(page.getContent())
                .build();
    }

}
