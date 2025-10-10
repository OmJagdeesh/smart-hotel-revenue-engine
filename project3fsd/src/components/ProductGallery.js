import React from 'react';
import ProductCard from './ProductCard';

const products = [
    { id: 1, name: 'Audemars Piguet', price: '$100000', imageUrl: 'https://www.watchesworld.com/wp-content/uploads/2024/11/MOBILE-BUY-CAMPAIGN@2x.jpg' },
    { id: 2, name: 'Patek Phillipe', price: '$100000', imageUrl: 'https://cdn-images.farfetch-contents.com/27/19/84/07/27198407_57085373_1000.jpg' },
    { id: 3, name: 'Vacheron Constantin', price: '$80000', imageUrl: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQZGUzv84Ey_iaNv9k_Eq1TLTUXm7yCd8y95A&s' },
    { id: 4, name: 'Rolex', price: '$40000', imageUrl: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTuCWqRz7LhCyBKf_Rkv1SdTAqIlVcvAeIhlA&s' },
    { id: 5, name: 'Richard Mille', price: '$600000', imageUrl: 'https://wristaficionado.com/cdn/shop/collections/richard-mille-rm-30-01-declutchable-rotor-rose-gold-skeleton-dial-2024-richard-mille-1125453465_2612975a-16f3-4912-99e5-9b8d18d1184e_800x800@2x.jpg?v=1740156488' },
];

function ProductGallery() {
    return (
        <div className="product-gallery">
            {products.map(product => (
                <ProductCard key={product.id} product={product} />
            ))}
        </div>
    );
}

export default ProductGallery;
