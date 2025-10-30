import React, { useEffect, useState } from "react";
import axios from "axios";
import { Link } from "react-router-dom";

function UserList() {
  const [users, setUsers] = useState([]);

  useEffect(() => {
    axios.get("http://localhost:5000/api/users").then(res => setUsers(res.data));
  }, []);

  return (
    <div className="container">
      <h1>User Profiles</h1>
      <Link to="/create" className="btn">+ Add User</Link>
      <ul>
        {users.map(user => (
          <li key={user._id}>
            <Link to={`/user/${user._id}`}>{user.name}</Link>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default UserList;
