import axiosInstance from './axiosInstance';

const questionService = {
  // Public
  getAllQuestions: () => {
    return axiosInstance.get('/questions/public/all');
  },

  getQuestionsByCategory: (category) => {
    return axiosInstance.get(`/questions/public/category/${category}`);
  },

  getRandomQuestions: (count = 10) => {
    return axiosInstance.get('/questions/public/random', {
      params: { count },
    });
  },

  getQuestionById: (id) => {
    return axiosInstance.get(`/questions/admin/${id}`);
  },

  // Admin
  createQuestion: (questionDTO) => {
    return axiosInstance.post('/questions/admin', questionDTO);
  },

  updateQuestion: (id, questionDTO) => {
    return axiosInstance.put(`/questions/admin/${id}`, questionDTO);
  },

  deleteQuestion: (id) => {
    return axiosInstance.delete(`/questions/admin/${id}`);
  },

  permanentlyDeleteQuestion: (id) => {
    return axiosInstance.delete(`/questions/admin/permanent/${id}`);
  },
};

export default questionService;
