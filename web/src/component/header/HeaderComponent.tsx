import React from "react";
import {Header} from "../../style/Header";
import {Tab, TabContainer} from "../../style/Tab";

const HeaderComponent: React.FC = () => {
    return (
        <Header>
            <TabContainer>
                <Tab to={"/products"}>Products</Tab>
                <Tab to={"/customer"}>Customers</Tab>
                <Tab to={"/orders"}>Orders</Tab>
                <Tab to={"/audit"}>Audit</Tab>
            </TabContainer>
        </Header>
    );
};

export default HeaderComponent;