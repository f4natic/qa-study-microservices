import React from 'react';

import ProductsPage from "./ProductsPage";
import {BrowserRouter, Link, Route, Routes, Navigate} from "react-router-dom";
import {NotImplementedPage} from "./NotImplementedPage";

const MainPage: React.FC = () => {

    return (
        <BrowserRouter>
            <nav className="tabs">
                <Link to="/products">Product</Link>
                <Link to="/users" style={{marginLeft:10}}>User</Link>
                <Link to="/audit-log" style={{marginLeft:10}}>Audit Log</Link>
            </nav>
            <Routes>
                <Route path={"/"} element={<Navigate to="/products" replace />} />
                <Route path={"/products"} element={<ProductsPage />} />
                <Route path={"/users"} element={<NotImplementedPage />} />
                <Route path={"/audit-log"} element={<NotImplementedPage />} />
            </Routes>
        </BrowserRouter>
    );
};

export default MainPage;