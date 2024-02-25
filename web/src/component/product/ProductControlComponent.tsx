import React, {ChangeEvent, useState} from "react";
import {useNavigate} from "react-router-dom";
import {StyledButton} from "../../style/StyledButton";
import {Product} from "../../model/Product";
import {Service} from "../../service/Service";
import ExceptionComponent from "../exception/ExceptionComponent";
import {mockException} from "../../model/Exception";

export enum WindowType {
    CREATE = 'Create',
    EDIT = 'Edit',
}

export interface ProductControlProps {
    type: WindowType;
    product: Product;
    service: Service<Product>;
}

const ProductControlComponent: React.FC<{component: ProductControlProps}> = ({component}) => {
    const navigate = useNavigate();
    const [product, setProduct] = useState({
        name: component.product.name,
        price: component.product.price === 0.0 ? "" : component.product.price.toString(),
        manufacturer: component.product.manufacturer})
    const [exceptionProps, setExceptionProps] = useState({isOpen: false, exception: mockException});

    const handleInputChange = (event: ChangeEvent<HTMLInputElement>) => {
        const { name, value } = event.target;
        const nameRegexp = /^(?:[a-zA-Z0-9-*#]+|)$/;
        const priceRegexp = /^(?:\d+\.?\d*|\d*\.\d+)$|^$/;
        const manufacturerRegexp = /^(?:[a-zA-Z-@#$]+|)$/;
        if(name === 'name') {
            if (nameRegexp.test(value)) {
                setProduct((props) => ({
                    ...props,
                    name: value
                }));
            }
        }else if(name === 'price') {
            if(priceRegexp.test(value)) {
                setProduct((props) => ({
                    ...props,
                    [name]: value
                }));
            }
        }else {
            if(manufacturerRegexp.test(value)) {
                setProduct((props) => ({
                    ...props,
                    [name]: value
                }));
            }
        }
    };


    const handleSave = () => {
        if(product.name.length === 0 || product.price.length === 0 || product.manufacturer.length === 0) {
            setExceptionProps((prev) => ({
                ...prev,
                isOpen: true,
                exception: {message: "Required fields must be filled in!"},
            }));
        }else {
            if(component.type === WindowType.EDIT) {
                component.service.update(product.name, {name: product.name, price: parseFloat(product.price), manufacturer: product.manufacturer}).then((result) => {
                    if("message" in result) {
                        setExceptionProps((prev) => ({
                            ...prev,
                            isOpen: true,
                            exception: result,
                        }));
                    }else {
                        navigate("/products");
                    }
                });
            }else {
                component.service.save({name: product.name, price: parseFloat(product.price), manufacturer: product.manufacturer}).then((result) => {
                    if("message" in result) {
                        setExceptionProps((prev) => ({
                            ...prev,
                            isOpen: true,
                            exception: result,
                        }));
                    }else {
                        navigate("/products");
                    }
                });
            }
        }
    };

    const handleCancel = () => {
        navigate('/products');
    };

    const closeException = () => {
        setExceptionProps((prev) => (
            {
                ...prev,
                isOpen: false,
                exception: mockException,
            }
        ));
    }

    return (
        <div
            style={{
                display: "flex",
                flexDirection: "column",
                alignItems: "center",
                justifyContent: "center",
            }}
        >
            <h1>{component.type} Product</h1>
            <div style={{ display: "flex", flexDirection: "column", alignItems: "flex-start" }}>
                <label style={{ textAlign: "right" }}>Name:</label>
                <input type="text" name="name" value={product.name} onChange={handleInputChange} readOnly={component.type === WindowType.EDIT ? true : false}/>
            </div>
            <div style={{ display: "flex", flexDirection: "column", alignItems: "flex-start" }}>
                <label style={{ textAlign: "right" }}>Price:</label>
                <input type="text" name="price" value={product.price} onChange={handleInputChange} />
            </div>
            <div style={{ display: "flex", flexDirection: "column", alignItems: "flex-start" }}>
                <label style={{ textAlign: "right" }}>Manufacturer:</label>
                <input
                    type="text"
                    name="manufacturer"
                    value={product.manufacturer}
                    onChange={handleInputChange}
                />
            </div>
            <div style={{display: "flex", justifyContent: "center", marginTop: "10px"}}>
                <StyledButton onClick={handleSave}>Save</StyledButton>
                <StyledButton onClick={handleCancel}>Cancel</StyledButton>
            </div>
            <ExceptionComponent isOpen={exceptionProps.isOpen} exception={exceptionProps.exception} onClose={()=>{closeException()}} />
        </div>
    );
}

export default ProductControlComponent;