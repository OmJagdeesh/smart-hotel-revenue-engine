import React from "react";
import "./App.css";

function App() {
  const products = [
    { id: 1, name: "Smartphone", price: 499, image: "https://via.placeholder.com/200x150?text=Smartphone" },
    { id: 2, name: "Laptop", price: 899, image: "https://via.placeholder.com/200x150?text=Laptop" },
    { id: 3, name: "Headphones", price: 99, image: "https://via.placeholder.com/200x150?text=Headphones" },
    { id: 4, name: "Smart Watch", price: 199, image: "https://via.placeholder.com/200x150?text=Smart+Watch" },
    { id: 5, name: "Camera", price: 599, image: "https://via.placeholder.com/200x150?text=Camera" },
    { id: 6, name: "Keyboard", price: 49, image: "https://via.placeholder.com/200x150?text=Keyboard" },
  ];

  return (
    <div className="container">
      <h1>Product Gallery</h1>
      <div className="gallery">
        {products.map((p) => (
          <div className="card" key={p.id}>
            <img src={p.image} alt={p.name} />
            <h3>{p.name}</h3>
            <p>${p.price}</p>
          </div>
        ))}
      </div>
    </div>
  );
}

export default App;
