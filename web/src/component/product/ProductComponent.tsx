import React, {useEffect, useState} from "react";
import {Service} from "../../service/Service";
import {mockProduct, Product} from "../../model/Product";
import {mockException} from "../../model/Exception";
import ExceptionComponent from "../exception/ExceptionComponent";
import {Table, TableD, TableH} from "../../style/Table";
import {PRODUCT_SERVICE_URL} from "../../service/ServiceUrl";
import {StyledButton} from "../../style/StyledButton";
import {useLocation, useNavigate} from "react-router-dom";
import {ProductControlProps, WindowType} from "./ProductControlComponent";
import Pagination from "../pagination/Pagination";

const productService: Service<Product> = new Service(PRODUCT_SERVICE_URL);

export interface ProductComponentProps {
    control: (productProps: ProductControlProps) => void;
}

const ProductComponent: React.FC<{componentProps: ProductComponentProps}> = ({componentProps}) => {
    const[products, setProducts] = useState<Product[]>([]);
    const [selectedProducts, setSelectedProducts] = useState<string[]>([]);
    const [selectAll, setSelectAll] = useState(false);
    const [exceptionProps, setExceptionProps] = useState({isOpen: false, exception: mockException});
    const navigate = useNavigate();
    const location = useLocation();

    // Pagination state
    const [currentPage, setCurrentPage] = useState(0);
    const [itemsPerPage, setItemsPerPage] = useState(5);
    const [totalItems, setTotalItems] = useState(0);
    const totalPages = Math.ceil(totalItems / itemsPerPage);
    const pageNumbers = Array.from({ length: totalPages }, (_, index) => index + 1);

    useEffect(() => {
        (async () => {
            const fetchedProducts = await productService.findAll(currentPage, itemsPerPage);
            const total = await productService.getTotal();
            if('message' in fetchedProducts) {
               setExceptionProps((prev) => ({
                   ...prev,
                   isOpen: true,
                   exception: fetchedProducts
               }));
               return;
            }
            if(typeof total !== 'number') {
                setExceptionProps((prev) => ({
                    ...prev,
                    isOpen: true,
                    exception: total
                }));
                return;
            }
            setTotalItems(total);
            setProducts(fetchedProducts.content);
        })();
    }, [currentPage, itemsPerPage]);

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

    const handlePageChange = (page: number) => {
        setCurrentPage(page - 1);
    };

    const handleItemsPerPageChange = (event: React.ChangeEvent<HTMLSelectElement>) => {
        setItemsPerPage(Number(event.target.value));
        setCurrentPage(0);
    };
    const goToPreviousPage = () => {
        setCurrentPage((prevPage) => prevPage - 1);
    };

    const goToNextPage = () => {
        setCurrentPage((prevPage) => prevPage + 1);
    };

    const handleCreateButton = () => {
        navigate(`${location.pathname}/create`)
        componentProps.control({type: WindowType.CREATE, product: mockProduct, service: productService});
    }

    const handleEditButton = (product: Product) => {
        navigate(`${location.pathname}/${product.name}`)
        componentProps.control({type: WindowType.EDIT, product: product, service: productService});
    }

    const handleRemove = async (name: string) => {
        const result = await productService.delete(name);
        if(result !== void 0) {
            setExceptionProps((prev) => ({
                ...prev,
                isOpen: true,
                exception: result
            }));
        }else {
            window.location.reload();
        }
    };

    const handleRemoveSelected = async () => {
        selectedProducts.map((name) => handleRemove(name))
        setSelectedProducts([]);
        setSelectAll(false);
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
            <div style={{
                display: "flex",
                alignItems: "center",
                marginBottom: "10px"
            }}>
                <StyledButton onClick={handleCreateButton}>Add</StyledButton>
                <StyledButton onClick={handleRemoveSelected}>Remove selected</StyledButton>
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
                                <StyledButton onClick={() => handleEditButton(product)}>Edit</StyledButton>
                                <StyledButton onClick={() => handleRemove(product.name)}>Remove</StyledButton>
                        </TableD>
                    </tr>
                ))}
                </tbody>
            </Table>
            <Pagination paginationProps={
                {
                    currentPage:currentPage,
                    itemsPerPage: itemsPerPage,
                    pageNumbers: pageNumbers,
                    totalPages: totalPages,
                    handlePageChange: handlePageChange,
                    handleItemsPerPageChange: handleItemsPerPageChange,
                    goToPreviousPage:goToPreviousPage,
                    goToNextPage: goToNextPage,
                }}
            />
            <ExceptionComponent isOpen={exceptionProps.isOpen} exception={exceptionProps.exception} onClose={() => {
                closeException()
            }}/>
        </div>
    );
}

export default ProductComponent;