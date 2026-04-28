import axiosInstance from './axiosInstance';

const careerService = {
  // Public
  getAllCareers: () => {
    return axiosInstance.get('/careers/public/all');
  },

  getCareerById: (id) => {
    return axiosInstance.get(`/careers/public/${id}`);
  },

  searchCareerByName: (name) => {
    return axiosInstance.get('/careers/public/search', {
      params: { name },
    });
  },

  // Admin
  createCareer: (careerDTO) => {
    return axiosInstance.post('/careers/admin', careerDTO);
  },

  updateCareer: (id, careerDTO) => {
    return axiosInstance.put(`/careers/admin/${id}`, careerDTO);
  },

  deleteCareer: (id) => {
    return axiosInstance.delete(`/careers/admin/${id}`);
  },

  permanentlyDeleteCareer: (id) => {
    return axiosInstance.delete(`/careers/admin/permanent/${id}`);
  },
};

export default careerService;
