import React, {ChangeEvent, useState} from "react";
import {useNavigate} from "react-router-dom";

const ProductControlComponent: React.FC = () => {
    const navigate = useNavigate();
    const [product, setProduct] = useState({name: '', price: '', manufacturer: ''})

    const handleInputChange = (event: ChangeEvent<HTMLInputElement>) => {
        const { name, value } = event.target;
        const nameRegexp = /^(?:[a-zA-Z0-9]+|)$/;
        const priceRegexp = /^(?:\d+\.?\d*|\d*\.\d+)$|^$/;
        const manufacturerRegexp = /^(?:[a-zA-Z0-9]+|)$/;
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
        console.log("Save clicked");
    };

    const handleCancel = () => {
        navigate('/products');
    };

    return (
        <div
            style={{
                display: "flex",
                flexDirection: "column",
                alignItems: "center",
                justifyContent: "center",
            }}
        >
            <h1>Create Product</h1>
            <div>
                <label>Name:</label>
                <input type="text" name="name" value={product.name} onChange={handleInputChange}/>
            </div>
            <div>
                <label>Price:</label>
                <input type="text" name="price" value={product.price} onChange={handleInputChange}/>
            </div>
            <div>
                <label>Manufacturer:</label>
                <input
                    type="text"
                    name="manufacturer"
                    value={product.manufacturer}
                    onChange={handleInputChange}
                />
            </div>
            <div>
                <button onClick={handleSave}>Save</button>
                <button onClick={handleCancel}>Cancel</button>
            </div>
        </div>
    );
}

export default ProductControlComponent;