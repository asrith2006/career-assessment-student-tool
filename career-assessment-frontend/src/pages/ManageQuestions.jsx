import React, { useState, useEffect } from 'react';
import questionService from '../services/questionService';

const ManageQuestions = () => {
  const [questions, setQuestions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showForm, setShowForm] = useState(false);
  const [editingId, setEditingId] = useState(null);
  const [formData, setFormData] = useState({
    questionText: '',
    category: '',
    difficulty: 1,
    description: '',
    answers: [
      { answerText: '', isCorrect: false },
      { answerText: '', isCorrect: false },
      { answerText: '', isCorrect: false },
      { answerText: '', isCorrect: false },
    ],
  });

  useEffect(() => {
    fetchQuestions();
  }, []);

  const fetchQuestions = async () => {
    try {
      const response = await questionService.getAllQuestions();
      setQuestions(response.data.data);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const validAnswers = formData.answers.filter((answer) => answer.answerText.trim() !== '');
    const hasCorrect = validAnswers.some((answer) => answer.isCorrect);

    if (validAnswers.length < 2) {
      alert('Please provide at least two answer options.');
      return;
    }

    if (!hasCorrect) {
      alert('Please mark one answer as the correct option.');
      return;
    }

    try {
      if (editingId) {
        await questionService.updateQuestion(editingId, {
          ...formData,
          answers: validAnswers.map((answer, index) => ({
            ...answer,
            displayOrder: index + 1,
            active: true,
          })),
        });
      } else {
        await questionService.createQuestion({
          ...formData,
          answers: validAnswers.map((answer, index) => ({
            ...answer,
            displayOrder: index + 1,
            active: true,
          })),
        });
      }
      handleCancel();
      fetchQuestions();
    } catch (err) {
      console.error(err);
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm('Are you sure?')) {
      try {
        await questionService.deleteQuestion(id);
        fetchQuestions();
      } catch (err) {
        console.error(err);
      }
    }
  };

  const handleEdit = async (id) => {
    try {
      const response = await questionService.getQuestionById(id);
      const question = response.data.data;
      setFormData({
        questionText: question.questionText,
        category: question.category,
        difficulty: question.difficulty,
        description: question.description || '',
        answers: question.answers.map((answer, index) => ({
          answerText: answer.answerText,
          isCorrect: answer.isCorrect,
        })).concat(
          Array.from({ length: Math.max(0, 4 - question.answers.length) }, () => ({
            answerText: '',
            isCorrect: false,
          }))
        ),
      });
      setEditingId(id);
      setShowForm(true);
    } catch (err) {
      console.error(err);
    }
  };

  const handleCancel = () => {
    setShowForm(false);
    setEditingId(null);
    setFormData({
      questionText: '',
      category: '',
      difficulty: 1,
      description: '',
      answers: [
        { answerText: '', isCorrect: false },
        { answerText: '', isCorrect: false },
        { answerText: '', isCorrect: false },
        { answerText: '', isCorrect: false },
      ],
    });
  };

  const handleAnswerChange = (index, value) => {
    const updatedAnswers = [...formData.answers];
    updatedAnswers[index].answerText = value;
    setFormData({ ...formData, answers: updatedAnswers });
  };

  const handleCorrectAnswer = (index) => {
    const updatedAnswers = formData.answers.map((answer, idx) => ({
      ...answer,
      isCorrect: idx === index,
    }));
    setFormData({ ...formData, answers: updatedAnswers });
  };

  if (loading) return <div className="spinner"></div>;

  return (
    <div className="container mt-3">
      <h1>Manage Questions</h1>

      <button
        onClick={showForm ? handleCancel : () => setShowForm(true)}
        className="btn btn-primary mb-2"
        aria-expanded={showForm}
        aria-controls="manage-question-form"
      >
        {showForm ? (editingId ? 'Cancel Editing' : 'Cancel') : 'Add Question'}
      </button>

      {showForm && (
        <form id="manage-question-form" onSubmit={handleSubmit} className="card mb-3">
          <div className="form-group">
            <h2>{editingId ? 'Edit Question' : 'New Question'}</h2>
          </div>
          <div className="form-group">
            <label>Question Text</label>
            <textarea
              value={formData.questionText}
              onChange={(e) => setFormData({ ...formData, questionText: e.target.value })}
              required
            ></textarea>
          </div>
          <div className="form-group">
            <label>Description</label>
            <textarea
              value={formData.description}
              onChange={(e) => setFormData({ ...formData, description: e.target.value })}
            ></textarea>
          </div>
          <div className="form-group">
            <label>Category</label>
            <input
              type="text"
              value={formData.category}
              onChange={(e) => setFormData({ ...formData, category: e.target.value })}
              required
            />
          </div>
          <div className="form-group">
            <label>Difficulty</label>
            <select
              value={formData.difficulty}
              onChange={(e) => setFormData({ ...formData, difficulty: parseInt(e.target.value) })}
            >
              <option value={1}>Easy</option>
              <option value={2}>Medium</option>
              <option value={3}>Hard</option>
            </select>
          </div>
          <div className="form-group">
            <label>Answer Options</label>
            {formData.answers.map((answer, index) => (
              <div key={index} className="answer-form-row">
                <input
                  type="radio"
                  name="correctAnswer"
                  checked={answer.isCorrect}
                  onChange={() => handleCorrectAnswer(index)}
                />
                <input
                  type="text"
                  value={answer.answerText}
                  onChange={(e) => handleAnswerChange(index, e.target.value)}
                  placeholder={`Answer option ${index + 1}`}
                  required={index < 2}
                />
              </div>
            ))}
            <small className="form-text text-muted">
              Mark the correct answer and add at least two options.
            </small>
          </div>
          <button
            id="manage-question-submit"
            type="submit"
            className="btn btn-success"
            aria-label={editingId ? 'Update Question' : 'Save Question'}
          >
            {editingId ? 'Update Question' : 'Save Question'}
          </button>
        </form>
      )}

      <table>
        <thead>
          <tr>
            <th>Question</th>
            <th>Category</th>
            <th>Difficulty</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {questions.map((q) => (
            <tr key={q.id}>
              <td>{q.questionText.substring(0, 50)}...</td>
              <td>{q.category}</td>
              <td>{q.difficulty === 1 ? 'Easy' : q.difficulty === 2 ? 'Medium' : 'Hard'}</td>
              <td>
                <button onClick={() => handleEdit(q.id)} className="btn btn-secondary mr-1">Edit</button>
                <button onClick={() => handleDelete(q.id)} className="btn btn-danger">Delete</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default ManageQuestions;
