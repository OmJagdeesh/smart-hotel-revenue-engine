import React, { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

function UserForm() {
  const [form, setForm] = useState({ name: "", bio: "", contact: "", password: "" });
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    await axios.post("http://localhost:5000/api/users", form);
    navigate("/");
  };

  return (
    <div className="container">
      <h2>Create User</h2>
      <form onSubmit={handleSubmit}>
        <input placeholder="Name" onChange={e => setForm({...form, name: e.target.value})} />
        <input placeholder="Contact" onChange={e => setForm({...form, contact: e.target.value})} />
        <input placeholder="Password" type="password" onChange={e => setForm({...form, password: e.target.value})} />
        <textarea placeholder="Bio" onChange={e => setForm({...form, bio: e.target.value})}></textarea>
        <button className="btn" type="submit">Save</button>
      </form>
    </div>
  );
}

export default UserForm;
