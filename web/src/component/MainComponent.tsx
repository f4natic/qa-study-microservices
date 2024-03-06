import React, {useState} from 'react';

import {BrowserRouter, Navigate, Route, Routes} from "react-router-dom";
import ImplementationMockComponent from "./mock/ImplementationMockComponent";
import HeaderComponent from "./header/HeaderComponent";
import ProductComponent from "./product/ProductComponent";
import ProductControlComponent, {ProductControlProps, WindowType} from "./product/ProductControlComponent";
import {mockProduct} from "../model/Product";
import {Service} from "../service/Service";

const MainComponent: React.FC = () => {
    const [productControlProps, setProductControlProps] = useState<ProductControlProps>({service: new Service(""), type:WindowType.CREATE, product: mockProduct});

    const handleControlProduct = (productControlProps: ProductControlProps) => {
        setProductControlProps(productControlProps);
    }

    return (
        <BrowserRouter>
            <HeaderComponent />
            <Routes>
                <Route path={"/"} element={<Navigate to="/products" replace />} />
                <Route path={"/products"} element={<ProductComponent componentProps={{control: handleControlProduct}}/>} />
                <Route path={"/products/create"} element={<ProductControlComponent component={productControlProps} />} />
                <Route path={"/products/*"} element={<ProductControlComponent component={productControlProps} />} />
                <Route path={"/customer"} element={<ImplementationMockComponent />} />
                <Route path={"/orders"} element={<ImplementationMockComponent />} />
                <Route path={"/audit"} element={<ImplementationMockComponent />} />
            </Routes>
        </BrowserRouter>
    );
};

export default MainComponent;