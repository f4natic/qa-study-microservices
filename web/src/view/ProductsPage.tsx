import React from 'react';
import { Product } from '../model/Product';

interface ProductPageProps {
    products: Product[];
}

const ProductsPage: React.FC = () => {
    const products: Product[] = [
      {
        id: 1,
        name: 'Product 1',
        price: 9.99,
        manufacturer: 'Manufacturer 1',
      },
      {
        id: 2,
        name: 'Product 2',
        price: 19.99,
        manufacturer: 'Manufacturer 2',
      },
    ];

    return (
        <div>
            <h1>Product List</h1>
            <table>
                <thead>
                <tr>
                    <th><input type="checkbox" className="checkbox"/></th>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Price</th>
                    <th>Manufacturer</th>
                    <th>
                        <button>Remove All Selected</button>
                    </th>
                </tr>
                </thead>
                <tbody>
                {products.map((product) => (
                    <tr key={product.id}>
                        <td><input type="checkbox" className="checkbox"/></td>
                        <td>{product.id}</td>
                        <td>{product.name}</td>
                        <td>{product.price}</td>
                        <td>{product.manufacturer}</td>
                        <td><button>Remove</button></td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
};

export default ProductsPage;