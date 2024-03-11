package br.com.brunogodoif.contacthub.core.domain.pagination;

import br.com.brunogodoif.contacthub.core.domain.pagination.exception.InvalidPageException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
public class PaginationRequest {

    private final int pageNumber;
    private final int pageSize;

    public PaginationRequest(int pageNumber, int pageSize) {

        if (pageNumber <= 0) {
            throw new InvalidPageException("The current page value [" + pageNumber + "] is invalid! Value must be greater than zero.");
        }

        if (pageSize <= 0 || pageSize > 30) {
            throw new InvalidPageException("The size value [" + pageSize + "] is invalid! Value must be greater than zero and equal or less than one hundred.");
        }

        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

}
