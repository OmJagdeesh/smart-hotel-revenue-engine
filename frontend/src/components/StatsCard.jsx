import React from 'react';

const StatsCard = ({ title, value, helper }) => (
  <div className="card">
    <h3>{title}</h3>
    <p>{value}</p>
    <small>{helper}</small>
  </div>
);

export default StatsCard;
