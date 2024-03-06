import styled from "styled-components";
import {Link} from "react-router-dom";

export const TabContainer = styled.nav`
    display: flex;
    justify-content: center;
`;

export const Tab = styled(Link)`
    margin-left: 10px;
    padding: 10px 20px;
    background-color: darkgray;
    border: none;
    border-radius: 4px;
    text-decoration: none;
    color: #333;
    cursor: pointer;

    &:hover {
        background-color: lightgray;
    }
`;