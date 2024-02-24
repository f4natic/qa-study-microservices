import React from 'react';

import {BrowserRouter, Link, Route, Routes, Navigate} from "react-router-dom";
import ImplementationMockComponent from "./mock/ImplementationMockComponent";
import HeaderComponent from "./header/HeaderComponent";
import ProductComponent from "./product/ProductComponent";
import ProductControlComponent from "./product/ProductControlComponent";

const MainComponent: React.FC = () => {

    return (
        <BrowserRouter>
            <HeaderComponent />
            <Routes>
                <Route path={"/"} element={<Navigate to="/products" replace />} />
                <Route path={"/products"} element={<ProductComponent />} />
                <Route path={"/products/create"} element={<ProductControlComponent />} />
                <Route path={"/products/*"} element={<ProductControlComponent />} />
                <Route path={"/clients"} element={<ImplementationMockComponent />} />
                <Route path={"/orders"} element={<ImplementationMockComponent />} />
                <Route path={"/audit"} element={<ImplementationMockComponent />} />
            </Routes>
        </BrowserRouter>
    );
};

export default MainComponent;