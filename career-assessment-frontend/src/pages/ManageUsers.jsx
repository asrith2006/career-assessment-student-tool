import React, { useState, useEffect } from 'react';
import userService from '../services/userService';

const ManageUsers = () => {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchUsers();
  }, []);

  const fetchUsers = async () => {
    try {
      const response = await userService.getAllUsers();
      setUsers(response.data.data);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleDeactivate = async (id) => {
    try {
      await userService.deactivateUser(id);
      fetchUsers();
    } catch (err) {
      console.error(err);
    }
  };

  const handleActivate = async (id) => {
    try {
      await userService.activateUser(id);
      fetchUsers();
    } catch (err) {
      console.error(err);
    }
  };

  if (loading) return <div className="spinner"></div>;

  return (
    <div className="container mt-3">
      <h1>Manage Users</h1>

      <table>
        <thead>
          <tr>
            <th>Name</th>
            <th>Email</th>
            <th>Role</th>
            <th>Status</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {users.map((user) => (
            <tr key={user.id}>
              <td>{user.fullName}</td>
              <td>{user.email}</td>
              <td>{user.role}</td>
              <td>{user.active ? 'Active' : 'Inactive'}</td>
              <td>
                {user.active ? (
                  <button onClick={() => handleDeactivate(user.id)} className="btn btn-warning">
                    Deactivate
                  </button>
                ) : (
                  <button onClick={() => handleActivate(user.id)} className="btn btn-success">
                    Activate
                  </button>
                )}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default ManageUsers;
