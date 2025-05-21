<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.Driving_School_System.modules.schedule_management.ScheduleModel" %>
<%
  List<ScheduleModel> schedules = (List<ScheduleModel>) request.getAttribute("schedules");
%>
<html>
<head>
  <title>Schedule List</title>
  <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
  <script src="https://cdnjs.cloudflare.com/ajax/libs/jspdf/2.5.1/jspdf.umd.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/jspdf-autotable/3.5.28/jspdf.plugin.autotable.min.js"></script>
</head>
<body class="bg-gray-50">
<div class="container mx-auto px-4 py-8">
  <div class="flex justify-between items-center mb-6">
    <h1 class="text-2xl font-bold text-gray-800">Schedule Management</h1>
    <div class="flex space-x-4">
      <input type="text" id="searchInput" placeholder="Search schedules..."
             class="px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500">
      <button onclick="generatePDF()"
              class="px-4 py-2 bg-gray-600 text-white rounded-md hover:bg-gray-700 transition">
        Export PDF
      </button>
      <a href="${pageContext.request.contextPath}/schedule-create"
         class="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition">
        Create New Schedule
      </a>
    </div>
  </div>

  <div id="alert" class="hidden mb-6"></div>

  <div class="bg-white shadow rounded-lg overflow-hidden">
    <div class="overflow-x-auto">
      <table id="scheduleTable" class="min-w-full divide-y divide-gray-200">
        <thead class="bg-gray-50">
        <tr>
          <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">ID</th>
          <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Instructor ID</th>
          <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Date</th>
          <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Time</th>
          <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Vehicle</th>
          <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Actions</th>
        </tr>
        </thead>
        <tbody id="scheduleTableBody" class="bg-white divide-y divide-gray-200">
        <% for (ScheduleModel schedule : schedules) { %>
        <tr data-id="<%= schedule.getId() %>"
            data-instructor="<%= schedule.getInstructorId() %>"
            data-date="<%= schedule.getDate() %>"
            data-time="<%= schedule.getTime() %>"
            data-vehicle="<%= schedule.getVehicle() %>">
          <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500"><%= schedule.getId() %></td>
          <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500"><%= schedule.getInstructorId() %></td>
          <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500"><%= schedule.getDate() %></td>
          <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500"><%= schedule.getTime() %></td>
          <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500"><%= schedule.getVehicle() %></td>
          <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
            <a href="${pageContext.request.contextPath}/schedule-edit?id=<%= schedule.getId() %>"
               class="text-blue-600 hover:text-blue-900 mr-4">Edit</a>
            <button onclick="confirmDelete(<%= schedule.getId() %>)"
                    class="text-red-600 hover:text-red-900">Delete</button>
          </td>
        </tr>
        <% } %>
        </tbody>
      </table>
    </div>
  </div>
</div>

<script>

  const { jsPDF } = window.jspdf;


  document.getElementById('searchInput').addEventListener('input', function() {
    const searchTerm = this.value.toLowerCase();
    const rows = document.querySelectorAll('#scheduleTableBody tr');

    rows.forEach(row => {
      const instructor = row.getAttribute('data-instructor').toLowerCase();
      const date = row.getAttribute('data-date').toLowerCase();
      const time = row.getAttribute('data-time').toLowerCase();
      const vehicle = row.getAttribute('data-vehicle').toLowerCase();

      if (instructor.includes(searchTerm) || date.includes(searchTerm) ||
              time.includes(searchTerm) || vehicle.includes(searchTerm)) {
        row.style.display = '';
      } else {
        row.style.display = 'none';
      }
    });
  });


  function generatePDF() {
    const doc = new jsPDF();
    const table = document.getElementById('scheduleTable');
    const headers = [];
    const data = [];


    table.querySelectorAll('thead th').forEach(th => {
      headers.push(th.textContent.trim());
    });


    table.querySelectorAll('tbody tr').forEach(tr => {
      if (tr.style.display !== 'none') {
        const rowData = [];
        tr.querySelectorAll('td').forEach(td => {
          rowData.push(td.textContent.trim());
        });
        data.push(rowData);
      }
    });


    doc.autoTable({
      head: [headers],
      body: data,
      theme: 'grid',
      styles: { fontSize: 8 },
      headStyles: { fillColor: [41, 128, 185] }
    });

    doc.save('schedules-list.pdf');
  }

  async function confirmDelete(id) {
    if (confirm('Are you sure you want to delete this schedule?')) {
      try {
        const response = await fetch('${pageContext.request.contextPath}/api/schedules/' + id, {
          method: 'DELETE'
        });

        if (response.ok) {
          showAlert('Schedule deleted successfully!', 'success');
          setTimeout(() => {
            window.location.reload();
          }, 1500);
        } else {
          const error = await response.json();
          showAlert(error.error || 'Failed to delete schedule', 'error');
        }
      } catch (error) {
        showAlert('Network error occurred. Please try again.', 'error');
      }
    }
  }

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