<%@ page import="com.Driving_School_System.modules.instructor_management.InstructorModel" %>
<%@ page import="java.util.List" %>
<%@ page import="com.Driving_School_System.modules.schedule_management.ScheduleModel" %>
<%
  ScheduleModel schedule = (ScheduleModel) request.getAttribute("schedule");
  List<InstructorModel> instructors = (List<InstructorModel>) request.getAttribute("instructors");
%>
<html>
<head>
  <title>Edit Schedule</title>
  <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
</head>
<body class="bg-gray-50">
<div class="container mx-auto px-4 py-8 max-w-3xl">
  <div class="flex items-center mb-6">
    <a href="${pageContext.request.contextPath}/schedule-list" class="mr-4 text-blue-600 hover:text-blue-800">
      <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 19l-7-7m0 0l7-7m-7 7h18" />
      </svg>
    </a>
    <h1 class="text-2xl font-bold text-gray-800">Edit Schedule</h1>
  </div>

  <div id="alert" class="hidden mb-6"></div>
  <div id="loading" class="hidden text-center py-8">
    <svg class="animate-spin mx-auto h-8 w-8 text-blue-600" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
      <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
      <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
    </svg>
  </div>

  <form id="scheduleForm" class="hidden bg-white shadow rounded-lg p-6">
    <input type="hidden" id="id" name="id">
    <div class="grid grid-cols-1 gap-6">

      <div>
        <label for="instructorId" class="block text-sm font-medium text-gray-700 mb-1">Instructor</label>
        <select id="instructorId" name="instructorId" required
                class="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500">
          <option value="">Select Instructor</option>
          <% if (instructors != null && !instructors.isEmpty()) {
            for (InstructorModel instructor : instructors) { %>
          <option value="<%= instructor.getId() %>">
            <%= instructor.getName() %> (<%= instructor.getInstructorNumber() %>)
          </option>
          <% }
          } else { %>
          <option disabled>No instructors available</option>
          <% } %>
        </select>
      </div>


      <div>
        <label for="date" class="block text-sm font-medium text-gray-700 mb-1">Date</label>
        <input type="date" id="date" name="date" required
               class="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500">
      </div>


      <div>
        <label for="time" class="block text-sm font-medium text-gray-700 mb-1">Time</label>
        <input type="time" id="time" name="time" required
               class="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500">
      </div>


      <div>
        <label for="vehicle" class="block text-sm font-medium text-gray-700 mb-1">Vehicle</label>
        <input type="text" id="vehicle" name="vehicle" required
               class="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500"
               pattern="^[A-Z0-9]{3,20}$"
               title="Vehicle should be 3-20 alphanumeric characters">
      </div>

      <div class="flex justify-end space-x-3">
        <button type="button" onclick="window.location.href='${pageContext.request.contextPath}/schedule-list'"
                class="px-4 py-2 border border-gray-300 rounded-md text-sm font-medium text-gray-700 bg-white hover:bg-gray-50">
          Cancel
        </button>
        <button type="submit" id="submitBtn"
                class="px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500">
          Update Schedule
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
    const scheduleId = urlParams.get('id');
    const alertDiv = document.getElementById('alert');
    const loadingDiv = document.getElementById('loading');
    const formDiv = document.getElementById('scheduleForm');

    alertDiv.classList.add('hidden');

    if (!scheduleId || isNaN(scheduleId)) {
      showAlert('Invalid schedule ID provided', 'error');
      loadingDiv.classList.add('hidden');
      return;
    }

    try {
      loadingDiv.classList.remove('hidden');
      formDiv.classList.add('hidden');

      const response = await fetch(${pageContext.request.contextPath}/api/,schedules/+scheduleId);

      if (!response.ok) {
        const error = await response.json();
        throw new Error(error.error || 'Failed to fetch schedule data');
      }

      const schedule = await response.json();

      if (!schedule || !schedule.id) {
        throw new Error('Invalid schedule data received');
      }


      document.getElementById('id').value = schedule.id;
      document.getElementById('instructorId').value = schedule.instructorId;
      document.getElementById('date').value = schedule.date;
      document.getElementById('time').value = schedule.time;
      document.getElementById('vehicle').value = schedule.vehicle;

      formDiv.classList.remove('hidden');
    } catch (error) {
      showAlert(error.message, 'error');
      document.getElementById('submitBtn').disabled = true;
    } finally {
      loadingDiv.classList.add('hidden');
    }
  });

  document.getElementById('scheduleForm').addEventListener('submit', async (e) => {
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
        instructorId: document.getElementById('instructorId').value,
        date: document.getElementById('date').value,
        time: document.getElementById('time').value,
        vehicle: document.getElementById('vehicle').value
      };

      const response = await fetch('${pageContext.request.contextPath}/api/schedules/' + formData.id, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(formData)
      });

      if (response.ok) {
        showAlert('Schedule updated successfully!', 'success');
        setTimeout(() => {
          window.location.href = '${pageContext.request.contextPath}/schedule-list';
        }, 1500);
      } else {
        const error = await response.json();
        showAlert(error.error || 'Failed to update schedule', 'error');
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