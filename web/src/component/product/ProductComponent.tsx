import React, {useEffect, useState} from "react";
import {Service} from "../../service/Service";
import {Product} from "../../model/Product";
import styled from "styled-components";
import {mockException} from "../../model/Exception";
import ExceptionComponent from "../exception/ExceptionComponent";

const Table = styled.table`
    width: 100%;
    border-collapse: collapse;
`;

const TableH = styled.th`
    padding: 8px;
    text-align: left;
    border-bottom: 1px solid #ddd;
    background-color: #f2f2f2;
`;
const TableD = styled.td`
    padding: 8px;
    text-align: left;
    border-bottom: 1px solid #ddd;
`;

const testProducts: Product[] = [
    {
        name: 'Product 1',
        price: 9.99,
        manufacturer: 'Manufacturer 1',
    },
    {
        name: 'Product 2',
        price: 19.99,
        manufacturer: 'Manufacturer 2',
    },
];

const productService: Service<Product> = new Service(testProducts);

const ProductComponent: React.FC = () => {
    const[products, setProducts] = useState<Product[]>([]);
    const [selectedProducts, setSelectedProducts] = useState<string[]>([]);
    const [selectAll, setSelectAll] = useState(false);
    const [exceptionProps, setExceptionProps] = useState({isOpen: false, exception: mockException});

    useEffect(() => {
        (async () => {
            const fetchedProducts = await productService.findAll(true);
            if('message' in fetchedProducts) {
                console.log("Error:", fetchedProducts.message);
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

    return(
        <div>
            <div className={"pageName"} style={{
                display: "flex",
                justifyContent: "center",
                alignItems: "center"}
            }>
                <h1>Product List</h1>
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
                    <TableH>
                        <button>Remove All Selected</button>
                    </TableH>
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
                            <button>Remove</button>
                        </TableD>
                    </tr>
                ))}
                </tbody>
            </Table>

            <ExceptionComponent isOpen={exceptionProps.isOpen} exception={exceptionProps.exception} onClose={()=>{}} />
        </div>
    );
}

export default ProductComponent;