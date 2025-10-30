import React, { useEffect, useState } from "react";
import { useParams, Link } from "react-router-dom";
import axios from "axios";

function UserDetail() {
  const { id } = useParams();
  const [user, setUser] = useState(null);

  useEffect(() => {
    axios.get(`http://localhost:5000/api/users/${id}`).then(res => setUser(res.data));
  }, [id]);

  const handleDelete = async () => {
    await axios.delete(`http://localhost:5000/api/users/${id}`);
    window.location.href = "/";
  };

  if (!user) return <p>Loading...</p>;

  return (
    <div className="container">
      <h1>{user.name}</h1>
      <p><b>Contact:</b> {user.contact}</p>
      <p><b>Bio:</b> {user.bio}</p>
      <button className="btn" onClick={handleDelete}>Delete</button>
      <Link to="/">← Back</Link>
    </div>
  );
}

export default UserDetail;
