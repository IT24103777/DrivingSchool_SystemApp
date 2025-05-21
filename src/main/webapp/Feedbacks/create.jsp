<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.driverschoolregistrationsystem.modules.feedback_management.FeedbackModel" %>
<%
  List<FeedbackModel> feedbacks = (List<FeedbackModel>) request.getAttribute("feedbacks");
%>
<html>
<head>
  <title>Feedback Management</title>
  <!-- Bootstrap CSS -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <!-- Font Awesome for icons -->
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
  <style>
    .star-rating {
      color: #ffc107;
      font-size: 1.2rem;
    }
    .card {
      transition: transform 0.3s;
      margin-bottom: 20px;
    }
    .card:hover {
      transform: translateY(-5px);
      box-shadow: 0 10px 20px rgba(0,0,0,0.1);
    }
    .feedback-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }
    .feedback-actions {
      display: flex;
      gap: 10px;
    }
    .loading-overlay {
      position: fixed;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      background-color: rgba(255, 255, 255, 0.7);
      display: flex;
      justify-content: center;
      align-items: center;
      z-index: 9999;
      visibility: hidden;
    }
  </style>
</head>
<body>
<!-- Loading Overlay -->
<div id="loadingOverlay" class="loading-overlay">
  <div class="spinner-border text-primary" role="status">
    <span class="visually-hidden">Loading...</span>
  </div>
</div>

<div class="container py-5">
  <div class="d-flex justify-content-between align-items-center mb-4">
    <h1 class="mb-0">Feedback Management</h1>
    <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#createFeedbackModal">
      <i class="fas fa-plus me-2"></i>Add New Feedback
    </button>
  </div>

  <div id="alertContainer"></div>

  <div class="row" id="feedbackContainer">
    <!-- Feedback cards will be loaded here -->
  </div>
</div>

<!-- Create Feedback Modal -->
<div class="modal fade" id="createFeedbackModal" tabindex="-1" aria-labelledby="createFeedbackModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header bg-primary text-white">
        <h5 class="modal-title" id="createFeedbackModalLabel">Add New Feedback</h5>
        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
        <form id="createFeedbackForm">
          <div class="mb-3">
            <label for="feedback" class="form-label">Feedback Message</label>
            <textarea class="form-control" id="feedback" name="feedback" rows="3" required></textarea>
            <div class="invalid-feedback">Please provide feedback (10-500 characters).</div>
          </div>
          <div class="mb-3">
            <label for="numOfStars" class="form-label">Rating</label>
            <select class="form-select" id="numOfStars" name="numOfStars" required>
              <option value="" selected disabled>Select rating</option>
              <option value="1">1 Star</option>
              <option value="2">2 Stars</option>
              <option value="3">3 Stars</option>
              <option value="4">4 Stars</option>
              <option value="5">5 Stars</option>
            </select>
            <div class="invalid-feedback">Please select a rating.</div>
          </div>
          <div class="mb-3">
            <label for="relatedTo" class="form-label">Related To</label>
            <select class="form-select" id="relatedTo" name="relatedTo" required>
              <option value="" selected disabled>Select category</option>
              <option value="Instructor">Instructor</option>
              <option value="Course">Course</option>
              <option value="System">System</option>
              <option value="Vehicle">Vehicle</option>
              <option value="Other">Other</option>
            </select>
            <div class="invalid-feedback">Please select what this feedback is related to.</div>
          </div>
          <div class="mb-3">
            <label for="additionalComments" class="form-label">Additional Comments (Optional)</label>
            <textarea class="form-control" id="additionalComments" name="additionalComments" rows="2"></textarea>
          </div>
        </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
        <button type="button" class="btn btn-primary" id="createFeedbackBtn">
          <span class="spinner-border spinner-border-sm d-none me-2" role="status" aria-hidden="true"></span>
          Save Feedback
        </button>
      </div>
    </div>
  </div>
</div>

<!-- Edit Feedback Modal -->
<div class="modal fade" id="editFeedbackModal" tabindex="-1" aria-labelledby="editFeedbackModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header bg-primary text-white">
        <h5 class="modal-title" id="editFeedbackModalLabel">Edit Feedback</h5>
        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
        <form id="editFeedbackForm">
          <input type="hidden" id="editId" name="id">
          <div class="mb-3">
            <label for="editFeedback" class="form-label">Feedback Message</label>
            <textarea class="form-control" id="editFeedback" name="feedback" rows="3" required></textarea>
            <div class="invalid-feedback">Please provide feedback (10-500 characters).</div>
          </div>
          <div class="mb-3">
            <label for="editNumOfStars" class="form-label">Rating</label>
            <select class="form-select" id="editNumOfStars" name="numOfStars" required>
              <option value="" disabled>Select rating</option>
              <option value="1">1 Star</option>
              <option value="2">2 Stars</option>
              <option value="3">3 Stars</option>
              <option value="4">4 Stars</option>
              <option value="5">5 Stars</option>
            </select>
            <div class="invalid-feedback">Please select a rating.</div>
          </div>
          <div class="mb-3">
            <label for="editRelatedTo" class="form-label">Related To</label>
            <select class="form-select" id="editRelatedTo" name="relatedTo" required>
              <option value="" disabled>Select category</option>
              <option value="Instructor">Instructor</option>
              <option value="Course">Course</option>
              <option value="System">System</option>
              <option value="Vehicle">Vehicle</option>
              <option value="Other">Other</option>
            </select>
            <div class="invalid-feedback">Please select what this feedback is related to.</div>
          </div>
          <div class="mb-3">
            <label for="editAdditionalComments" class="form-label">Additional Comments (Optional)</label>
            <textarea class="form-control" id="editAdditionalComments" name="additionalComments" rows="2"></textarea>
          </div>
        </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
        <button type="button" class="btn btn-primary" id="updateFeedbackBtn">
          <span class="spinner-border spinner-border-sm d-none me-2" role="status" aria-hidden="true"></span>
          Update Feedback
        </button>
      </div>
    </div>
  </div>
</div>

<!-- Delete Confirmation Modal -->
<div class="modal fade" id="deleteFeedbackModal" tabindex="-1" aria-labelledby="deleteFeedbackModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header bg-danger text-white">
        <h5 class="modal-title" id="deleteFeedbackModalLabel">Confirm Delete</h5>
        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
        <p>Are you sure you want to delete this feedback? This action cannot be undone.</p>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
        <button type="button" class="btn btn-danger" id="confirmDeleteBtn">
          <span class="spinner-border spinner-border-sm d-none me-2" role="status" aria-hidden="true"></span>
          Delete
        </button>
      </div>
    </div>
  </div>
</div>

<!-- Bootstrap Bundle with Popper -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>

<script>
  // Global variables
  const API_URL = '${pageContext.request.contextPath}/api/feedbacks';
  let currentDeleteId = null;

  // DOM elements
  const loadingOverlay = document.getElementById('loadingOverlay');
  const feedbackContainer = document.getElementById('feedbackContainer');
  const alertContainer = document.getElementById('alertContainer');

  // Form elements
  const createForm = document.getElementById('createFeedbackForm');
  const editForm = document.getElementById('editFeedbackForm');

  // Buttons with loading states
  const createFeedbackBtn = document.getElementById('createFeedbackBtn');
  const updateFeedbackBtn = document.getElementById('updateFeedbackBtn');
  const confirmDeleteBtn = document.getElementById('confirmDeleteBtn');

  // Bootstrap modal instances
  const createModal = new bootstrap.Modal(document.getElementById('createFeedbackModal'));
  const editModal = new bootstrap.Modal(document.getElementById('editFeedbackModal'));
  const deleteModal = new bootstrap.Modal(document.getElementById('deleteFeedbackModal'));

  // Show loading overlay
  function showLoading() {
    loadingOverlay.style.visibility = 'visible';
  }

  // Hide loading overlay
  function hideLoading() {
    loadingOverlay.style.visibility = 'hidden';
  }

  // Show button loading state
  function showButtonLoading(button) {
    const spinner = button.querySelector('.spinner-border');
    spinner.classList.remove('d-none');
    button.disabled = true;
  }

  // Hide button loading state
  function hideButtonLoading(button) {
    const spinner = button.querySelector('.spinner-border');
    spinner.classList.add('d-none');
    button.disabled = false;
  }

  // Show alert message
  function showAlert(message, type = 'success') {
    const alert = document.createElement('div');
    alert.className = "alert alert-" + type + " alert-dismissible fade show";
    alert.innerHTML =
            message +
            '<button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>';

    alertContainer.innerHTML = '';
    alertContainer.appendChild(alert);

    // Auto dismiss after 5 seconds
    setTimeout(() => {
      const bsAlert = new bootstrap.Alert(alert);
      bsAlert.close();
    }, 5000);
  }

  // Validate form
  function validateForm(form) {
    let isValid = true;

    // Reset validation state
    form.querySelectorAll('.form-control, .form-select').forEach(input => {
      input.classList.remove('is-invalid');
    });

    // Validate feedback text (10-500 chars)
    const feedbackInput = form.querySelector('[name="feedback"]');
    if (!feedbackInput.value || feedbackInput.value.length < 10 || feedbackInput.value.length > 500) {
      feedbackInput.classList.add('is-invalid');
      isValid = false;
    }

    // Validate rating
    const ratingInput = form.querySelector('[name="numOfStars"]');
    if (!ratingInput.value || isNaN(ratingInput.value) || ratingInput.value < 1 || ratingInput.value > 5) {
      ratingInput.classList.add('is-invalid');
      isValid = false;
    }

    // Validate related to
    const relatedToInput = form.querySelector('[name="relatedTo"]');
    if (!relatedToInput.value) {
      relatedToInput.classList.add('is-invalid');
      isValid = false;
    }

    // Validate additional comments (optional, but max 1000 chars)
    const commentsInput = form.querySelector('[name="additionalComments"]');
    if (commentsInput.value && commentsInput.value.length > 1000) {
      commentsInput.classList.add('is-invalid');
      isValid = false;
    }

    return isValid;
  }

  // Generate star rating HTML
  function generateStarRating(rating) {
    let stars = '';
    for (let i = 1; i <= 5; i++) {
      if (i <= rating) {
        stars += '<i class="fas fa-star"></i>';
      } else {
        stars += '<i class="far fa-star"></i>';
      }
    }
    return stars;
  }

  // Render feedback cards
  function renderFeedbacks(feedbacks) {
    feedbackContainer.innerHTML = '';

    if (feedbacks.length === 0) {
      feedbackContainer.innerHTML = '<div class="col-12"><div class="alert alert-info">No feedbacks found. Be the first to add one!</div></div>';
      return;
    }

    feedbacks.forEach(feedback => {
      const card = document.createElement('div');
      card.className = 'col-md-6 col-lg-4';
      card.innerHTML =
              "<div class=\"card h-100\">" +
              "<div class=\"card-header feedback-header\">" +
              "<div class=\"star-rating\">" + generateStarRating(feedback.numOfStars) + "</div>" +
              "<span class=\"badge bg-primary\">" + feedback.relatedTo + "</span>" +
              "</div>" +
              "<div class=\"card-body\">" +
              "<p class=\"card-text\">" + feedback.feedback + "</p>" +
              (feedback.additionalComments
                      ? "<p class=\"card-text text-muted small mt-2\">" + feedback.additionalComments + "</p>"
                      : "") +
              "</div>" +
              "<div class=\"card-footer d-flex justify-content-end\">" +
              "<button class=\"btn btn-sm btn-outline-primary me-2\" onclick=\"openEditModal(" + feedback.id + ")\">" +
              "<i class=\"fas fa-edit\"></i> Edit" +
              "</button>" +
              "<button class=\"btn btn-sm btn-outline-danger\" onclick=\"openDeleteModal(" + feedback.id + ")\">" +
              "<i class=\"fas fa-trash\"></i> Delete" +
              "</button>" +
              "</div>" +
              "</div>";

      feedbackContainer.appendChild(card);
    });
  }

  // Fetch all feedbacks
  async function fetchFeedbacks() {
    try {
      showLoading();
      const response = await fetch(API_URL);

      if (!response.ok) {
        throw new Error('Failed to fetch feedbacks');
      }

      const feedbacks = await response.json();
      renderFeedbacks(feedbacks);
    } catch (error) {
      showAlert(error.message, 'danger');
    } finally {
      hideLoading();
    }
  }

  // Create new feedback
  async function createFeedback() {
    if (!validateForm(createForm)) {
      return;
    }

    try {
      showButtonLoading(createFeedbackBtn);

      const formData = {
        feedback: document.getElementById('feedback').value,
        numOfStars: parseInt(document.getElementById('numOfStars').value),
        relatedTo: document.getElementById('relatedTo').value,
        additionalComments: document.getElementById('additionalComments').value
      };

      const response = await fetch(API_URL, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(formData)
      });

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.error || 'Failed to create feedback');
      }

      createModal.hide();
      createForm.reset();
      showAlert('Feedback created successfully!');
      fetchFeedbacks();
    } catch (error) {
      showAlert(error.message, 'danger');
    } finally {
      hideButtonLoading(createFeedbackBtn);
    }
  }

  // Open edit modal and populate form
  async function openEditModal(id) {
    try {
      showLoading();
      const response = await fetch(API_URL + "/" + id);



      if (!response.ok) {
        throw new Error('Failed to fetch feedback details');
      }

      const feedback = await response.json();

      document.getElementById('editId').value = feedback.id;
      document.getElementById('editFeedback').value = feedback.feedback;
      document.getElementById('editNumOfStars').value = feedback.numOfStars;
      document.getElementById('editRelatedTo').value = feedback.relatedTo;
      document.getElementById('editAdditionalComments').value = feedback.additionalComments || '';

      editModal.show();
    } catch (error) {
      showAlert(error.message, 'danger');
    } finally {
      hideLoading();
    }
  }

  // Update feedback
  async function updateFeedback() {
    if (!validateForm(editForm)) {
      return;
    }

    try {
      showButtonLoading(updateFeedbackBtn);

      const id = document.getElementById('editId').value;
      const formData = {
        id: parseInt(id),
        feedback: document.getElementById('editFeedback').value,
        numOfStars: parseInt(document.getElementById('editNumOfStars').value),
        relatedTo: document.getElementById('editRelatedTo').value,
        additionalComments: document.getElementById('editAdditionalComments').value
      };

      const response = await fetch(API_URL+/+id, {
      method: 'PUT',
              headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(formData)
    });

    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.error || 'Failed to update feedback');
    }

    editModal.hide();
    showAlert('Feedback updated successfully!');
    fetchFeedbacks();
  } catch (error) {
    showAlert(error.message, 'danger');
  } finally {
    hideButtonLoading(updateFeedbackBtn);
  }
  }

  // Open delete confirmation modal
  function openDeleteModal(id) {
    currentDeleteId = id;
    deleteModal.show();
  }

  // Delete feedback
  async function deleteFeedback() {
    if (!currentDeleteId) return;

    try {
      showButtonLoading(confirmDeleteBtn);

      const response = await fetch(API_URL+/+currentDeleteId, {
      method: 'DELETE'
    });

    if (!response.ok && response.status !== 204) {
      const errorData = await response.json();
      throw new Error(errorData.error || 'Failed to delete feedback');
    }

    deleteModal.hide();
    showAlert('Feedback deleted successfully!');
    fetchFeedbacks();
  } catch (error) {
    showAlert(error.message, 'danger');
  } finally {
    hideButtonLoading(confirmDeleteBtn);
    currentDeleteId = null;
  }
  }

  // Event listeners
  document.addEventListener('DOMContentLoaded', () => {
    // Load feedbacks on page load
    fetchFeedbacks();

    // Create feedback button
    createFeedbackBtn.addEventListener('click', createFeedback);

    // Update feedback button
    updateFeedbackBtn.addEventListener('click', updateFeedback);

    // Confirm delete button
    confirmDeleteBtn.addEventListener('click', deleteFeedback);

    // Reset form when create modal is closed
    document.getElementById('createFeedbackModal').addEventListener('hidden.bs.modal', () => {
      createForm.reset();
      createForm.querySelectorAll('.is-invalid').forEach(input => {
        input.classList.remove('is-invalid');
      });
    });

    // Reset validation when edit modal is closed
    document.getElementById('editFeedbackModal').addEventListener('hidden.bs.modal', () => {
      editForm.querySelectorAll('.is-invalid').forEach(input => {
        input.classList.remove('is-invalid');
      });
    });

    // Input event listeners to remove validation errors on input
    createForm.addEventListener('input', event => {
      if (event.target.classList.contains('form-control') || event.target.classList.contains('form-select')) {
        event.target.classList.remove('is-invalid');
      }
    });

    editForm.addEventListener('input', event => {
      if (event.target.classList.contains('form-control') || event.target.classList.contains('form-select')) {
        event.target.classList.remove('is-invalid');
      }
    });
  });
</script>
</body>
</html>