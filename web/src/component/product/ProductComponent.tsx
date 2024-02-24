import React, {useEffect, useState} from "react";
import {Service} from "../../service/Service";
import {Product} from "../../model/Product";
import {mockException} from "../../model/Exception";
import ExceptionComponent from "../exception/ExceptionComponent";
import {Table, TableD, TableH} from "../../style/Table";
import {PRODUCT_SERVICE_URL} from "../../service/ServiceUrl";
import {StyledButton} from "../../style/Button";
import {useNavigate, useLocation} from "react-router-dom";

const productService: Service<Product> = new Service(PRODUCT_SERVICE_URL);

const ProductComponent: React.FC = () => {
    const[products, setProducts] = useState<Product[]>([]);
    const [selectedProducts, setSelectedProducts] = useState<string[]>([]);
    const [selectAll, setSelectAll] = useState(false);
    const [exceptionProps, setExceptionProps] = useState({isOpen: false, exception: mockException});
    const navigate = useNavigate();
    const location = useLocation();

    useEffect(() => {
        (async () => {
            const fetchedProducts = await productService.findAll(false);
            if('message' in fetchedProducts) {
               setExceptionProps((prev) => ({
                   ...prev,
                   isOpen: true,
                   exception: fetchedProducts
               }));
            }else {
                setProducts(fetchedProducts);
            }
        })();
    }, []);

    const handleCheckboxChange = (name: string, checked: boolean) => {
        if (checked) {
            setSelectedProducts((prevState) => ([...prevState, name]));
        } else {
            setSelectedProducts((prevState) => prevState.filter((productName) => productName !== name));
        }
    };

    const handleSelectAllChange = (checked: boolean) => {
        if (checked) {
            const allProductNames = products.map((product) => product.name);
            setSelectedProducts(allProductNames);
        } else {
            setSelectedProducts([]);
        }
        setSelectAll(checked);
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

    return(
        <div>
            <div className={"pageName"} style={{
                display: "flex",
                justifyContent: "center",
                alignItems: "center"}
            }>
                <h1>Product List</h1>
            </div>
            <div style={{
                display: "flex",
                alignItems: "center",
                marginBottom: "10px"
            }}>
                <StyledButton onClick={() => {navigate(`${location.pathname}/create`)}}>Add</StyledButton>
                <StyledButton>Remove selected</StyledButton>
            </div>
            <Table>
            <thead>
                <tr>
                    <TableH>
                        <input type="checkbox"
                               className="checkbox"
                               checked={selectAll}
                               onChange={(e) => handleSelectAllChange(e.target.checked)}
                        />
                    </TableH>
                    <TableH>Name</TableH>
                    <TableH>Price</TableH>
                    <TableH>Manufacturer</TableH>
                    <TableH>Action</TableH>
                </tr>
                </thead>
                <tbody>
                {products.map((product) => (
                    <tr key={product.name}>
                        <TableD>
                            <input type="checkbox"
                                       className="checkbox"
                                       checked={selectedProducts.includes(product.name)}
                                       onChange={(e) => {
                                           handleCheckboxChange(product.name, e.target.checked)
                                       }}
                            />
                        </TableD>
                        <TableD>{product.name}</TableD>
                        <TableD>{product.price}</TableD>
                        <TableD>{product.manufacturer}</TableD>
                        <TableD>
                                <StyledButton>Edit</StyledButton>
                                <StyledButton>Remove</StyledButton>
                        </TableD>
                    </tr>
                ))}
                </tbody>
            </Table>
            <ExceptionComponent isOpen={exceptionProps.isOpen} exception={exceptionProps.exception} onClose={()=>{closeException()}} />
        </div>
    );
}

export default ProductComponent;