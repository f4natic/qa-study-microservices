import React from "react";
import {StyledSelect} from "../../style/StyledSelect";
import {StyledButton} from "../../style/StyledButton";

interface PaginationProps {
    itemsPerPage: number;
    currentPage: number;
    pageNumbers: number[];
    totalPages: number;
    handlePageChange: (page: number) => void;
    handleItemsPerPageChange: (event: React.ChangeEvent<HTMLSelectElement>) => void;
    goToPreviousPage: () => void;
    goToNextPage: () => void;
}
const Pagination: React.FC<{ paginationProps: PaginationProps }> = ({paginationProps}) => {
    return (
        <div style={{marginTop: "10px"}}>
            <StyledSelect value={paginationProps.itemsPerPage} onChange={paginationProps.handleItemsPerPageChange}>
                <option value={5}>5</option>
                <option value={25}>25</option>
                <option value={100}>100</option>
            </StyledSelect>
            <StyledButton onClick={paginationProps.goToPreviousPage} disabled={paginationProps.currentPage === 0}>
                &lt;
            </StyledButton>
            {paginationProps.pageNumbers.map((pageNumber) => (
                <StyledButton
                    key={pageNumber}
                    onClick={() => paginationProps.handlePageChange(pageNumber)}
                    style={{marginRight: "5px", fontWeight: pageNumber === paginationProps.currentPage + 1 ? "bold" : "normal"}}
                >
                    {pageNumber}
                </StyledButton>
            ))}
            <StyledButton onClick={paginationProps.goToNextPage} disabled={paginationProps.currentPage === paginationProps.totalPages - 1}>
                &gt;
            </StyledButton>
        </div>
    );
}

export default Pagination;