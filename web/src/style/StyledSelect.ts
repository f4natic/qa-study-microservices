import styled from "styled-components";

export const StyledSelect = styled.select`
    padding: 8px 16px;
    font-size: 14px;
    border: 2px solid grey;
    border-radius: 4px;
    background-color: #fff;
    color: #333;
    
    &:focus {
        border-color: lightgray;
        outline: none;
    }
`;