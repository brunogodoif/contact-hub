import br.com.brunogodoif.contacthub.core.domain.pagination.PaginationRequest;

public class PaginationResponse {

    private Integer pageSize;
    private Integer totalPages;
    private Integer totalElements;
    private Integer currentPage;
    private Integer nextPage;
    private Integer previousPage;
    private Boolean hasNextPage;
    private Boolean hasPreviousPage;

    private PaginationResponse(PaginationBuilder paginationBuilder) {
        this.pageSize = paginationBuilder.size;
        this.totalPages = paginationBuilder.totalPages;
        this.totalElements = paginationBuilder.totalElements;
        this.currentPage = paginationBuilder.currentPage;
        this.nextPage = paginationBuilder.nextPage;
        this.previousPage = paginationBuilder.previousPage;
        this.hasNextPage = paginationBuilder.hasNext;
        this.hasPreviousPage = paginationBuilder.hasPrevious;
    }

    public static PaginationResponse getInstance(PaginationRequest paginationRequest, Integer totalElements) {
        Integer totalPages = (int) Math.ceil((double) totalElements / (double) paginationRequest.getPageSize());

        Integer nextPage = paginationRequest.getPageNumber() + 1;
        boolean hasNext = totalPages >= nextPage;
        nextPage = hasNext ? nextPage : null;

        Integer previousPage = paginationRequest.getPageNumber() - 1;
        boolean hasPrevious = previousPage >= 1;
        previousPage = hasPrevious ? previousPage : null;

        return new PaginationBuilder(paginationRequest.getPageSize(), totalPages, totalElements, paginationRequest.getPageNumber(),
                hasNext, hasPrevious).nextPage(nextPage).previousPage(previousPage).build();
    }

    public static class PaginationBuilder {
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

        public PaginationBuilder nextPage(Integer nextPage) {
            this.nextPage = nextPage;
            return this;
        }

        public PaginationBuilder previousPage(Integer previousPage) {
            this.previousPage = previousPage;
            return this;
        }

        public PaginationResponse build() {
            return new PaginationResponse(this);
        }
    }

}