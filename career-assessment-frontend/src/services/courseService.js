const STORAGE_KEY = 'career_assessment_courses';

const defaultCourses = [
  {
    id: 1,
    title: 'Career Planning 101',
    description: 'Learn how to align your strengths with job opportunities and build a roadmap for success.',
    duration: '4 weeks',
  },
  {
    id: 2,
    title: 'Resume & Interview Skills',
    description: 'Improve your resume, interview communication, and personal branding for job applications.',
    duration: '3 weeks',
  },
  {
    id: 3,
    title: 'Professional Networking',
    description: 'Build meaningful connections, create a strong profile, and grow your professional network.',
    duration: '2 weeks',
  },
];

const getStoredCourses = () => {
  const raw = localStorage.getItem(STORAGE_KEY);
  if (!raw) {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(defaultCourses));
    return defaultCourses;
  }

  try {
    return JSON.parse(raw) || defaultCourses;
  } catch (e) {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(defaultCourses));
    return defaultCourses;
  }
};

const saveCourses = (courses) => {
  localStorage.setItem(STORAGE_KEY, JSON.stringify(courses));
  return courses;
};

const getAllCourses = () => {
  return getStoredCourses();
};

const addCourse = (course) => {
  const courses = getStoredCourses();
  const maxId = courses.reduce((max, current) => Math.max(max, current.id || 0), 0);
  const newCourse = { ...course, id: maxId + 1 };
  return saveCourses([...courses, newCourse]);
};

const deleteCourse = (id) => {
  const courses = getStoredCourses().filter((course) => course.id !== id);
  return saveCourses(courses);
};

const updateCourse = (updatedCourse) => {
  const courses = getStoredCourses().map((course) => (
    course.id === updatedCourse.id ? updatedCourse : course
  ));
  return saveCourses(courses);
};

export default {
  getAllCourses,
  addCourse,
  deleteCourse,
  updateCourse,
};
