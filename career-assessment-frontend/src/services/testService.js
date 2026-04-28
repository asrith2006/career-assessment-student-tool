import axiosInstance from './axiosInstance';

const testService = {
  submitTest: (testName, responses) => {
    return axiosInstance.post('/tests/submit', {
      testName,
      responses,
    });
  },

  getMyResults: () => {
    return axiosInstance.get('/tests/my-results');
  },

  getTestResult: (resultId) => {
    return axiosInstance.get(`/tests/result/${resultId}`);
  },

  getAllResults: () => {
    return axiosInstance.get('/tests/admin/all');
  },
};

export default testService;
