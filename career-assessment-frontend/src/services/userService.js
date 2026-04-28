import axiosInstance from './axiosInstance';

const userService = {
  getAllUsers: () => {
    return axiosInstance.get('/admin/users');
  },

  getUserById: (id) => {
    return axiosInstance.get(`/admin/users/${id}`);
  },

  getUserByEmail: (email) => {
    return axiosInstance.get(`/admin/users/email/${email}`);
  },

  deactivateUser: (id) => {
    return axiosInstance.put(`/admin/users/${id}/deactivate`);
  },

  activateUser: (id) => {
    return axiosInstance.put(`/admin/users/${id}/activate`);
  },

  updateUserRole: (id, role) => {
    return axiosInstance.put(`/admin/users/${id}/role`, null, {
      params: { role },
    });
  },

  deleteUser: (id) => {
    return axiosInstance.delete(`/admin/users/${id}`);
  },
};

export default userService;
