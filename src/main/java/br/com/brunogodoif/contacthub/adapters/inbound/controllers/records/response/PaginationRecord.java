package br.com.brunogodoif.contacthub.adapters.inbound.controllers.records.response;


import br.com.brunogodoif.contacthub.core.domain.pagination.PaginationResponse;

public record PaginationRecord(
        Integer pageSize,
        Integer totalPages,
        Integer totalElements,
        Integer currentPage,
        Integer nextPage,
        Integer previousPage,
        Boolean hasNextPage,
        Boolean hasPreviousPage
) {
    public static PaginationRecord fromDomain(PaginationResponse paginationResponse) {
        return new PaginationRecord(
                paginationResponse.getPageSize(),
                paginationResponse.getTotalPages(),
                paginationResponse.getTotalElements(),
                paginationResponse.getCurrentPage(),
                paginationResponse.getNextPage(),
                paginationResponse.getPreviousPage(),
                paginationResponse.getHasNextPage(),
                paginationResponse.getHasPreviousPage()
        );
    }
}
