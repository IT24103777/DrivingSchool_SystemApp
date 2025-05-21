<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.Driving_School_System.modules.instructor_management.InstructorModel" %>
<%
  InstructorModel instructor = (InstructorModel) request.getAttribute("instructor");
%>
<html>
<head>
  <title>Edit Instructor</title>
  <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
</head>
<body class="bg-gray-50">
<div class="container mx-auto px-4 py-8 max-w-3xl">
  <div class="flex items-center mb-6">
    <a href="${pageContext.request.contextPath}/instructor-list" class="mr-4 text-blue-600 hover:text-blue-800">
      <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 19l-7-7m0 0l7-7m-7 7h18" />
      </svg>
    </a>
    <h1 class="text-2xl font-bold text-gray-800">Edit Instructor</h1>
  </div>

  <div id="alert" class="hidden mb-6"></div>
  <div id="loading" class="hidden text-center py-8">
    <svg class="animate-spin mx-auto h-8 w-8 text-blue-600" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
      <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
      <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
    </svg>
  </div>

  <form id="instructorForm" class="hidden bg-white shadow rounded-lg p-6">
    <input type="hidden" id="id" name="id" >
    <div class="grid grid-cols-1 gap-6">
      <!-- Name Field -->
      <div>
        <label for="name" class="block text-sm font-medium text-gray-700 mb-1">Full Name</label>
        <input type="text" id="name" name="name" required
               class="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500"
               pattern="^[a-zA-Z ]{3,50}$"
               title="Name should be 3-50 alphabetic characters"
        >
      </div>

      <!-- Instructor Number Field -->
      <div>
        <label for="instructorNumber" class="block text-sm font-medium text-gray-700 mb-1">Instructor Number</label>
        <input type="text" id="instructorNumber" name="instructorNumber" required
               class="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500"
               pattern="^INS\d{4}$"
               title="Instructor number should be in format INS0000"
        >
      </div>

      <!-- Contact Number Field -->
      <div>
        <label for="contactNumber" class="block text-sm font-medium text-gray-700 mb-1">Contact Number</label>
        <input type="tel" id="contactNumber" name="contactNumber" required
               class="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500"
               pattern="^[0-9]{10}$"
               title="10 digit phone number"
        >
      </div>

      <!-- Email Field -->
      <div>
        <label for="email" class="block text-sm font-medium text-gray-700 mb-1">Email</label>
        <input type="email" id="email" name="email" required
               class="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500"
        >
      </div>

      <!-- NIC Number Field -->
      <div>
        <label for="nicNumber" class="block text-sm font-medium text-gray-700 mb-1">NIC Number</label>
        <input type="text" id="nicNumber" name="nicNumber" required
               class="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500"
               pattern="^([0-9]{9}[vVxX]|[0-9]{12})$"
               title="Valid NIC number (old or new format)"
        >
      </div>

      <!-- Age Field -->
      <div>
        <label for="age" class="block text-sm font-medium text-gray-700 mb-1">Age</label>
        <input type="number" id="age" name="age" required min="18" max="70"
               class="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500"
        >
      </div>

      <!-- Address Field -->
      <div>
        <label for="address" class="block text-sm font-medium text-gray-700 mb-1">Address</label>
        <textarea id="address" name="address" required
                  class="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500"></textarea>
      </div>

      <div class="flex justify-end space-x-3">
        <button type="button" onclick="window.location.href='${pageContext.request.contextPath}/instructor-list'"
                class="px-4 py-2 border border-gray-300 rounded-md text-sm font-medium text-gray-700 bg-white hover:bg-gray-50">
          Cancel
        </button>
        <button type="submit" id="submitBtn"
                class="px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500">
          Update Instructor
        </button>
        <button type="button" id="loadingBtn" disabled
                class="hidden px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-blue-400">
          <svg class="animate-spin -ml-1 mr-2 h-4 w-4 text-white inline" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
            <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
            <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
          </svg>
          Processing...
        </button>
      </div>
    </div>
  </form>
</div>

<script>
  document.addEventListener('DOMContentLoaded', async () => {
    const urlParams = new URLSearchParams(window.location.search);
    const instructorId = urlParams.get('id');
    const alertDiv = document.getElementById('alert');
    const loadingDiv = document.getElementById('loading');
    const formDiv = document.getElementById('instructorForm');

    alertDiv.classList.add('hidden');

    if (!instructorId || isNaN(instructorId)) {
      showAlert('Invalid instructor ID provided', 'error');
      loadingDiv.classList.add('hidden');
      return;
    }

    try {
      loadingDiv.classList.remove('hidden');
      formDiv.classList.add('hidden');

      const response = await fetch(`${pageContext.request.contextPath}/api/instructors/`+instructorId);

      if (!response.ok) {
        const error = await response.json();
        throw new Error(error.error || 'Failed to fetch instructor data');
      }

      const instructor = await response.json();

      if (!instructor || !instructor.id) {
        throw new Error('Invalid instructor data received');
      }

      // Populate form fields
      document.getElementById('id').value = instructor.id;
      document.getElementById('name').value = instructor.name;
      document.getElementById('instructorNumber').value = instructor.instructorNumber;
      document.getElementById('contactNumber').value = instructor.contactNumber;
      document.getElementById('email').value = instructor.email;
      document.getElementById('nicNumber').value = instructor.nicNumber;
      document.getElementById('age').value = instructor.age;
      document.getElementById('address').value = instructor.address;

      formDiv.classList.remove('hidden');
    } catch (error) {
      showAlert(error.message, 'error');
      document.getElementById('submitBtn').disabled = true;
    } finally {
      loadingDiv.classList.add('hidden');
    }
  });

  document.getElementById('instructorForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const submitBtn = document.getElementById('submitBtn');
    const loadingBtn = document.getElementById('loadingBtn');
    const alertDiv = document.getElementById('alert');

    submitBtn.classList.add('hidden');
    loadingBtn.classList.remove('hidden');
    alertDiv.classList.add('hidden');

    try {
      const formData = {
        id: document.getElementById('id').value,
        name: document.getElementById('name').value,
        instructorNumber: document.getElementById('instructorNumber').value,
        contactNumber: document.getElementById('contactNumber').value,
        email: document.getElementById('email').value,
        nicNumber: document.getElementById('nicNumber').value,
        age: parseInt(document.getElementById('age').value),
        address: document.getElementById('address').value
      };

      const response = await fetch('${pageContext.request.contextPath}/api/instructors/' + formData.id, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(formData)
      });

      if (response.ok) {
        showAlert('Instructor updated successfully!', 'success');
        setTimeout(() => {
          window.location.href = '${pageContext.request.contextPath}/instructor-list';
        }, 1500);
      } else {
        const error = await response.json();
        showAlert(error.error || 'Failed to update instructor', 'error');
      }
    } catch (error) {
      showAlert('Network error occurred. Please try again.', 'error');
    } finally {
      submitBtn.classList.remove('hidden');
      loadingBtn.classList.add('hidden');
    }
  });

  function showAlert(message, type) {
    const alertDiv = document.getElementById('alert');
    alertDiv.innerHTML = '<div class="rounded-md p-4 ' + (type === 'success' ? 'bg-green-50' : 'bg-red-50') + '">' +
            '<div class="flex">' +
            '<div class="flex-shrink-0">' +
            '<svg class="h-5 w-5 ' + (type === 'success' ? 'text-green-400' : 'text-red-400') + '" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor">' +
            '<path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clip-rule="evenodd" />' +
            '</svg>' +
            '</div>' +
            '<div class="ml-3">' +
            '<p class="text-sm font-medium ' + (type === 'success' ? 'text-green-800' : 'text-red-800') + '">' +
            message +
            '</p>' +
            '</div>' +
            '</div>' +
            '</div>';
    alertDiv.classList.remove('hidden');
  }
</script>
</body>
</html>