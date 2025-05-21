<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.Driving_School_System.modules.feedback_management.FeedbackModel" %>
<%
  List<FeedbackModel> feedbacks = (List<FeedbackModel>) request.getAttribute("feedbacks");
%>
<html>
<head>
  <title>Feedback List</title>
  <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
  <script src="https://cdnjs.cloudflare.com/ajax/libs/jspdf/2.5.1/jspdf.umd.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/jspdf-autotable/3.5.28/jspdf.plugin.autotable.min.js"></script>
</head>
<body class="bg-gray-50">
<div class="container mx-auto px-4 py-8">
  <div class="flex justify-between items-center mb-6">
    <div class="flex items-center">
      <a href="${pageContext.request.contextPath}/" class="mr-4 text-blue-600 hover:text-blue-800">
        <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 19l-7-7m0 0l7-7m-7 7h18" />
        </svg>
      </a>
      <h1 class="text-2xl font-bold text-gray-800">Feedback Management</h1>
    </div>
    <div class="flex space-x-4">
      <input type="text" id="searchInput" placeholder="Search feedbacks..."
             class="px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500">
      <button onclick="generatePDF()"
              class="px-4 py-2 bg-gray-600 text-white rounded-md hover:bg-gray-700 transition">
        Export PDF
      </button>
    </div>
  </div>

  <div id="alert" class="hidden mb-6"></div>

  <div class="bg-white shadow rounded-lg overflow-hidden">
    <div class="overflow-x-auto">
      <table id="feedbackTable" class="min-w-full divide-y divide-gray-200">
        <thead class="bg-gray-50">
        <tr>
          <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">ID</th>
          <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Feedback</th>
          <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Rating</th>
          <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Related To</th>
          <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Additional Comments</th>
        </tr>
        </thead>
        <tbody id="feedbackTableBody" class="bg-white divide-y divide-gray-200">
        <% if (feedbacks != null && !feedbacks.isEmpty()) { %>
        <% for (FeedbackModel feedback : feedbacks) { %>
        <tr data-id="<%= feedback.getId() %>"
            data-feedback="<%= feedback.getFeedback() != null ? feedback.getFeedback() : "" %>"
            data-stars="<%= feedback.getNumOfStars() %>"
            data-related="<%= feedback.getRelatedTo() != null ? feedback.getRelatedTo() : "" %>"
            data-comments="<%= feedback.getAdditionalComments() != null ? feedback.getAdditionalComments() : "" %>">
          <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500"><%= feedback.getId() %></td>
          <td class="px-6 py-4 text-sm text-gray-500">
            <%= feedback.getFeedback() != null ? (feedback.getFeedback().length() > 50 ? feedback.getFeedback().substring(0, 50) + "..." : feedback.getFeedback()) : "N/A" %>
          </td>
          <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
            <div class="flex">
              <% for (int i = 1; i <= 5; i++) { %>
              <% if (i <= feedback.getNumOfStars()) { %>
              <svg class="h-5 w-5 text-yellow-400" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor">
                <path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118l-2.8-2.034c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z" />
              </svg>
              <% } else { %>
              <svg class="h-5 w-5 text-gray-300" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor">
                <path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118l-2.8-2.034c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z" />
              </svg>
              <% } %>
              <% } %>
            </div>
          </td>
          <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
            <%= feedback.getRelatedTo() != null ? feedback.getRelatedTo() : "N/A" %>
          </td>
          <td class="px-6 py-4 text-sm text-gray-500">
            <%= feedback.getAdditionalComments() != null ? (feedback.getAdditionalComments().length() > 50 ? feedback.getAdditionalComments().substring(0, 50) + "..." : feedback.getAdditionalComments()) : "N/A" %>
          </td>
        </tr>
        <% } %>
        <% } else { %>
        <tr>
          <td colspan="5" class="px-6 py-4 text-center text-sm text-gray-500">
            <div class="flex flex-col items-center justify-center py-6">
              <svg class="h-12 w-12 text-gray-400 mb-4" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9.172 16.172a4 4 0 015.656 0M9 10h.01M15 10h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
              <p class="text-gray-500 text-lg">No feedbacks found</p>
              <p class="text-gray-400 text-sm mt-1">Feedbacks will appear here once they are submitted</p>
            </div>
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
    const rows = document.querySelectorAll('#feedbackTableBody tr');

    rows.forEach(row => {

      if (!row.hasAttribute('data-id')) {
        return;
      }

      const feedback = row.getAttribute('data-feedback').toLowerCase();
      const stars = row.getAttribute('data-stars').toLowerCase();
      const related = row.getAttribute('data-related').toLowerCase();
      const comments = row.getAttribute('data-comments').toLowerCase();

      if (feedback.includes(searchTerm) || stars.includes(searchTerm) ||
              related.includes(searchTerm) || comments.includes(searchTerm)) {
        row.style.display = '';
      } else {
        row.style.display = 'none';
      }
    });


    const visibleRows = Array.from(rows).filter(row => row.style.display !== 'none' && row.hasAttribute('data-id'));
    const emptyStateRow = document.querySelector('#feedbackTableBody tr:not([data-id])');

    if (visibleRows.length === 0 && emptyStateRow) {
      emptyStateRow.style.display = '';
    } else if (emptyStateRow) {
      emptyStateRow.style.display = 'none';
    }
  });


  function generatePDF() {
    const doc = new jsPDF();
    const table = document.getElementById('feedbackTable');
    const headers = [];
    const data = [];


    table.querySelectorAll('thead th').forEach(th => {
      headers.push(th.textContent.trim());
    });


    table.querySelectorAll('tbody tr[data-id]').forEach(tr => {
      if (tr.style.display !== 'none') {
        const rowData = [];
        tr.querySelectorAll('td').forEach((td, index) => {

          if (index === 2) {
            rowData.push(tr.getAttribute('data-stars') + ' Stars');
          } else {
            rowData.push(td.textContent.trim());
          }
        });
        data.push(rowData);
      }
    });


    if (data.length === 0) {
      showAlert('No data to export', 'error');
      return;
    }


    doc.autoTable({
      head: [headers],
      body: data,
      theme: 'grid',
      styles: { fontSize: 8 },
      headStyles: { fillColor: [41, 128, 185] }
    });

    doc.save('feedback-list.pdf');
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


    setTimeout(() => {
      alertDiv.classList.add('hidden');
    }, 3000);
  }
</script>
</body>
</html>