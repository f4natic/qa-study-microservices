import { Link } from "react-router-dom";
import React from "react";
import styled from "styled-components";

const Header = styled.header`
    display: flex;
    justify-content: center;
    align-items: center;
    height: 50px;
    background-color: gray;
`;

const TabContainer = styled.nav`
    display: flex;
    justify-content: center;
`;

const Tab = styled(Link)`
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

const HeaderComponent: React.FC = () => {
    return (
        <Header>
            <TabContainer>
                <Tab to={"/products"}>Products</Tab>
                <Tab to={"/clients"}>Clients</Tab>
                <Tab to={"/orders"}>Orders</Tab>
                <Tab to={"/audit"}>Audit</Tab>
            </TabContainer>
        </Header>
    );
};

export default HeaderComponent;