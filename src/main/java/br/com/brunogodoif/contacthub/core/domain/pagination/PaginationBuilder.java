package br.com.brunogodoif.contacthub.core.domain.pagination;

static class PaginationBuilder {
    private Integer size;
    private Integer totalPages;
    private Integer totalElements;
    private Integer currentPage;
    private Integer nextPage;
    private Integer previousPage;
    private Boolean hasNext;
    private Boolean hasPrevious;

    public PaginationBuilder(Integer size, Integer totalPages, Integer totalElements, Integer currentPage, Boolean hasNext, Boolean hasPrevious) {
        this.size = size;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.currentPage = currentPage;
        this.hasNext = hasNext;
        this.hasPrevious = hasPrevious;
    }

    public PaginationResponse.PaginationBuilder nextPage(Integer nextPage) {
        this.nextPage = nextPage;
        return this;
    }

    public PaginationResponse.PaginationBuilder previousPage(Integer previousPage) {
        this.previousPage = previousPage;
        return this;
    }

    public PaginationResponse build() {
        return new PaginationResponse(this);
    }
}